package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.DataHolders.Group;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * Tab fragment for view group display
 * Displays all group details
 */
public class TabGroupDetailsFragment extends Fragment{

    private static Group group;

    public TabGroupDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_group_details_tab, container, false);

        group = ViewGroupFragment.getGroup();

        TextView groupType = (TextView) rootView.findViewById(R.id.valueGroupType);
        TextView groupDesc = (TextView) rootView.findViewById(R.id.valueDescription);
        TextView date = (TextView) rootView.findViewById(R.id.valueDate);
        TextView time = (TextView) rootView.findViewById(R.id.valueTime);
        TextView venue = (TextView) rootView.findViewById(R.id.valueVenue);

        if (group != null) {
            groupType.setText(group.getType());
            groupDesc.setText(group.getDescription());
            date.setText(group.getDate());
            time.setText(group.getTime());
            venue.setText(group.getVenue());
        }
        else {
            groupType.setText("e.g. Study/Project/Games");
            groupDesc.setText(getContext().getResources().getString(R.string.small_text));
            date.setText("31st July 2078");
            time.setText("3pm");
            venue.setText("Room 666");
        }

        return rootView;
    }
}
