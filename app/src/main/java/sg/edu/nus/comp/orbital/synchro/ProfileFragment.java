package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewModulesAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.ModuleList;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

public class ProfileFragment extends Fragment {

    private static User profile = SynchroDataLoader.getUserProfile();
    private static ArrayList<ModuleList> moduleLists = SynchroDataLoader.getModuleLists();

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
        /*
        try {
            // prepare self signed ssl crt
            SynchroAPI api = SynchroAPI.getInstance();

            Log.d("Synchro", api.getMe().toString());

        } catch (Exception ex) {
            Log.d("Synchro", "Something horrible went wrong.");
            ex.printStackTrace();
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        //this provides alternate layout since layer effect cannot be achieved pre-Lollipop
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            ImageView profileHeader = (ImageView) rootView.findViewById(R.id.header_cover_image);
            profileHeader.setImageResource(R.drawable.profile);
            TextView profileName = (TextView) rootView.findViewById(R.id.user_profile_name);
            profileName.setPadding(5, 80, 5, 80);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayProfileInfo(view);
        displayModulesTaken(view);
    }

    /*
        modules' code + name strings sorted by semester, then sorted by year into ModuleList objects
        displayed using cardview (this can help with option for user to hide some module info)
    */
    private void displayModulesTaken(View rootView) {

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_profile);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewModulesAdapter(moduleLists));
    }

    //displays naem, faculty, major, matriculation year of user
    private void displayProfileInfo(View rootView) {

        TextView name = (TextView) rootView.findViewById(R.id.user_profile_name);
        TextView faculty = (TextView) rootView.findViewById(R.id.valueFaculty);
        TextView firstMajor = (TextView) rootView.findViewById(R.id.valueFirstMajor);
        TextView secondMajor = (TextView) rootView.findViewById(R.id.valueSecondMajor);
        TextView year = (TextView) rootView.findViewById(R.id.valueMatriculationYear);

        //use this code if you want to display your real name instead of placeholder
        //name.setText(profile.getName());
        name.setText("Hermione Granger");

        faculty.setText(profile.getFaculty());

        if (profile.hasFirstMajor()) {
            firstMajor.setText(profile.getFirstMajor());
        }
        else {
            rootView.findViewById(R.id.labelFirstMajor).setVisibility(View.GONE);
            firstMajor.setVisibility(View.GONE);
        }
        if (profile.hasSecondMajor()) {
            secondMajor.setText(profile.getSecondMajor());
        }
        else {
            rootView.findViewById(R.id.labelSecondMajor).setVisibility(View.GONE);
            secondMajor.setVisibility(View.GONE);
        }

        year.setText(profile.getYear());
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
