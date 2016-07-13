package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;

public class GroupsJoinedFragment extends Fragment {

    private static ArrayList<GroupData> groupDatas = SynchroDataLoader.getGroupDatas();

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
        return inflater.inflate(R.layout.fragment_groups_joined, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (groupDatas.size() == 0) {
            LinearLayout noGroupsMessage = (LinearLayout) view.findViewById(R.id.noGroupsMessageLayout);
            noGroupsMessage.setVisibility(View.VISIBLE);
        }
        else {
            displayGroupsJoined(view);
        }
    }

    /*  setups cardviews for display of group details
        NOTE: user called is not current user, endpoint not configured yet
    */
    private void displayGroupsJoined(View rootView) {

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_groups_joined);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewGroupAdapter(groupDatas, getFragmentManager()));
    }

}
