package sg.edu.nus.comp.orbital.synchro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SynchroAPI.validate()) {
                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute();

                    Intent launchMainActivity = new Intent(SplashActivity.this, DrawerActivity.class);
                    launchMainActivity.putExtra("caller", "SplashActivity");
                    SplashActivity.this.startActivity(launchMainActivity);
                    SplashActivity.this.finish();
                }
                else{
                    Intent launchLoginActivity = new Intent(SplashActivity.this, LoginActivity.class);
                    launchLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    SplashActivity.this.startActivity(launchLoginActivity);
                    SplashActivity.this.finish();
                }

            }
        }, 3000);
    }

    private class AsyncTaskRunner extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                SynchroDataLoader.loadProfileData();
                SynchroDataLoader.loadGroupsJoinedData();
                SynchroDataLoader.loadViewGroupData();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            Toast.makeText(App.getContext(), "data loaded", Toast.LENGTH_LONG).show();
        }
    }

}
