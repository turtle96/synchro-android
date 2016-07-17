package sg.edu.nus.comp.orbital.synchro;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class SplashActivity extends AppCompatActivity {

    private static final String STATUS_SUCCESS = "Successful";
    private static final String STATUS_FAIL = "Fail";
    private static final String STATUS_ERROR = "Error";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String status = SynchroAPI.validate();

                if (status.equals(STATUS_SUCCESS)) {
                    ProgressBar progressBar = (ProgressBar) findViewById(R.id.splashProgressBar);
                    AsyncTaskDataLoader.setProgressBar(progressBar);
                    AsyncTaskDataLoader.loadInitialData(SplashActivity.this);
                }
                else if (status.equals(STATUS_ERROR)) {
                    Snackbar notifyInternet = Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Internet please", Snackbar.LENGTH_INDEFINITE);
                    notifyInternet.setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reloadSplash();
                        }
                    });
                    notifyInternet.show();
                }
                else if (status.equals(STATUS_FAIL)) {
                    Intent launchLoginActivity = new Intent(SplashActivity.this, LoginActivity.class);
                    launchLoginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    SplashActivity.this.startActivity(launchLoginActivity);
                    SplashActivity.this.finish();
                }
            }
        }, 1000);

    }

    //allows AsyncTaskDataLoader to call redirect once loadInitialData is finished
    public void redirectFromSplash() {
        Intent launchMainActivity = new Intent(SplashActivity.this, DrawerActivity.class);
        launchMainActivity.putExtra("caller", "SplashActivity");
        SplashActivity.this.startActivity(launchMainActivity);
        SplashActivity.this.finish();
    }

    //for reloading if internet connection is disrupted
    public void reloadSplash() {
        Intent launchSplashActivity = new Intent(SplashActivity.this, SplashActivity.class);
        SplashActivity.this.startActivity(launchSplashActivity);
        SplashActivity.this.finish();
    }

}
