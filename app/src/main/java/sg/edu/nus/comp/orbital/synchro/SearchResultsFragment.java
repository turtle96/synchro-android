package sg.edu.nus.comp.orbital.synchro;


import android.os.Bundle;
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

import sg.edu.nus.comp.orbital.synchro.GroupsJoined.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.GroupsJoined.GroupInfo;
import sg.edu.nus.comp.orbital.synchro.ViewGroup.CardViewUserAdapter;

public class SearchResultsFragment extends Fragment {

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance() {
        SearchResultsFragment fragment = new SearchResultsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchViewInFragment);

        //prevents shutdown if accessed from drawer menu
        if (getArguments() != null) {
            String query = getArguments().getString("searchQuery");
            searchView.setQuery(query, false);
            searchView.clearFocus();    //not clearing focus =_= need to fix
        }

        //display results according to filters
        final ToggleButton buttonUsers = (ToggleButton) rootView.findViewById(R.id.buttonUsers);
        final ToggleButton buttonGroups = (ToggleButton) rootView.findViewById(R.id.buttonGroups);
        final ToggleButton buttonBoth = (ToggleButton) rootView.findViewById(R.id.buttonBoth);

        final JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();
        final JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();

        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_search_results);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final CardViewUserAdapter[] memberAdapters = new CardViewUserAdapter[1];
        final CardViewGroupAdapter[] groupAdapter = new CardViewGroupAdapter[1];

        //toggle search users only
        buttonUsers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    // The toggle is enabled
                    buttonGroups.setChecked(false);     //only toggle one button at a time
                    buttonBoth.setChecked(false);

                    ArrayList<String> users = new ArrayList<>();

                    //last 2 items are the programmers' names lol (excluded)
                    for (int i=0; i<15; i++) {
                        JsonObject object = usersJsonArray.get(i).getAsJsonObject();
                        users.add(object.get("name").toString().replaceAll("\"", ""));
                    }

                    memberAdapters[0] = new CardViewUserAdapter(users);
                    recyclerView.setAdapter(memberAdapters[0]);

                } else {
                    // The toggle is disabled
                    if (memberAdapters[0] != null) {
                        memberAdapters[0].clearView();
                    }

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
                    buttonBoth.setChecked(false);

                    ArrayList<GroupInfo> groups = GroupInfo.parseGroupInfo(groupsJsonArray);

                    groupAdapter[0] = new CardViewGroupAdapter(groups, null);
                    recyclerView.setAdapter(groupAdapter[0]);

                } else {
                    // The toggle is disabled
                    if (groupAdapter[0] != null) {
                        groupAdapter[0].clearView();
                    }
                }
            }
        });

        //toggle search both
        buttonBoth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    buttonUsers.setChecked(false);
                    buttonGroups.setChecked(false);

                } else {
                    // The toggle is disabled

                }
            }
        });

        return rootView;
    }

}