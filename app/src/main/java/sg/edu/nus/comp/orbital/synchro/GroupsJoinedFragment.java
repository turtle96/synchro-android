package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.GroupsJoined.CardViewGroupAdapter;

public class GroupsJoinedFragment extends Fragment {

    //dummy user called
    private static JsonArray groupsJsonArray = SynchroDataLoader.getGroupsJsonArray();

    public GroupsJoinedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static GroupsJoinedFragment newInstance() {
        GroupsJoinedFragment fragment = new GroupsJoinedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_groups_joined, container, false);

        displayGroupsJoined(rootView);

        return rootView;
    }

    /*
        calls from server
        sorts names from JsonArray for display via cardview
        NOTE: user called is not current user, endpoint not configured yet
    */
    private void displayGroupsJoined(View rootView) {

        ArrayList<String> groupDetails = new ArrayList<String>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            groupDetails.add(object.get("name").toString().replaceAll("\"", ""));
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_groups_joined);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewGroupAdapter(groupDetails, getContext()));
    }

}
