package sg.edu.nus.comp.orbital.synchro;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;
import sg.edu.nus.comp.orbital.synchro.ViewGroup.ViewGroupTabAdapter;

/**
 * Created by angja_000 on 12/6/2016.
 */
public class ViewGroupFragment extends Fragment {

    private static final String GET_GROUP_ID = "Group Id";
    private String groupId;
    private ArrayList<User> members;
    private GroupData groupData;

    public ViewGroupFragment() {}

    public static ViewGroupFragment newInstance() {
        ViewGroupFragment fragment = new ViewGroupFragment();
        return fragment;
    }

    public GroupData getGroupData() {return groupData;}
    public ArrayList<User> getMembers() {return members;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        JsonArray membersJsonArray = null;
        JsonObject groupJson = null;

        if (getArguments() != null) {
            groupId = getArguments().getString(GET_GROUP_ID);
            groupJson = SynchroAPI.getInstance().getGroupById(groupId);
            membersJsonArray = SynchroAPI.getInstance().getUsersByGroupId(groupId);
        }

        if (groupJson==null || membersJsonArray==null) {
            rootView = inflater.inflate(R.layout.error_layout, container, false);
        }
        else {
            groupData = GroupData.parseSingleGroup(groupJson);
            members = User.parseUsers(membersJsonArray);
            setupTabs(rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (groupData!=null && members!=null) {

            System.out.println(groupData.getName() + " " + groupData.isAdmin());

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_join_group);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "joined group (theoretically)", Toast.LENGTH_LONG).show();
                }
            });

            TextView groupName = (TextView) view.findViewById(R.id.labelGroupName);
            groupName.setText(groupData.getName());
        }

    }

    //setup tab layouts and child fragments: GroupDetails and GroupMembers
    private void setupTabs(View rootView) {
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout_view_group);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_info_outline_black_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group_black_48dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewGroupPager);
        ViewGroupTabAdapter adapter = new ViewGroupTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.background_light_grey));
        tabLayout.setSelectedTabIndicatorHeight(10);
        tabLayout.getTabAt(0).getIcon().setColorFilter(ContextCompat.getColor(getContext(),
                R.color.background_light_grey), PorterDuff.Mode.SRC_IN);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(getContext(), R.color.background_light_grey),
                        PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().clearColorFilter();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
