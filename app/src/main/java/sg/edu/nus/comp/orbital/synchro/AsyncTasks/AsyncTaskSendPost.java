package sg.edu.nus.comp.orbital.synchro.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;

import sg.edu.nus.comp.orbital.synchro.DataHolders.Post;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;
import sg.edu.nus.comp.orbital.synchro.ViewPostsFragment;

/**
 * Created by angja_000 on 22/7/2016.
 */
public class AsyncTaskSendPost {

    private static ProgressDialog progressDialog;
    private static FragmentManager fragmentManager;
    private static Post post;
    private static boolean result;

    public static void load(ProgressDialog pd, Post p, FragmentManager manager) {
        progressDialog = pd;
        post = p;
        fragmentManager = manager;

        LoadSendPost loadSendPost = new LoadSendPost();
        loadSendPost.execute();
    }

    private static class LoadSendPost extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Posting...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                result = SynchroAPI.getInstance().postNewPost(post);
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
            ViewPostsFragment.redirectAfterPost(result, fragmentManager);
        }
    }
}
