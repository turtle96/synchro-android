package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

        JsonObject profile = SynchroAPI.getInstance().getMe(getContext());

        TextView faculty = (TextView) rootView.findViewById(R.id.valueFaculty);
        TextView firstMajor = (TextView) rootView.findViewById(R.id.valueFirstMajor);
        TextView year = (TextView) rootView.findViewById(R.id.valueMatriculationYear);

        faculty.append(profile.get("faculty").toString().replaceAll("\"", ""));
        firstMajor.append(profile.get("first_major").toString().replaceAll("\"", ""));
        year.append(profile.get("matriculation_year").toString().replaceAll("\"", ""));


        //modules display
        //need to consider year and sem categorization
        JsonArray modules = SynchroAPI.getInstance().getMeModules(getContext());
        String modulesString = "";

        for (int i=0; i<modules.size(); i++) {
            JsonObject object = modules.get(i).getAsJsonObject();
            object = object.get("module").getAsJsonObject();
            //System.out.println("HERE " + i + " " + object.toString());
            modulesString = modulesString + object.get("module_code").toString().replaceAll("\"", "")
                    + ": " + object.get("module_title").toString().replaceAll("\"", "") + "\n";
        }

        //Toast.makeText(getContext(), modulesString, Toast.LENGTH_LONG).show();
        TextView modulesTaken = (TextView) rootView.findViewById(R.id.valueModulesTaken);
        modulesTaken.append(modulesString);

        return rootView;
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
