package redeyes17.com.abhi.android.iamat.UI.tabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import redeyes17.com.abhi.android.iamat.R;

/**
 * Created by Abhilash Reddy on 4/29/2017.
 */

public class UserPostsFragment extends Fragment{
static UserPostsFragment fragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_posts,container,false);


        return view;
    }

    public static Fragment getInstance(){
        if(fragment == null){
            fragment = new UserPostsFragment();
            return fragment;
        }
        return fragment;
    }
}
