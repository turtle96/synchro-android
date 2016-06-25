package sg.edu.nus.comp.orbital.synchro.Recommend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupInfo;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;

/**
 * Created by angja_000 on 23/6/2016.
 */
public class TabRecommendGroupsFragment extends Fragment {

    private static JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();

    public TabRecommendGroupsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_groups_tab, container, false);

        //Toast.makeText(getContext(), "shows list of groups with 'm' in their name", Toast.LENGTH_LONG).show();

        ArrayList<GroupInfo> groups = GroupInfo.parseAndFilterGroupInfo(groupsJsonArray, "m");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recommend_groups);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewGroupAdapter(groups, null));

        return rootView;
    }

}
