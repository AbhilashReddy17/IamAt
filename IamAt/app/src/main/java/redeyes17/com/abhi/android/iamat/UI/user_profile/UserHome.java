package redeyes17.com.abhi.android.iamat.UI.user_profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import redeyes17.com.abhi.android.iamat.UI.tabs.CustomAdapter;

/**
 * Created by Abhilash Reddy on 4/27/2017.
 */

public class   UserHome extends AppCompatActivity {
    FirebaseAuth mauth;

    FirebaseAuth.AuthStateListener mauthstatelistener;
    DatabaseReference databaseReference;
    LinearLayout fragmentlayout ;
    ViewPager pager;
    CustomAdapter adapter;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        Log.d("userhome", "onCreate: ");
        configureViews();
        pager = (ViewPager) findViewById(R.id.userhome_viewpager);
        manager = getSupportFragmentManager();
        adapter = new CustomAdapter(manager);
        pager.setAdapter(adapter);

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

                                //setting the viewpager witht the all three fragments
                                pager.setAdapter(adapter);

                            }else{
                                //if user doesnot has child then directing to create userprofile;
                                fragmentlayout.setVisibility(View.VISIBLE);
                                Log.d("userhome","got into no uid");
                                Intent intent = new Intent(UserHome.this,UserProfileFragment.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    //if doesnot has the user then directing to mainactivity from there it will take to signin page
                    Log.d("userhome", "else user null ");
                    Intent intent= new Intent(UserHome.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
                }
            }
        };

        mauth.addAuthStateListener(mauthstatelistener);

    }


   //_______________________________________________________________________________________________
    //general configuring views and menu settings are below
    //onPause and onResume method is also here below

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
//__________________________________________________________________________________________________

}
