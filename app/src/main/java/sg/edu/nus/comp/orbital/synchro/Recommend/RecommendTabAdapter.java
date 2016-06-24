package sg.edu.nus.comp.orbital.synchro.Recommend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by angja_000 on 23/6/2016.
 */
public class RecommendTabAdapter extends FragmentStatePagerAdapter {

    private int numTabs = 2;

    public RecommendTabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new TabRecommendUsersFragment();
            case 1:
                return new TabRecommendGroupsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
