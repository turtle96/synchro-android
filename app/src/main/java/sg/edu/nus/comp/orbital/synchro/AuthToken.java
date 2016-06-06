package sg.edu.nus.comp.orbital.synchro;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by angja_000 on 6/6/2016.
 *
 * for accessing token stored in SharedPref
 */
public class AuthToken {
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    public AuthToken(Context context) {
        prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean setToken(String token) {
        editor.putString("ivleAuthToken", token);
        editor.commit();
        return true;
    }

    public String getToken() {return prefs.getString("ivleAuthToken", "Error");}
}
