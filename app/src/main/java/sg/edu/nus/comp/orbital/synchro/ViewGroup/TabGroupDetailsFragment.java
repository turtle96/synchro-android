package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * Tab fragment for view group display
 * Displays all group details
 */
public class TabGroupDetailsFragment extends Fragment{

    public TabGroupDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_group_details_tab, container, false);
        return rootView;
    }
}
