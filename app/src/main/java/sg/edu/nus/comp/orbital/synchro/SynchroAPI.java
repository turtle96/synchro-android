package sg.edu.nus.comp.orbital.synchro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by kfwong on 6/2/16.
 */
public class SynchroAPI {

    private static SynchroAPI self;
    private static String ivleApiKey = "PK3n2PGjXR4OooZPZyelQ";
    private static String ivleAuthToken;

    // the following attributes is for self signed SSL cert in staging/dev server. Useless if the SSL is verfied CA.
    private static SSLContext sslContext;
    private static TrustManagerFactory trustManagerFactory;

    // IVLE Specific endpoints
    public static String ivleLogin = "https://ivle.nus.edu.sg/api/login/?apikey=" + ivleApiKey;
    public static String ivleLoginSuccess = "https://ivle.nus.edu.sg/api/login/login_result.ashx?apikey=" + ivleApiKey + "&r=0";
    private static String ivleValidate = "https://ivle.nus.edu.sg/api/Lapi.svc/Validate?APIKey=" + ivleApiKey + "&Token=" + ivleAuthToken;

    // Synchro API endpoints
    private static final String API_BASE_URL = "https://ec2-52-77-240-7.ap-southeast-1.compute.amazonaws.com/api/v1/";
    private static final String apiMeResync = API_BASE_URL + "me/resync";
    private static final String apiMe = API_BASE_URL + "me";
    private static final String apiMeModules = API_BASE_URL + "me/modulesTaken";
    private static final String apiUsers = API_BASE_URL + "users";
    private static final String apiGroups = API_BASE_URL + "groups";

    private SynchroAPI(String ivleAuthToken) {
        // default private singleton
        this.ivleAuthToken = ivleAuthToken;
    }

    public static SynchroAPI getInstance() {
        if (self == null) {
            // not init
            // perform login, forward intent to LoginActivity
            Log.d("Synchro", "SynchroApi is not configured properly. Have you logged in?");
        }
        return self;
    }

    //configures app given authentication token
    //need to call every time app launches, so that getInstance() works
    public static void authenticate(String ivleAuthToken) {
        self = new SynchroAPI(ivleAuthToken);
        configureSelfSignedSSL();
        configureIon();
    }

    private static void configureSelfSignedSSL() {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca = cf.generateCertificate(App.getContext().getResources().openRawResource(R.raw.synchro_api_x509_der));
            Log.d("Synchro", ((X509Certificate) ca).getSubjectDN().toString());

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("ca", ca);

            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(ks);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
        } catch (Exception ex) {
            Log.d("Synchro", "Self signed SSL configuration error");
            ex.printStackTrace();
        }
    }

    private static void configureIon(){
        Ion ion = Ion.getDefault(App.getContext());
        ion.getHttpClient().getSSLSocketMiddleware().setSSLContext(sslContext);
        ion.getHttpClient().getSSLSocketMiddleware().setTrustManagers(trustManagerFactory.getTrustManagers());
    }

    //ensures variables using authToken are updated from SharedPrefs after receiving token
    //this takes care of the null pointer issue when app is relaunched after successful login
    public static void updateToken(String token) {
        ivleAuthToken = token;
        ivleValidate = "https://ivle.nus.edu.sg/api/Lapi.svc/Validate?APIKey=" + ivleApiKey + "&Token=" + ivleAuthToken;
    }

    //for validation of current token
    //updates token if new token received
    //returns string based on validation result for action to be taken accordingly
    public static String validate() {
        updateToken(AuthToken.getToken());  //ensures token variable in SynchroApi is updated from SharedPrefs

        JsonObject result = null;
        try {
            result = Ion.with(App.getContext())
                    .load(ivleValidate)
                    .asJsonObject()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if (result == null) {   //no object returned
            return "Error";
        }
        else if (result.get("Success").toString().equals("true")) {

            /*  checks if returned token is a newly generated one for replacement
                =_= apparently the returned token is within "" so compare properly!
            */
            if (!result.get("Token").toString().replaceAll("\"", "").equals(AuthToken.getToken())) {
                //takes out the "" marks
                AuthToken.setToken(result.get("Token").toString().replaceAll("\"", ""));
                updateToken(AuthToken.getToken());
            }
            authenticate(AuthToken.getToken());
            return "Successful";
        }
        else {  //validate unsuccessful
            return "Fail";
        }
    }

    //Retrieve current authenticated User's info
    public JsonObject getMe() {
        JsonObject result = null;
        try {
            result = Ion.with(App.getContext())
                    .load(apiMe)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonObject()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //Resynchronize & cache current user info from IVLE
    public JsonObject getMeResync() {
        JsonObject result = null;
        try {
            result = Ion.with(App.getContext())
                    .load(apiMeResync)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonObject()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //Retrieve list of Modules that current authenticated User has taken
    //JsonArray
    public JsonArray getMeModules() {
        JsonArray result = null;
        try {
            result = Ion.with(App.getContext())
                    .load(apiMeModules)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonArray()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //Retrieve list of Groups a particular User belongs to, given user id
    //JsonArray
    public JsonArray getGroupsByUserId(int userId) {
        JsonArray result = null;
        String apiUserGroups = API_BASE_URL + "users/" + userId +"/groups";
        try {
            result = Ion.with(App.getContext())
                    .load(apiUserGroups)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonArray()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //Retrieve list of Users belonging to a particular Group, given Group id
    //JsonArray
    public JsonArray getUsersByGroupId(int groupId) {
        JsonArray result = null;
        String apiGroupUsers = API_BASE_URL + "groups/" + groupId +"/users";
        try {
            result = Ion.with(App.getContext())
                    .load(apiGroupUsers)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonArray()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //retrieve list of all users on server
    //JsonArray
    public JsonArray getAllUsers() {
        JsonArray result = null;

        try {
            result = Ion.with(App.getContext())
                    .load(apiUsers)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonArray()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //retrieve list of all groups on server
    //JsonArray
    public JsonArray getAllGroups() {
        JsonArray result = null;

        try {
            result = Ion.with(App.getContext())
                    .load(apiGroups)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonArray()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }
}
