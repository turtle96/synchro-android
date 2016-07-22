package sg.edu.nus.comp.orbital.synchro.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import sg.edu.nus.comp.orbital.synchro.EditProfileFragment;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;

/**
 * Created by angja_000 on 22/7/2016.
 */
public class AsyncTaskPutMe {

    private static ProgressDialog progressDialog;
    private static FragmentManager fragmentManager;
    private static String description;
    private static boolean result;

    public static void load(ProgressDialog pd, String desc, FragmentManager manager) {
        progressDialog = pd;
        description = desc;
        fragmentManager = manager;

        LoadPutMe loadPutMe = new LoadPutMe();
        loadPutMe.execute();
    }

    private static class LoadPutMe extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = SynchroAPI.getInstance().putMe(description);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void param) {
            if (progressDialog!=null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            EditProfileFragment.redirectAfterUpdate(result, fragmentManager);
        }
    }
}
