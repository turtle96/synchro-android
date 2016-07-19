package sg.edu.nus.comp.orbital.synchro;

import android.graphics.drawable.Drawable;
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

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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

        if (profile!=null && moduleLists!=null) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                displayAlternateLayout(rootView,
                        //ContextCompat.getDrawable(getContext(), R.drawable.emma_watson), null);
                        null, profile.getName());
            }
            else if (profile.getProfileImage()!=null && profile.getProfileImage() instanceof TextDrawable) {
                displayTextDrawable(rootView, profile);
            }
        }
        else {
            rootView = inflater.inflate(R.layout.error_layout, container, false);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (profile!=null && moduleLists!=null) {
            displayProfileInfo(view, profile);
            displayModulesTaken(view, moduleLists);
        }
    }

    public static void displayTextDrawable(View view, User user) {
        //textdrawables cannot be displayed on CircleImageView directly
        //so some layout formatting required
        CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.user_profile_photo);
        circleImageView.setVisibility(View.GONE);

        ImageView profileImage = (ImageView) view.findViewById(R.id.textDrawableView);
        profileImage.setImageDrawable(user.getProfileImage());
        profileImage.setVisibility(View.VISIBLE);
        view.findViewById(R.id.textDrawableBorder).setVisibility(View.VISIBLE);
    }

    //this provides alternate layout since layer effect cannot be achieved pre-Lollipop
    //image can be set with either resourceID or text drawable
    //todo once image upload is supported need to edit code
    public static void displayAlternateLayout(View view, Drawable imageDrawable, String name) {
        ImageView profileHeader = (ImageView) view.findViewById(R.id.header_cover_image);

        if (imageDrawable==null && name!=null) {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color = generator.getRandomColor();
            imageDrawable = TextDrawable.builder()
                    .buildRect(name.substring(0, 1), color);
        }

        profileHeader.setImageDrawable(imageDrawable);

        TextView profileName = (TextView) view.findViewById(R.id.user_profile_name);
        profileName.setPadding(20, 100, 20, 100);
        CircleImageView profileImage = (CircleImageView) view.findViewById(R.id.user_profile_photo);
        profileImage.setVisibility(View.GONE);
    }

    /*
        modules' code + name strings sorted by semester, then sorted by year into ModuleList objects
        displayed using cardview (this can help with option for user to hide some module info)
    */
    public static void displayModulesTaken(View rootView, ArrayList<ModuleList> moduleLists) {

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_profile);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(new CardViewModulesAdapter(moduleLists));
    }

    //displays name, faculty, major, matriculation year of user
    public static void displayProfileInfo(View rootView, User profile) {

        TextView name = (TextView) rootView.findViewById(R.id.user_profile_name);
        TextView faculty = (TextView) rootView.findViewById(R.id.valueFaculty);
        TextView firstMajor = (TextView) rootView.findViewById(R.id.valueFirstMajor);
        TextView secondMajor = (TextView) rootView.findViewById(R.id.valueSecondMajor);
        TextView year = (TextView) rootView.findViewById(R.id.valueMatriculationYear);

        //use this code if you want to display your real name instead of placeholder
        name.setText(profile.getName());
        //name.setText("Hermione Granger");

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
