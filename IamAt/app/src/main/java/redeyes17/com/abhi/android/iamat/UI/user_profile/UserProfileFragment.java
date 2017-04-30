package redeyes17.com.abhi.android.iamat.UI.user_profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.io.ByteArrayOutputStream;
import java.io.File;

import redeyes17.com.abhi.android.iamat.R;

import static android.R.attr.permission;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class UserProfileFragment extends AppCompatActivity {
    public static final String USERPROFILE = "userprofile";
    private static final int RC_GALLERY = 20;
    private static final int REQUEST_IMAGE_CAPTURE = 21;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;
    private static final String TAG = "userprofileactivity";
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 13;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 12;
    File imageFile;
    ImageView img;
    TextView username, status;
    Button saveprofilebutton;
    Uri imageHoldUri = null;
    boolean permissionRead = false;
    boolean permissionCamera = false;
    boolean permissionWrite = false;


    FirebaseAuth mauth;
    DatabaseReference databasereference;
    StorageReference mstorageReference;
    AlertDialog.Builder builder;
    View mySnackbar;
    ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_fragment);
        configureViews();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] titles = {"Choose from gallery", "Take Photo", "cancel"};
                builder.setItems(titles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (titles[which].equals("Choose from gallery")) {
                            //checking the permissions after marshmellow
                            checkPermissionRead();
                            if (permissionRead)
                                chooseFromGallery();

                        } else if (titles[which].equals("Take Photo")) {
                            checkPermissionCamera();
                            checkPermissionRead();
                            if(permissionWrite && permissionCamera && permissionRead)
                            takePhoto();
                        } else if (titles[which].equals("cancel")) {
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
                Intent intent = new Intent(UserProfileFragment.this, UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

    }

    private void createProfile() {
        String name = username.getText().toString().trim();
        String sts = status.getText().toString().trim();
        final String uid = mauth.getCurrentUser().getUid();

        databasereference.child(uid).child("username").setValue(name);
        databasereference.child(uid).child("status").setValue(sts);
        databasereference.child(uid).child("uid").setValue(uid);

        if (imageHoldUri != null) {
            Log.d(TAG, "imageholduri is not null");
            StorageReference storage = mstorageReference.child("user_profile_picture")
                    .child(uid)
                    .child(imageHoldUri.getLastPathSegment());

                storage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("UserProfileFragment", "onSuccess: ");
                    Uri uri = taskSnapshot.getDownloadUrl();
                    databasereference.child(uid).child("profilepicture").setValue(uri.toString());
                    Intent intent = new Intent(UserProfileFragment.this, UserHome.class);
                    startActivity(intent);
                }
            });
        }
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            getImage(data);
        }

        Toast.makeText(UserProfileFragment.this, requestCode + "", Toast.LENGTH_SHORT).show();
        if (requestCode == RC_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

           Bundle extras = data.getExtras();
            Bitmap image = extras.getParcelable("data");
            img.setImageBitmap(image);

            imageHoldUri =  getImageUri(getApplicationContext(), image);
            //Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(),image,"profilepic.jpeg",null));;
        } else {
            Toast.makeText(this, "uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void getImage(Intent data) {
        if(imageFile.exists()){
            imageHoldUri = Uri.fromFile(imageFile);
            img.setImageURI(imageHoldUri);
        }
        else{
            Toast.makeText(this, "Failed to take the file", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhoto() {
       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"profilepic.jpg");
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    private void chooseFromGallery() {
        Log.d("useractivity profile", "entered choose from gallery");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RC_GALLERY);
    }

    private void configureViews() {
        img = (ImageView) findViewById(R.id.userprofile_image);
        username = (TextView) findViewById(R.id.userrprofie_username);
        status = (TextView) findViewById(R.id.userprofile_status);
        saveprofilebutton = (Button) findViewById(R.id.userprofile_saveprofile_button);
        builder = new AlertDialog.Builder(UserProfileFragment.this);
        mstorageReference = FirebaseStorage.getInstance().getReference();
        databasereference = FirebaseDatabase.getInstance().getReference().child("user_profiles");
        mauth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(UserProfileFragment.this);
        Log.d("UserProfileFragment ", this.toString());
        mySnackbar = findViewById(R.id.snackbarPosition);
        mySnackbar.setVisibility(View.INVISIBLE);

    }


    //______________________________________________________________________________________________\
    //checking runtime permissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permissionRead was granted, yay!
                    permissionRead = true;

                } else {
                    permissionRead = false;
                    // permissionRead denied, boo! Disable the
                    // functionality that depends on this permissionRead.
                    // Toast.makeText(this, "permissionRead to gallery denied by user", Toast.LENGTH_SHORT).show();

                }
                return;
            }
            case  MY_PERMISSIONS_REQUEST_ACCESS_CAMERA :
                permissionWrite = true;
                permissionCamera = true;
                return;


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void checkPermissionCamera(){
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int permissionCheckCamera = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permissionCheckWrite == PackageManager.PERMISSION_GRANTED && permissionCheckCamera == PackageManager.PERMISSION_GRANTED) {
            permissionCamera = true;
            permissionWrite = true;
        }
        else{

            ActivityCompat.requestPermissions(UserProfileFragment.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
            permissionCamera = true;
        }

//        if (permissionCheckCamera == PackageManager.PERMISSION_GRANTED) {
//            permissionWrite = true;
//        }
//        else{
//
//            ActivityCompat.requestPermissions(UserProfileFragment.this,
//                    new String[]{Manifest.permission.CAMERA},
//                    MY_PERMISSIONS_REQUEST_CAMERA);
//            permissionWrite = true;
//        }

        if (permissionCheckWrite == PackageManager.PERMISSION_DENIED && permissionCheckCamera == PackageManager.PERMISSION_DENIED ) {
            mySnackbar.setVisibility(View.VISIBLE);

            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snackbarPosition),
                    "you denied the permissionRead you cannot access the gallery", Snackbar.LENGTH_SHORT);
            mySnackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(UserProfileFragment.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);

                }
            });
            mySnackbar.show();
        }

        if (permissionCheckCamera == PackageManager.PERMISSION_DENIED ) {
            mySnackbar.setVisibility(View.VISIBLE);

            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snackbarPosition),
                    "you denied the permissionRead you cannot access the gallery", Snackbar.LENGTH_SHORT);
            mySnackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(UserProfileFragment.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);

                }
            });
            mySnackbar.show();
        }

    }

    private void checkPermissionRead() {

        int permissionCheckRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheckRead != PackageManager.PERMISSION_GRANTED) {
// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permissionRead.
                permissionRead = true;
                // Toast.makeText(this, "you need this permissionRead to access camera and store the picture", Toast.LENGTH_SHORT).show();

            } else {

                // No explanation needed, we can request the permissionRead.


                ActivityCompat.requestPermissions(UserProfileFragment.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);


                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        if (permissionCheckRead == PackageManager.PERMISSION_DENIED ) {
            mySnackbar.setVisibility(View.VISIBLE);

            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.snackbarPosition),
                    "you denied the permissionRead you cannot access the gallery", Snackbar.LENGTH_SHORT);
            mySnackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(UserProfileFragment.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                }
            });
            mySnackbar.show();
        }


    }

    //______________________________________________________________________________________________

}
