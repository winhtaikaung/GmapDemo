package adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import ui.fragment.Fragment_history;
import ui.fragment.Fragment_tracker;

/**
 * Created by winhtaikaung on 2/14/16.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    String[] mtitles;
    FragmentManager fragManager;
    FragmentTransaction ft;
    public TabsPagerAdapter(FragmentManager fm,String[] titles) {
        super(fm);
        this.fragManager=fm;
        this.ft=fragManager.beginTransaction();

        this.mtitles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        switch (position){
            case 0:
                return new Fragment_tracker();

            case 1:

                return new Fragment_history();



        }
        return null;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mtitles[position];
    }
}