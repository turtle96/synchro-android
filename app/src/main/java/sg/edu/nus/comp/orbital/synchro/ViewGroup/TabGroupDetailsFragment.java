package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * Tab fragment for view groupData display
 * Displays all groupData details
 */
public class TabGroupDetailsFragment extends Fragment{

    private GroupData groupData;

    public TabGroupDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_group_details_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroupFragment viewGroupFragment = (ViewGroupFragment) getParentFragment();
        groupData = viewGroupFragment.getGroupData();

        TextView groupType = (TextView) view.findViewById(R.id.valueGroupType);
        TextView groupDesc = (TextView) view.findViewById(R.id.valueDescription);
        TextView date = (TextView) view.findViewById(R.id.valueDate);
        TextView time = (TextView) view.findViewById(R.id.valueTime);
        TextView venue = (TextView) view.findViewById(R.id.valueVenue);

        if (groupData != null) {
            groupType.setText(groupData.getType());
            groupDesc.setText(groupData.getDescription());
            date.setText(groupData.getDate());
            time.setText(groupData.getTime());
            venue.setText(groupData.getVenue());
        }
        else {
            groupType.setText("e.g. Study/Project/Games");
            groupDesc.setText(getContext().getResources().getString(R.string.small_text));
            date.setText("31st July 2078");
            time.setText("3pm");
            venue.setText("Room 666");
        }
    }
}
