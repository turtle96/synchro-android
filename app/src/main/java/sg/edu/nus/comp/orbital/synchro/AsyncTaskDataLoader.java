package sg.edu.nus.comp.orbital.synchro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by angja_000 on 18/6/2016.
 *
 * Runs AsyncTasks for app launch, displays progress indicators accordingly
 * NOTE: to check on sequence of task execution, or check on speeds, use the commented system.out.println statements
 *
 * available via:   DrawerActivity (after login, first-time use only)
 *                  SplashActivity (subsequent uses)
 * loads:   Resync call
 *          User profile
 *          User Modules
 *          User groups
 *
 */
public class AsyncTaskDataLoader {

    private static SplashActivity splashActivity;
    private static DrawerActivity drawerActivity;

    private static ProgressBar progressBar;         //for SplashActivity
    private static ProgressDialog progressDialog;   //for after Login redirect to DrawerActivity

    private static boolean resyncFinished = false;
    private static boolean profileFinished = false;
    private static boolean modulesFinished = false;
    private static boolean groupsFinished = false;

    //sets progress dialog from whichever activity/fragment the async task is called
    public static void setProgressDialog(ProgressDialog pd) {
        progressDialog = pd;
    }
    //sets progress bar
    public static void setProgressBar(ProgressBar pb) {progressBar = pb;}


    /*  resyncs and loads user's personal data on app launch
        calls resync on API, gets profile data, modules and groups joined list data
        runs 4 separate AsyncTasks for faster execution

        will handle showing progress differently for after Login and Splashscreen loading
        after login: loading will be done in DrawerActivity + progressDialog + redirect to GroupsJoined
        splashscreen: loading will be done in SplashActivity + progressBar + redirect to DrawerActivity
    */
    public static void loadInitialData(Activity activity) {
        if (activity instanceof SplashActivity) {
            splashActivity = (SplashActivity) activity;
            drawerActivity = null;
        }
        else if (activity instanceof DrawerActivity) {
            drawerActivity = (DrawerActivity) activity;
            splashActivity = null;
        }

        initializeProgress();

        LoadResync loadResync = new LoadResync();
        LoadProfile loadProfile = new LoadProfile();
        LoadGroupsJoined loadGroupsJoined = new LoadGroupsJoined();
        LoadModules loadModules = new LoadModules();

        //this should ensure first time users are cached on server (resynced) BEFORE calls to server are made
        //to prevent NullPointerExceptions
        if (drawerActivity != null) {
            SynchroAPI.getInstance().getMeResync();
            resyncFinished = true;
            //System.out.println("Resync done");
        }
        else {
            loadResync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        loadProfile.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        loadGroupsJoined.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        loadModules.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //ensures progress dialog is stopped only when all tasks are finished
    //login will require resync call to be finished first
    //if user already logged in, resync will load in background
    private static boolean getLoadInitialDataStatus() {
        if (drawerActivity != null) {
            return (resyncFinished && profileFinished && groupsFinished && modulesFinished);
        }
        else if (splashActivity != null) {
            return (profileFinished && groupsFinished && modulesFinished);
        }
        else {
            return false;
        }
    }

    //checks which progress type to use and display accordingly
    private static void initializeProgress() {
        if (progressDialog != null) {
            progressDialog.setMessage("Syncing user data");
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

            if (drawerActivity != null) {
                dismissProgressDialog();
            }
            Toast.makeText(App.getContext(), "Resync Done", Toast.LENGTH_SHORT).show();
            //System.out.println("Resync done");
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

            //System.out.println("Profile done");
        }
    }

    /////////// Loader for Calling Modules /////////////
    private static class LoadModules extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroDataLoader.loadModules();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            modulesFinished = true;
            dismissProgressDialog();

            //System.out.println("Modules done");
        }
    }

    /////////// Loader for Calling Groups Joined list /////////////
    private static class LoadGroupsJoined extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroDataLoader.loadGroupsJoinedData();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            groupsFinished = true;
            dismissProgressDialog();

            //System.out.println("Groups done");
        }
    }
}
