package sg.edu.nus.comp.orbital.synchro;

import android.graphics.PorterDuff;
import android.os.Bundle;
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

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.ViewGroup.ViewGroupTabAdapter;

/**
 * Created by angja_000 on 12/6/2016.
 */
public class ViewGroupFragment extends Fragment {

    private static final String GET_GROUP_KEY = "GroupData Object";
    private static JsonArray membersJsonArray = SynchroDataLoader.loadViewGroupData(5);
    private static GroupData groupData;

    public ViewGroupFragment() {}

    public static ViewGroupFragment newInstance() {
        ViewGroupFragment fragment = new ViewGroupFragment();
        return fragment;
    }

    //getter method so that TabGroupMembersFragment can access
    //data is loaded upon ViewGroupFragment instantiation so cannot be immediately stored in child fragment
    public static JsonArray getMembersJsonArray() {return membersJsonArray;}
    public static GroupData getGroupData() {return groupData;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_group, container, false);
        setupTabs(rootView);

        if (getArguments() != null) {
            groupData = (GroupData) getArguments().getSerializable(GET_GROUP_KEY);
        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_join_group);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "joined group (theoretically)", Toast.LENGTH_LONG).show();
            }
        });

        TextView groupName = (TextView) rootView.findViewById(R.id.labelGroupName);

        if (groupData != null) {
            groupName.setText(groupData.getName());
        }
        else {
            groupName.setText("Study Group CS1010");
        }

        return rootView;
    }

    //setup tab layouts and child fragments: GroupDetails and GroupMembers
    private void setupTabs(View rootView) {
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout_view_group);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_info_outline_black_48dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group_black_48dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewGroupPager);
        ViewGroupTabAdapter adaptor = new ViewGroupTabAdapter(getChildFragmentManager());
        viewPager.setAdapter(adaptor);
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
