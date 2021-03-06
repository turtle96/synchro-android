package sg.edu.nus.comp.orbital.synchro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewUserAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

public class SearchResultsFragment extends Fragment {
    private static final String GET_SEARCH_QUERY = "Search Query";
    private String query;
    private static SearchView searchView;

    private static JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();
    private static ArrayList<User> users = User.parseUsers(usersJsonArray);
    private static JsonArray groupsJsonArray;
    private static ArrayList<GroupData> groupDatas;

    private static ToggleButton buttonUsers;
    private static ToggleButton buttonGroups;
    private static RecyclerView recyclerView;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            //this ensures if there's multiple searches, back button shows correct result
            if (query == null) {
                query = getArguments().getString(GET_SEARCH_QUERY);
            }

            return inflater.inflate(R.layout.fragment_search_results, container, false);
        }
        else {
            return inflater.inflate(R.layout.error_layout, container, false);
        }

    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView = (SearchView) view.findViewById(R.id.searchViewInFragment);
        searchView.setQuery(query, false);
        searchView.requestFocusFromTouch();
        searchView.clearFocus();

        //todo buttons not visible in app demo, go to fragment layout to set cardview visibility to get buttons back

        buttonUsers = (ToggleButton) view.findViewById(R.id.buttonUsers);
        buttonGroups = (ToggleButton) view.findViewById(R.id.buttonGroups);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search_results);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String q) {
                query = q;
                searchView.clearFocus();
                search(view);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search(view);
    }

    //clears searchview on bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_search_results, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_view_all_groups) {
            searchView.setQuery("%groups", true);
            searchView.clearFocus();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search(final View view) {
        //display results according to filters: users or groups
        //note: buttons are mutually exclusive
        buttonGroups.setChecked(true);
        displayGroups(view, recyclerView, query);

        //toggle search users only
        buttonUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonGroups.setChecked(false);     //only toggle one button at a time
                    displayUsers(recyclerView, query);
                }
                else if (!buttonGroups.isChecked()) {
                    // The toggle is disabled
                    buttonUsers.setChecked(true);
                }
            }
        });

        //toggle search groups only
        buttonGroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonUsers.setChecked(false);
                    displayGroups(view, recyclerView, query);
                }
                else if (!buttonUsers.isChecked()) {
                    // The toggle is disabled
                    buttonGroups.setChecked(true);
                }
            }
        });
    }

    //sets adapter of RecyclerView to display users info
    //returns the CardViewUserAdapter created
    private void displayUsers(RecyclerView recyclerView, String query) {
        ArrayList<User> arrayToDisplay;

        query = "%users";   //todo remove when users search is up

        if (query.equalsIgnoreCase("%users")) {
            arrayToDisplay = users;
        }
        else {
            JsonArray result = SynchroAPI.getInstance().getSearchGroups(query);
            arrayToDisplay = User.parseUsers(result);
        }

        CardViewUserAdapter userAdapter = new CardViewUserAdapter(arrayToDisplay, getFragmentManager());
        recyclerView.setAdapter(userAdapter);
    }

    //sets adapter of RecyclerView to display groups info
    //returns the CardViewGroupAdapter created
    private void displayGroups(View view, RecyclerView recyclerView, String query) {
        ArrayList<GroupData> arrayToDisplay;

        if (query.equalsIgnoreCase("%groups")) {
            groupsJsonArray = SynchroAPI.getInstance().getAllGroups();
            groupDatas = GroupData.parseGroups(groupsJsonArray);
            arrayToDisplay = groupDatas;
        }
        else {
            JsonArray result = SynchroAPI.getInstance().getSearchGroups(query);
            arrayToDisplay = GroupData.parseGroups(result);
        }

        if (arrayToDisplay!=null && arrayToDisplay.size() == 0) {
            view.findViewById(R.id.no_results_layout).setVisibility(View.VISIBLE);
        }
        else {
            view.findViewById(R.id.no_results_layout).setVisibility(View.GONE);
        }

        CardViewGroupAdapter groupAdapter = new CardViewGroupAdapter(arrayToDisplay, getFragmentManager());
        recyclerView.setAdapter(groupAdapter);

    }

}
