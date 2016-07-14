package sg.edu.nus.comp.orbital.synchro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import sg.edu.nus.comp.orbital.synchro.CardViewAdapters.CardViewModulesAdapter;
import sg.edu.nus.comp.orbital.synchro.DataHolders.ModuleList;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

/**
 * Created by angja_000 on 14/7/2016.
 *
 * NOTE: this is to display other users' details, not the logged in user (refer to profile fragment)
 * uses the same xml layout as profile
 */
public class ViewUserFragment extends Fragment {

    private static final String GET_USER_ID = "User Id";
    private String userId;
    private User user;
    private ArrayList<ModuleList> moduleLists;

    public ViewUserFragment() {}

    public static ViewUserFragment newInstance() {
        ViewUserFragment fragment = new ViewUserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(GET_USER_ID);
            JsonObject userJson = SynchroAPI.getInstance().getUserById(userId);
            user = User.parseSingleUser(userJson);
            JsonArray modulesJsonArray = SynchroAPI.getInstance().getModulesByUserId(userId);
            moduleLists = ModuleList.parseModules(modulesJsonArray);

            ProfileFragment.displayProfileInfo(view, user);
            ProfileFragment.displayModulesTaken(view, moduleLists);

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                ProfileFragment.displayAlternateLayout(view, 0, user.getProfileImage());
            }
            else if (user.getProfileImage() != null) {
                CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.user_profile_photo);
                circleImageView.setVisibility(View.GONE);
                ImageView profileImage = (ImageView) view.findViewById(R.id.textDrawableView);
                profileImage.setImageDrawable(user.getProfileImage());
                profileImage.setVisibility(View.VISIBLE);
            }
        }
    }

}
