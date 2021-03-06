package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.AsyncTasks.AsyncTaskDeleteGroup;
import sg.edu.nus.comp.orbital.synchro.AsyncTasks.AsyncTaskJoinGroup;
import sg.edu.nus.comp.orbital.synchro.AsyncTasks.AsyncTaskLeaveGroup;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

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

            if (!groupData.isAdmin()) {
                setHasOptionsMenu(false);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (groupData==null || members==null) {
            return;
        }

        //System.out.println(groupData.getName() + " " + groupData.isAdmin());

        final TextView groupName = (TextView) view.findViewById(R.id.labelGroupName);
        groupName.setText(groupData.getName());

        final FloatingActionButton fabJoinGroup = (FloatingActionButton) view.findViewById(R.id.fab_join_group);
        final FloatingActionButton fabGoToPosts = (FloatingActionButton) view.findViewById(R.id.fab_goto_posts);

        fabGoToPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out);

                ViewPostsFragment viewPostsFragment = ViewPostsFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString(GET_GROUP_ID, groupId);
                viewPostsFragment.setArguments(bundle);

                transaction.replace(R.id.content_fragment, viewPostsFragment);
                transaction.commit();
            }
        });

        fabJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskJoinGroup.load(new ProgressDialog(getContext()), groupId, getFragmentManager());
            }
        });

        if (groupData.isAdmin()) {
            fabGoToPosts.setVisibility(View.VISIBLE);
        }
        else {
            fabJoinGroup.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_view_group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_leave_group) {
            leaveGroup();
            return true;
        }
        else if (id == R.id.action_edit_group) {
            EditGroupFragment editGroupFragment = EditGroupFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(GET_GROUP_ID, groupId);
            editGroupFragment.setArguments(bundle);

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.content_fragment, editGroupFragment);
            transaction.commit();
            return true;
        }
        else if (id == R.id.action_delete_group) {
            deleteGroup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void leaveGroup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        alertDialog.setTitle("Leave Group");
        alertDialog.setMessage("Are you sure you want to leave?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTaskLeaveGroup.load(new ProgressDialog(getContext()), groupId, getFragmentManager());
            }
        });
        alertDialog.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void deleteGroup() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);
        alertDialog.setTitle("Delete Group");
        alertDialog.setMessage("Are you sure you want to delete this group?");

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncTaskDeleteGroup.load(new ProgressDialog(getContext()), groupId, getFragmentManager());
            }
        });
        alertDialog.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    //to be called after AsyncTask for leaving group loaded
    public static void redirectAfterLeaveGroup(boolean result, FragmentManager manager) {
        if (result) {
            Toast.makeText(App.getContext(), "Sayonara~", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.content_fragment, GroupsJoinedFragment.newInstance());
            transaction.commit();
        }
        else {
            Toast.makeText(App.getContext(), "Error leaving group", Toast.LENGTH_SHORT).show();
        }
    }

    public static void redirectAfterDeleteGroup(boolean result, FragmentManager manager) {
        if (result) {
            Toast.makeText(App.getContext(), "Sayonara~", Toast.LENGTH_SHORT).show();

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.content_fragment, GroupsJoinedFragment.newInstance());
            transaction.commit();
        }
        else {
            Toast.makeText(App.getContext(), "Error deleting group", Toast.LENGTH_SHORT).show();
        }
    }

    //to be called after AsyncTask for joining group loaded
    public static void redirectAfterJoinGroup(boolean result, String groupId, FragmentManager manager) {
        if (result) {
            Toast.makeText(App.getContext(), "Joined group", Toast.LENGTH_SHORT).show();

            ViewGroupFragment viewGroupFragment = ViewGroupFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString(GET_GROUP_ID, groupId);
            viewGroupFragment.setArguments(bundle);

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.content_fragment, viewGroupFragment);
            transaction.commit();
        }
        else {
            Toast.makeText(App.getContext(), "Error joining group", Toast.LENGTH_SHORT).show();
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
