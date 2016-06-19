package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SynchroAPI.validate()) {
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

}
