package sg.edu.nus.comp.orbital.synchro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private static String query;

    private static JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();
    private static ArrayList<User> users = User.parseUsers(usersJsonArray);
    private static JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();
    private static ArrayList<GroupData> groupDatas = GroupData.parseGroups(groupsJsonArray);

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = (SearchView) view.findViewById(R.id.searchViewInFragment);
        searchView.setQuery(query, false);

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
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search();
    }

    //clears searchview on bar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void search() {
        //display results according to filters: users or groups
        //note: buttons are mutually exclusive
        buttonGroups.setChecked(true);
        displayGroups(recyclerView, query);

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

                    //todo fix search results views
                    displayGroups(recyclerView, query);
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
    private CardViewUserAdapter displayUsers(RecyclerView recyclerView, String query) {
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

        return userAdapter;
    }

    //sets adapter of RecyclerView to display groups info
    //returns the CardViewGroupAdapter created
    private CardViewGroupAdapter displayGroups(RecyclerView recyclerView, String query) {
        ArrayList<GroupData> arrayToDisplay;

        if (query.equalsIgnoreCase("%groups")) {
            arrayToDisplay = groupDatas;
        }
        else {
            JsonArray result = SynchroAPI.getInstance().getSearchGroups(query);
            arrayToDisplay = GroupData.parseGroups(result);
        }

        CardViewGroupAdapter groupAdapter = new CardViewGroupAdapter(arrayToDisplay, getFragmentManager());
        recyclerView.setAdapter(groupAdapter);

        return groupAdapter;
    }

}
