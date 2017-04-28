package redeyes17.com.abhi.android.iamat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import redeyes17.com.abhi.android.iamat.Login.SignInFragment;
import redeyes17.com.abhi.android.iamat.UI.User.UserHome;

public class MainActivity extends AppCompatActivity {

FragmentTransaction transaction;
    FragmentManager manager;
    ProgressDialog dialog;
   FirebaseAuth mauth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureViews();

        FirebaseUser user = mauth.getCurrentUser();

        //check if user is not null then direct to userhome directly
       if (user!=null){
           Intent intent = new Intent(this,UserHome.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
        }
        else {
           //if user is null direct to login page
                    dialog.show();
                    Fragment fragment = new SignInFragment();
                    transaction.add(R.id.login_fragment_container, fragment, SignInFragment.SIGNIN_FRAGMENT);
                    transaction.addToBackStack(SignInFragment.SIGNIN_FRAGMENT);
                    transaction.commit();
                    dialog.dismiss();

                }
            }

    private void configureViews() {
        mauth = FirebaseAuth.getInstance();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        dialog = new ProgressDialog(this);


    }

}
