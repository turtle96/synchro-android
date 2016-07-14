package sg.edu.nus.comp.orbital.synchro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewUserAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

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
        return inflater.inflate(R.layout.fragment_search_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchView searchView = (SearchView) view.findViewById(R.id.searchViewInFragment);

        //prevents shutdown if accessed from drawer menu
        if (getArguments() != null) {
            String query = getArguments().getString("searchQuery");
            searchView.setQuery(query, false);
        }

        //display results according to filters: users or groups
        //note: buttons are mutually exclusive
        final ToggleButton buttonUsers = (ToggleButton) view.findViewById(R.id.buttonUsers);
        final ToggleButton buttonGroups = (ToggleButton) view.findViewById(R.id.buttonGroups);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_search_results);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        buttonUsers.setChecked(true);
        displayUsers(recyclerView);

        //toggle search users only
        buttonUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonGroups.setChecked(false);     //only toggle one button at a time
                    displayUsers(recyclerView);
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
                    displayGroups(recyclerView);
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
    private CardViewUserAdapter displayUsers(RecyclerView recyclerView) {
        JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();

        ArrayList<User> users = User.parseUsers(usersJsonArray);
        CardViewUserAdapter userAdapter = new CardViewUserAdapter(users, getFragmentManager());
        recyclerView.setAdapter(userAdapter);

        return userAdapter;
    }

    //sets adapter of RecyclerView to display groups info
    //returns the CardViewGroupAdapter created
    private CardViewGroupAdapter displayGroups(RecyclerView recyclerView) {
        JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();
        ArrayList<GroupData> groupDatas = GroupData.parseGroups(groupsJsonArray);

        CardViewGroupAdapter groupAdapter = new CardViewGroupAdapter(groupDatas, getFragmentManager());
        recyclerView.setAdapter(groupAdapter);

        return groupAdapter;
    }

}
