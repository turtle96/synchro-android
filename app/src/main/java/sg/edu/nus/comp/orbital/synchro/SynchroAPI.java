package sg.edu.nus.comp.orbital.synchro;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.gson.GsonArrayParser;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;

/**
 * Created by kfwong on 6/2/16.
 */
public class SynchroAPI {

    private static SynchroAPI self;
    private static String ivleApiKey = "PK3n2PGjXR4OooZPZyelQ";
    private static String ivleAuthToken = AuthToken.getToken();

    // the following attributes is for self signed SSL cert in staging/dev server. Useless if the SSL is verfied CA.
    private static SSLContext sslContext;
    private static TrustManagerFactory trustManagerFactory;

    // IVLE Specific endpoints
    public static String ivleLogin = "https://ivle.nus.edu.sg/api/login/?apikey=" + ivleApiKey;
    public static String ivleLoginSuccess = "https://ivle.nus.edu.sg/api/login/login_result.ashx?apikey=" + ivleApiKey + "&r=0";
    private static String ivleValidate = "https://ivle.nus.edu.sg/api/Lapi.svc/Validate?APIKey=" + ivleApiKey + "&Token=" + ivleAuthToken;

    // Synchro API endpoints
    private static final String API_BASE_URL = "https://52.77.240.7/api/v1/";
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
        //System.out.println("token " + ivleAuthToken);
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

            if (!result.get("Token").getAsString().equals(AuthToken.getToken())) {
                AuthToken.setToken(result.get("Token").getAsString());
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

    //retrieve a user's details by id
    public JsonObject getUserById(String userId) {
        JsonObject result = null;
        String apiUsersId = apiUsers + "/" + userId;
        try {
            result = Ion.with(App.getContext())
                    .load(apiUsersId)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonObject()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

    //retrieve a group's details by id
    public JsonObject getGroupById(String groupId) {
        JsonObject result = null;
        String apiGroupsId = apiGroups + "/" + groupId;
        try {
            result = Ion.with(App.getContext())
                    .load(apiGroupsId)
                    .addHeader("Authorization", ivleAuthToken)
                    .asJsonObject()
                    .get();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;

    }

    //Retrieve list of Groups a particular User belongs to, given user id
    //JsonArray
    public JsonArray getGroupsByUserId(String userId) {
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

    //Retrieve list of Users belonging to a particular GroupData, given GroupData id
    //JsonArray
    public JsonArray getUsersByGroupId(String groupId) {
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

    public String postNewGroup(GroupData group) {

        JsonObject groupJson = group.parseToPostGroupJson();
        System.out.println("Json here " + groupJson.toString());

        String id = "default id";
        JsonObject result = null;

        try {
            result = Ion.with(App.getContext())
                    .load(apiGroups)
                    .setHeader("Authorization", ivleAuthToken)
                    .setHeader("Content-Type", "application/json")
                    .setJsonObjectBody(groupJson)
                    .asJsonObject()
                    .get();

        }catch (Exception ex){
            System.out.println("Error here" + ex.toString());
        }


        if (result == null) {
            Toast.makeText(App.getContext(), "Error creating group", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(App.getContext(), "group created", Toast.LENGTH_SHORT).show();
            id = result.get("id").getAsString();
            System.out.println("here group id: " + id);
        }

        return id;
    }
}
