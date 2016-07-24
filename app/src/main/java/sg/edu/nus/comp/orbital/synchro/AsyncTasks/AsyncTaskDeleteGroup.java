package sg.edu.nus.comp.orbital.synchro.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import sg.edu.nus.comp.orbital.synchro.SynchroAPI;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 24/7/2016.
 */
public class AsyncTaskDeleteGroup {

    private static ProgressDialog progressDialog;
    private static FragmentManager fragmentManager;
    private static String groupId;
    private static boolean result;

    public static void load(ProgressDialog pd, String id, FragmentManager manager) {
        progressDialog = pd;
        fragmentManager = manager;
        groupId = id;

        LoadDeleteGroup loadDeleteGroup = new LoadDeleteGroup();
        loadDeleteGroup.execute();
    }

    private static class LoadDeleteGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Deleting group...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = SynchroAPI.getInstance().deleteGroup(groupId);
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
            ViewGroupFragment.redirectAfterDeleteGroup(result, fragmentManager);
        }
    }
}
