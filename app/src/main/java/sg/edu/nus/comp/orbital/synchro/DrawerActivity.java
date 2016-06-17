package sg.edu.nus.comp.orbital.synchro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.JsonObject;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        String caller = getIntent().getStringExtra("caller");
        if (caller.equals("LoginActivity")) {
            SynchroAPI.authenticate(AuthToken.getToken());
            SynchroAPI.updateToken(AuthToken.getToken());
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        //delay so drawer closes smoothly
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // initialize fragment manager
                FragmentManager manager = getSupportFragmentManager();

                // add all fragment into backstack
                FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
                transaction.setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left,
                        R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right);

                // replace the content_fragment with appropriate view
                switch(id){
                    case R.id.nav_new_group:
                        transaction.replace(R.id.content_fragment, NewGroupFragment.newInstance());
                        break;
                    case R.id.nav_recommendations:
                        transaction.replace(R.id.content_fragment, RecommendationsFragment.newInstance());
                        break;
                    case R.id.nav_search:
                        transaction.replace(R.id.content_fragment, SearchFragment.newInstance());
                        break;
                    case R.id.nav_profile:
                        transaction.replace(R.id.content_fragment, ProfileFragment.newInstance());
                        break;
                    case R.id.nav_groups_joined:
                        transaction.replace(R.id.content_fragment, GroupsJoinedFragment.newInstance());
                        break;
                    case R.id.nav_login:
                        // do not add loginactivity into backstack
                        Intent loginActivity = new Intent(DrawerActivity.this, LoginActivity.class);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(loginActivity);
                    case R.id.nav_view_group:
                        transaction.replace(R.id.content_fragment, ViewGroupFragment.newInstance());
                        break;

                }

                transaction.commit();

            }
        }, 300);

        return true;
    }

    private class AsyncTaskRunner extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                SynchroDataLoader.loadProfileData();
                SynchroDataLoader.loadGroupsJoinedData();
                SynchroDataLoader.loadViewGroupData();
                //resync call
                JsonObject obj = SynchroAPI.getInstance().getMeResync();
                Toast.makeText(DrawerActivity.this, obj.get("message").toString(), Toast.LENGTH_LONG).show();
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
