package sg.edu.nus.comp.orbital.synchro;


import android.app.Service;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewUserAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupInfo;

public class SearchResultsFragment extends Fragment {

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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewInFragment);

        //prevents shutdown if accessed from drawer menu
        if (getArguments() != null) {
            String query = getArguments().getString("searchQuery");
            searchView.setQuery(query, false);
        }

        //display results according to filters
        final ToggleButton buttonUsers = (ToggleButton) rootView.findViewById(R.id.buttonUsers);
        final ToggleButton buttonGroups = (ToggleButton) rootView.findViewById(R.id.buttonGroups);

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_search_results);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        buttonUsers.setChecked(true);
        displayUsers(recyclerView);

        //Todo: test clearViews()

        //toggle search users only
        buttonUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CardViewUserAdapter userAdapter = null;

                if (isChecked) {
                    // The toggle is enabled
                    buttonGroups.setChecked(false);     //only toggle one button at a time
                    userAdapter = displayUsers(recyclerView);
                }
                else {
                    // The toggle is disabled
                    clearUsers(userAdapter);    //doesnt seem to be working?
                }
            }
        });

        //toggle search groups only
        buttonGroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CardViewGroupAdapter groupAdapter = null;

                if (isChecked) {
                    // The toggle is enabled
                    buttonUsers.setChecked(false);
                    groupAdapter = displayGroups(recyclerView);
                }
                else {
                    // The toggle is disabled
                    clearGroups(groupAdapter);
                }
            }
        });

        return rootView;
    }

    //sets adapter of RecyclerView to display users info
    //returns the CardViewUserAdapter created so it can be cleared
    private CardViewUserAdapter displayUsers(RecyclerView recyclerView) {
        JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();
        ArrayList<String> users = new ArrayList<>();

        //last 2 items are the programmers' names lol (excluded)
        for (int i=0; i<15; i++) {
            JsonObject object = usersJsonArray.get(i).getAsJsonObject();
            users.add(object.get("name").toString().replaceAll("\"", ""));
        }
        CardViewUserAdapter userAdapter = new CardViewUserAdapter(users);
        recyclerView.setAdapter(userAdapter);

        return userAdapter;
    }

    //given CardViewUserAdapter, clears the view
    private void clearUsers(CardViewUserAdapter userAdapter) {
        if (userAdapter != null) {
            userAdapter.clearView();
        }
    }

    //sets adapter of RecyclerView to display groups info
    //returns the CardViewGroupAdapter created so it can be cleared
    private CardViewGroupAdapter displayGroups(RecyclerView recyclerView) {
        JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();
        ArrayList<GroupInfo> groups = GroupInfo.parseGroupInfo(groupsJsonArray);

        CardViewGroupAdapter groupAdapter = new CardViewGroupAdapter(groups, null);
        recyclerView.setAdapter(groupAdapter);

        return groupAdapter;
    }

    //given CardViewGroupAdapter, clears the view
    private void clearGroups(CardViewGroupAdapter groupAdapter) {
        if (groupAdapter != null) {
            groupAdapter.clearView();
        }
    }

}
