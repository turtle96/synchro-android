package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_drawer);
        setSupportActionBar(toolbar);

        setupDrawer(toolbar);

        //means redirected from Login
        String caller = getIntent().getStringExtra("caller");
        if (caller!=null) {
            if (caller.equals("LoginActivity")) {
                SynchroAPI.authenticate(AuthToken.getToken());
                SynchroAPI.updateToken(AuthToken.getToken());

                ProgressDialog progressDialog = new ProgressDialog(DrawerActivity.this);
                AsyncTaskRunner.setProgressDialog(progressDialog);
                AsyncTaskRunner.loadInitialData(DrawerActivity.this);
            }
            //sets landing page to Groups Joined if redirected from SplashActivity
            else if (caller.equals("SplashActivity") && SynchroDataLoader.getGroupsJsonArray()!=null) {
                redirectGroupsJoined();
            }

        }

        handleIntent(getIntent());  //for search queries in search bar

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_bar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.requestFocusFromTouch();
        //cant get keyboard to show automatically, need to fix

        return true;
    }

    public void redirectGroupsJoined() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
        transaction.replace(R.id.content_fragment, GroupsJoinedFragment.newInstance());
        transaction.commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    //handle search query, redirects to SearchResultsFragment
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Bundle bundle = new Bundle();
            bundle.putString("searchQuery", query);
            SearchResultsFragment searchResultsFragment = SearchResultsFragment.newInstance();
            searchResultsFragment.setArguments(bundle);

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left,
                    R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right);
            transaction.replace(R.id.content_fragment, searchResultsFragment);
            transaction.commit();
        }
    }

    //drawer setup stuff
    public void setupDrawer(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                    case R.id.nav_login:
                        // do not add loginactivity into backstack
                        Intent loginActivity = new Intent(DrawerActivity.this, LoginActivity.class);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(loginActivity);
                    case R.id.nav_view_group:
                        transaction.replace(R.id.content_fragment, ViewGroupFragment.newInstance());
                        break;
                    case R.id.nav_search:
                        transaction.replace(R.id.content_fragment, SearchResultsFragment.newInstance());
                        break;
                    case R.id.nav_profile:
                        transaction.replace(R.id.content_fragment, ProfileFragment.newInstance());
                        break;
                    case R.id.nav_groups_joined:
                        transaction.replace(R.id.content_fragment, GroupsJoinedFragment.newInstance());
                        break;
                    case R.id.nav_new_group:
                        transaction.replace(R.id.content_fragment, CreateGroupFragment.newInstance());
                        break;
                    case R.id.nav_recommendations:
                        transaction.replace(R.id.content_fragment, RecommendationsFragment.newInstance());
                        break;
                }

                transaction.commit();

            }
        }, 300);

        return true;
    }

}
