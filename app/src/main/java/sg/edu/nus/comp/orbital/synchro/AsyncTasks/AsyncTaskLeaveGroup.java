package sg.edu.nus.comp.orbital.synchro.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import sg.edu.nus.comp.orbital.synchro.SynchroAPI;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 20/7/2016.
 */
public class AsyncTaskLeaveGroup {

    private static ProgressDialog progressDialog;
    private static FragmentManager fragmentManager;
    private static String groupId;
    private static boolean result;

    public static void load(ProgressDialog pd, String id, FragmentManager manager) {
        progressDialog = pd;
        fragmentManager = manager;
        groupId = id;

        LoadLeaveGroup loadLeaveGroup = new LoadLeaveGroup();
        loadLeaveGroup.execute();
    }

    private static class LoadLeaveGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Leaving group...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = SynchroAPI.getInstance().postLeaveGroup(groupId);
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
            ViewGroupFragment.redirectAfterLeaveGroup(result, fragmentManager);
        }
    }
}
