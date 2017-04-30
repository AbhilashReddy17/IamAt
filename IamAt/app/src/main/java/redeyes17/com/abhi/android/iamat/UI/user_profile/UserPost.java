package redeyes17.com.abhi.android.iamat.UI.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import redeyes17.com.abhi.android.iamat.R;

import static android.R.attr.configure;

/**
 * Created by Abhilash Reddy on 4/30/2017.
 */

public class UserPost extends Fragment{

    ImageView postingPicture;
    EditText pictureStatus;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_post,container,false);

        configureViews(view);

        postingPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(MediaStore.ACTION)
            }
        });


        return view;
    }

    private void configureViews(View view) {
        postingPicture = (ImageView) view.findViewById(R.id.recentpost_image);
        pictureStatus = (EditText) view.findViewById(R.id.recentpost_status);

    }
}
