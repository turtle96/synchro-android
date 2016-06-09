package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class GroupsJoinedFragment extends Fragment {
    public GroupsJoinedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupsJoinedFragment newInstance() {
        GroupsJoinedFragment fragment = new GroupsJoinedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_groups_joined, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<String> groupDetails = new ArrayList<String>();

        //placeholder
        groupDetails.add("Turtle");
        groupDetails.add("Water");
        groupDetails.add("Sun");
        groupDetails.add("Moon");
        groupDetails.add("Turtle");
        groupDetails.add("Water");
        groupDetails.add("Sun");
        groupDetails.add("Moon");


        recyclerView.setAdapter(new CardViewAdapter(groupDetails, getContext()));


        return rootView;
    }

}
