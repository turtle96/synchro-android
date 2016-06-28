package sg.edu.nus.comp.orbital.synchro;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpGet;

import java.util.concurrent.ExecutionException;

/**
 * Created by kfwong on 6/2/16.
 * This class is used for initialize variables for the application
 */
public class App extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.appContext = this;
    }

    public static Context getContext(){
        return appContext;
    }

}
