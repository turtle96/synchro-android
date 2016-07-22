package sg.edu.nus.comp.orbital.synchro.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.EditGroupFragment;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;

/**
 * Created by angja_000 on 22/7/2016.
 */
public class AsyncTaskEditGroup {

    private static FragmentManager fragmentManager;
    private static ProgressDialog progressDialog;
    private static GroupData groupData;
    private static String groupId;
    private static boolean result;

    public static void load(ProgressDialog pd, String id, GroupData gd, FragmentManager manager) {
        progressDialog = pd;
        groupId = id;
        groupData = gd;
        fragmentManager = manager;

        LoadUpdate loadUpdate = new LoadUpdate();
        loadUpdate.execute();

    }

    private static class LoadUpdate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Updating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = SynchroAPI.getInstance().putNewGroup(groupData);
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
            EditGroupFragment.editGroupLoaded(result, groupId, fragmentManager);
        }
    }
}
