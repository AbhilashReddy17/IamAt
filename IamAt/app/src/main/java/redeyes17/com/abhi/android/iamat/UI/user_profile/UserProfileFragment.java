package redeyes17.com.abhi.android.iamat.UI.user_profile;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import redeyes17.com.abhi.android.iamat.R;
import redeyes17.com.abhi.android.iamat.UI.User.UserHome;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class UserProfileFragment extends Fragment{
    public static final String USERPROFILE ="userprofile";
    private static final int RC_GALLERY = 20;
    private static final int REQUEST_IMAGE_CAPTURE = 21;
    ImageView img;
    TextView username,status;
    Button saveprofilebutton;
    Uri imageHoldUri=null;

    FirebaseAuth mauth;
    DatabaseReference databasereference;
  StorageReference mstorageReference;
AlertDialog.Builder builder;

    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.userprofile_fragment,container,false);

        configureViews(view);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] titles = {"Choose from gallery","Take Photo","cancel"};
                builder.setItems(titles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(titles[which].equals("Choose from gallery")){
                            chooseFromGallery();
                        }
                        else if(titles[which].equals("Take Photo")){
                            takePhoto();
                        }
                        else if(titles[which].equals("cancel")){
                            dialog.dismiss();
                        }

                    }
                });
                builder.show();
            }
        });

        saveprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
               createProfile();
                Intent intent = new Intent(getActivity(),UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        return view;
    }

    private void createProfile() {
        String name = username.getText().toString().trim();
        String sts = status.getText().toString().trim();
       final String uid = mauth.getCurrentUser().getUid();

        databasereference.child(uid).child("username").setValue(name);
        databasereference.child(uid).child("status").setValue(sts);
        databasereference.child(uid).child("uid").setValue(uid);

        if(imageHoldUri != null){

           StorageReference storage = mstorageReference.child("user_profile_picture")
                                                        .child(uid)
                                                        .child(imageHoldUri.getLastPathSegment());

                storage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         Uri uri = taskSnapshot.getDownloadUrl();
                        databasereference.child(uid).child("profilepicture").setValue(uri);
                        Intent intent = new Intent(getActivity(), UserHome.class);
                        startActivity(intent);
                    }
                });
        }
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GALLERY && resultCode== RESULT_OK){
getImage(data);
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
getImage(data);
        }

        Toast.makeText(getActivity(), requestCode + "", Toast.LENGTH_SHORT).show();
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(getActivity(), "enered onActivityresult", Toast.LENGTH_SHORT).show();
            Log.d("entered","setting image");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                Log.d("image uri",imageHoldUri.toString());
                img.setImageURI(imageHoldUri);
            }

            if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void getImage(Intent data) {

        Uri imageuri = data.getData();
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(getActivity());
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void chooseFromGallery() {
        Log.d("useractivity profile","entered choose from gallery");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,RC_GALLERY);
    }

    private void configureViews(View view) {
        img = (ImageView) view.findViewById(R.id.userprofile_image);
        username = (TextView) view.findViewById(R.id.userrprofie_username);
        status = (TextView) view.findViewById(R.id.userprofile_status);
        saveprofilebutton = (Button) view.findViewById(R.id.userprofile_saveprofile_button);
        builder = new AlertDialog.Builder(getActivity());
        mstorageReference = FirebaseStorage.getInstance().getReference();
        databasereference = FirebaseDatabase.getInstance().getReference().child("user_profiles");
        mauth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getActivity());
        Log.d("UserProfileFragment ",getActivity().toString());
    }
}
