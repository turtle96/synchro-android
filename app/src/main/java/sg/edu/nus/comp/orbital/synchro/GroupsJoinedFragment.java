package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.Group;

public class GroupsJoinedFragment extends Fragment {

    //dummy user called, check AsyncTaskRunner
    private static ArrayList<Group> groups = SynchroDataLoader.getGroups();

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

    /*  setups cardviews for display of group details
        NOTE: user called is not current user, endpoint not configured yet
    */
    private void displayGroupsJoined(View rootView) {

        // initialize fragment manager
        FragmentManager manager = getFragmentManager();

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_groups_joined);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewGroupAdapter(groups, manager, null));
    }

}
