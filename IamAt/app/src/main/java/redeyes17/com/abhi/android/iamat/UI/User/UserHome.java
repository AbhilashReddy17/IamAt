package redeyes17.com.abhi.android.iamat.UI.User;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import redeyes17.com.abhi.android.iamat.MainActivity;
import redeyes17.com.abhi.android.iamat.R;
import redeyes17.com.abhi.android.iamat.UI.user_profile.UserProfileFragment;

/**
 * Created by Abhilash Reddy on 4/27/2017.
 */

public class   UserHome extends AppCompatActivity {
    FirebaseAuth mauth;

    FirebaseAuth.AuthStateListener mauthstatelistener;
    DatabaseReference databaseReference;
    LinearLayout fragmentlayout ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        Log.d("userhome", "onCreate: ");
        configureViews();

        mauthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mauth.getCurrentUser();
                if(user != null){
                    final String uid = user.getUid();
                    Log.d("userhome", "onAuthStateChanged: ");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(uid)){
                                fragmentlayout.setVisibility(View.GONE);
                                Log.d("userhome","got into  uid");
                            }else{
                                fragmentlayout.setVisibility(View.VISIBLE);
                                Log.d("userhome","got into no uid");
                                Fragment fragment = new UserProfileFragment();
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();

                                transaction.add(R.id.userprofilefragment_container,fragment,UserProfileFragment.USERPROFILE);
                                transaction.commit();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Log.d("userhome", "else user null ");
                    Intent intent= new Intent(UserHome.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
                }
            }
        };

        mauth.addAuthStateListener(mauthstatelistener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mauth.addAuthStateListener(mauthstatelistener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mauth.removeAuthStateListener(mauthstatelistener);
    }

    private void configureViews() {
        mauth= FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_profiles");
        fragmentlayout = (LinearLayout) findViewById(R.id.userprofilefragment_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch(item.getItemId()) {
           case R.id.sign_out:
               mauth.signOut();

               return true;
       }
       return super.onOptionsItemSelected(item);
    }


}
