package sg.edu.nus.comp.orbital.synchro;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by angja_000 on 6/6/2016.
 *
 * for accessing token stored in SharedPref
 */
public class AuthToken {

    private static SharedPreferences prefs = App.getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = prefs.edit();

    public static boolean setToken(String token) {
        editor.putString("ivleAuthToken", token);
        editor.commit();
        return true;
    }

    public static String getToken() {return prefs.getString("ivleAuthToken", "Error");}
}
