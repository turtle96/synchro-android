package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by angja_000 on 18/6/2016.
 *
 * runs all async tasks needed
 * reduce code repetition
 */
public class AsyncTaskRunner {

    private static ProgressDialog progressDialog;

    //sets progress dialog from whichever activity/fragment the async task is called
    public static void setProgressDialog(ProgressDialog pd) {
        progressDialog = pd;
    }

    //resyncs and loads user's personal data on app launch
    //calls resync on API, gets profile data and groups joined list data
    public static void runLoadInitialData() {
        LoadInitialData runner = new LoadInitialData();
        runner.execute();
    }

    private static class LoadInitialData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            if (progressDialog != null) {
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                SynchroAPI.getInstance().getMeResync();
                SynchroDataLoader.loadProfileData();
                SynchroDataLoader.loadGroupsJoinedData(1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            //Toast.makeText(App.getContext(), "data loaded", Toast.LENGTH_LONG).show();
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
