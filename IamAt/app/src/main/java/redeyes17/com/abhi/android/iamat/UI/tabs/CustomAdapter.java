package redeyes17.com.abhi.android.iamat.UI.tabs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Abhilash Reddy on 4/29/2017.
 */

public class CustomAdapter extends FragmentStatePagerAdapter{
    public CustomAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                   return RecentFragment.getInstance();
            case 1:
                return TopperFragment.getInstance();
            case 2:
                return UserPostsFragment.getInstance();
            default:
                return RecentFragment.getInstance();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
