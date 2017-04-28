package redeyes17.com.abhi.android.iamat.UI.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import redeyes17.com.abhi.android.iamat.FireBase_references.FirebaseAuthentication;
import redeyes17.com.abhi.android.iamat.FireBase_references.Firebase_database;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);

        configureViews();

        mauthstatelistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mauth.getCurrentUser();
                final String uid = user.getUid();
                if(user != null){
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(uid)){

                            }else{
                                Intent intent = new Intent(getApplicationContext(), UserProfileFragment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{

                }
            }
        };

    }

    private void configureViews() {
        mauth= FirebaseAuthentication.getInstance();
        databaseReference = Firebase_database.getInstance().getReference().child("user_profiles");
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
               finish();
               Intent intent= new Intent(UserHome.this,MainActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               mauth.signOut();

               return true;
       }
       return super.onOptionsItemSelected(item);
    }


}
