package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.Profile.CardViewModulesAdaptor;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;
import sg.edu.nus.comp.orbital.synchro.SynchroDataLoader;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * Tab fragment for view group display
 */
public class TabGroupMembersFragment extends Fragment {

    public TabGroupMembersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_group_members_tab, container, false);

        JsonArray membersJsonArray = ViewGroupFragment.getMembersJsonArray();

        ArrayList<String> members = new ArrayList<>();

        for (int i=0; i<membersJsonArray.size(); i++) {
            JsonObject object = membersJsonArray.get(i).getAsJsonObject();
            members.add(object.get("name").toString().replaceAll("\"", ""));
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_group_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewMemberAdaptor(members, getContext()));

        return rootView;
    }

}
