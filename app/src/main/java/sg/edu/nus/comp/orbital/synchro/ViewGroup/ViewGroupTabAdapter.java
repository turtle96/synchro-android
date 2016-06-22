package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * PagerAdapter to manage tabs for ViewGroupPage
 */
public class ViewGroupTabAdapter extends FragmentStatePagerAdapter {

    private int numTabs = 2;

    public ViewGroupTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new TabGroupDetailsFragment();
            case 1:
                return new TabGroupMembersFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
