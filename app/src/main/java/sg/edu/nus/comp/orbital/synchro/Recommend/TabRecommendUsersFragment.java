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
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.SynchroAPI;
import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewUserAdapter;

/**
 * Created by angja_000 on 23/6/2016.
 */
public class TabRecommendUsersFragment extends Fragment {

    private static JsonArray usersJsonArray = SynchroAPI.getInstance().getAllUsers();

    public TabRecommendUsersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recommend_users_tab, container, false);

        Toast.makeText(getContext(), "shows list of users with 'a' in their name", Toast.LENGTH_LONG).show();

        ArrayList<String> users = new ArrayList<>();

        //last 2 items are the programmers' names lol (excluded)
        for (int i=0; i<15; i++) {
            JsonObject object = usersJsonArray.get(i).getAsJsonObject();

            if (object.get("name").toString().contains("a")) {
                users.add(object.get("name").toString().replaceAll("\"", ""));
            }
        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_recommend_users);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewUserAdapter(users));

        return rootView;
    }
}
