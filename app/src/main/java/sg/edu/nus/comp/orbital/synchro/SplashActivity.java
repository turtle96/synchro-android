package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SynchroAPI.validate()) {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
                    AsyncTaskRunner.setProgressBar(progressBar);
                    AsyncTaskRunner.loadInitialData(SplashActivity.this);
                }
                else{
                    Intent launchLoginActivity = new Intent(SplashActivity.this, LoginActivity.class);
                    launchLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    SplashActivity.this.startActivity(launchLoginActivity);
                    SplashActivity.this.finish();
                }
            }
        }, 1000);

    }

    //allows AsyncTaskRunner to call redirect once processing is finished
    public void redirectFromSplash() {
        Intent launchMainActivity = new Intent(SplashActivity.this, DrawerActivity.class);
        launchMainActivity.putExtra("caller", "SplashActivity");
        SplashActivity.this.startActivity(launchMainActivity);
        SplashActivity.this.finish();
    }

}
