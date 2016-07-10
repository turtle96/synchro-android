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

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewGroupAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;


public class RecommendationsFragment extends Fragment {

    private static JsonArray groupsJsonArray = SynchroAPI.getInstance().getAllGroups();

    public RecommendationsFragment() {
        // Required empty public constructor
    }

    public static RecommendationsFragment newInstance() {
        RecommendationsFragment fragment = new RecommendationsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recommendations, container, false);

        ArrayList<GroupData> groupDatas = GroupData.parseAndFilterGroups(groupsJsonArray, "m");

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recommend_groups);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewGroupAdapter(groupDatas, null, null));

        return rootView;
    }


}
