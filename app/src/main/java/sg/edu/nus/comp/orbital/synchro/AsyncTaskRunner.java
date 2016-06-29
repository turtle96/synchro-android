package sg.edu.nus.comp.orbital.synchro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by angja_000 on 18/6/2016.
 *
 * Runs AsyncTasks for app, displays progress indicators accordingly
 *
 */
public class AsyncTaskRunner {

    private static SplashActivity splashActivity;
    private static DrawerActivity drawerActivity;

    private static ProgressBar progressBar;         //for SplashActivity
    private static ProgressDialog progressDialog;   //for after Login redirect to DrawerActivity

    private static boolean resyncFinished = false;
    private static boolean profileFinished = false;
    private static boolean groupsFinished = false;

    //sets progress dialog from whichever activity/fragment the async task is called
    public static void setProgressDialog(ProgressDialog pd) {
        progressDialog = pd;
    }
    //sets progress bar
    public static void setProgressBar(ProgressBar pb) {progressBar = pb;}


    /*  resyncs and loads user's personal data on app launch
        calls resync on API, gets profile data and groups joined list data
        runs 3 separate AsyncTasks for faster execution

        will handle showing progress differently for after Login and Splashscreen loading
        after login: loading will be done in DrawerActivity + progressDialog + redirect to GroupsJoined
        splashscreen: loading will be done in SplashActivity + progressBar + redirect to DrawerActivity
    */
    public static void loadInitialData(Activity activity) {
        if (activity instanceof SplashActivity) {
            splashActivity = (SplashActivity) activity;
        }
        else if (activity instanceof DrawerActivity) {
            drawerActivity = (DrawerActivity) activity;
        }

        LoadResync loadResync = new LoadResync();
        LoadProfile loadProfile = new LoadProfile();
        LoadGroupsJoined loadGroupsJoined = new LoadGroupsJoined();

        loadResync.execute();
        loadProfile.execute();
        loadGroupsJoined.execute();
    }

    //ensures progress dialog is stopped only when all 3 tasks are finished
    private static boolean getLoadInitialDataStatus() {
        return (resyncFinished && profileFinished && groupsFinished);
    }

    //checks which progress type to use and display accordingly
    private static void initializeProgress() {
        if (progressDialog != null) {
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        else if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    //checks which progress type is being used and stops display accordingly
    //also calls redirect from SplashActivity if SplashActivity is detected
    private static void dismissProgressDialog() {
        if (getLoadInitialDataStatus()) {
            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            else if (progressBar!=null) {
                progressBar.setVisibility(View.GONE);
            }

            if (splashActivity != null) {
                splashActivity.redirectFromSplash();
            }
            else if (drawerActivity != null) {
                drawerActivity.redirectToGroupsJoined();
            }
        }

    }

    /////////// Loader for Resyncing User Data /////////////
    private static class LoadResync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            initializeProgress();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroAPI.getInstance().getMeResync();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            resyncFinished = true;
            dismissProgressDialog();
        }
    }

    /////////// Loader for Calling Profile /////////////
    private static class LoadProfile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroDataLoader.loadProfileData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            profileFinished = true;
            dismissProgressDialog();
        }
    }

    /////////// Loader for Calling Groups Joined list /////////////
    private static class LoadGroupsJoined extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroDataLoader.loadGroupsJoinedData(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            groupsFinished = true;
            dismissProgressDialog();
        }
    }
}
