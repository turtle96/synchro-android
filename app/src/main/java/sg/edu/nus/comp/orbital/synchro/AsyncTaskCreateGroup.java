package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;

/**
 * Created by angja_000 on 17/7/2016.
 */
public class AsyncTaskCreateGroup {
    private static FragmentManager fragmentManager;
    private static ProgressDialog progressDialog;
    private static GroupData newGroupData;
    private static String groupId;

    public static void loadCreateGroup(ProgressDialog pd, GroupData gd, FragmentManager manager) {
        progressDialog = pd;
        newGroupData = gd;
        fragmentManager = manager;

        LoadCreate loadCreate = new LoadCreate();
        loadCreate.execute();
    }

    private static class LoadCreate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Creating...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                groupId = SynchroAPI.getInstance().postNewGroup(newGroupData);
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
            CreateGroupFragment.createGroupLoaded(groupId, fragmentManager);
        }
    }
}
