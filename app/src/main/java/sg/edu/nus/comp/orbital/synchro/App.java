package sg.edu.nus.comp.orbital.synchro;

import android.app.Application;
import android.content.Context;

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

        //TODO: set previously stored authtoken to SynchroAPI if user has logged in before
    }

    public static Context getContext(){
        return appContext;
    }
}
