package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.Profile.CardViewModulesAdaptor;
import sg.edu.nus.comp.orbital.synchro.Profile.ModuleList;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        try {
            // prepare self signed ssl crt
            SynchroAPI api = SynchroAPI.getInstance();

            Log.d("Synchro", api.getMe(getContext()).toString());

        } catch (Exception ex) {
            Log.d("Synchro", "Something horrible went wrong.");
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        displayProfileInfo(rootView);
        displayModulesTaken(rootView);

        return rootView;
    }

    /*  calls from server & displays a list of modules taken by user
        processes JsonArray returned from server
        modules' code + name strings sorted by semester, then sorted by year into ModuleList objects
        displayed using cardview (this can help with option for user to hide some module info)
    */
    private void displayModulesTaken(View rootView) {
        JsonArray modulesJsonArray = SynchroAPI.getInstance().getMeModules(getContext());

        ArrayList<ModuleList> moduleLists = new ArrayList<>();
        int yearCounter = 0;    //index counter for modulesByYear
        String yearTracker = modulesJsonArray.get(0).getAsJsonObject().get("year_taken").toString();
        String semTracker = modulesJsonArray.get(0).getAsJsonObject().get("semester_taken").toString();
        moduleLists.add(new ModuleList(yearTracker));   //initialize

        for (int i=0; i<modulesJsonArray.size(); i++) {
            JsonObject object = modulesJsonArray.get(i).getAsJsonObject();

            if (!object.get("year_taken").toString().equals(yearTracker)) {
                yearTracker = object.get("year_taken").toString();
                yearCounter++;
                moduleLists.add(new ModuleList(yearTracker));   //initialize
            }
            if (!object.get("semester_taken").toString().equals(semTracker)) {
                semTracker = object.get("semester_taken").toString();
            }

            JsonObject moduleObj = object.getAsJsonObject("module");

            //sort info into correct sem
            //each sem string contains the entire module list
            if (semTracker.replaceAll("\"", "").equals("1")) {
                moduleLists.get(yearCounter).addToListSem1(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }
            else {
                moduleLists.get(yearCounter).addToListSem2(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }

        }

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_profile);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewModulesAdaptor(moduleLists, getContext()));
    }

    //calls from server & displays faculty, major, matriculation of user
    private void displayProfileInfo(View rootView) {
        JsonObject profile = SynchroAPI.getInstance().getMe(getContext());

        TextView faculty = (TextView) rootView.findViewById(R.id.valueFaculty);
        TextView firstMajor = (TextView) rootView.findViewById(R.id.valueFirstMajor);
        TextView year = (TextView) rootView.findViewById(R.id.valueMatriculationYear);

        faculty.append(profile.get("faculty").toString().replaceAll("\"", ""));
        firstMajor.append(profile.get("first_major").toString().replaceAll("\"", ""));
        year.append(profile.get("matriculation_year").toString().replaceAll("\"", ""));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        getActivity().getMenuInflater().inflate(R.menu.menu_profile_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
