package redeyes17.com.abhi.android.iamat.Login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import redeyes17.com.abhi.android.iamat.R;
import redeyes17.com.abhi.android.iamat.UI.User.UserHome;
import redeyes17.com.abhi.android.iamat.UI.user_profile.UserProfileFragment;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class SignUpFragment extends Fragment{
    public static final String SIGNUP_FRAGMENT = "signupfragment";
    TextView emailTextView, passwordTextView, reenterpasswordTextView,signup;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment,container,false);

        configureViews(view);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailTextView.getText().toString().trim();
                String password= passwordTextView.getText().toString().trim();
                String reenterpassword= reenterpasswordTextView.getText().toString().trim();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(reenterpassword)){
                    if(!password.equals(reenterpassword)){
                        Toast.makeText(getActivity(), "your passwords donot match", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dialog.setMessage("please wait while we create your account");
                        dialog.show();
                        //do the login here
                       createuser(email,password);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "please enter all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void createuser(String email, String password) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mauth = FirebaseAuth.getInstance();
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               Fragment fragment = new UserProfileFragment();
               FragmentManager manager = getActivity().getSupportFragmentManager();
               FragmentTransaction transaction = manager.beginTransaction();
               transaction.detach(manager.findFragmentByTag(SIGNUP_FRAGMENT));
               transaction.add(R.id.login_fragment_container,fragment, UserProfileFragment.USERPROFILE);
               transaction.commit();
                dialog.dismiss();
               Toast.makeText(getActivity(), "your account created", Toast.LENGTH_SHORT).show();
           }
           else{
               dialog.dismiss();
               Toast.makeText(getActivity(), "There was some problem creating account", Toast.LENGTH_SHORT).show();
           }
            }
        });


    }

    private void configureViews(View view) {
        dialog = new ProgressDialog(getActivity());
        emailTextView = (TextView) view.findViewById(R.id.signup_emailid);
        passwordTextView = (TextView) view.findViewById(R.id.signup_password);
        reenterpasswordTextView = (TextView) view.findViewById(R.id.signup_reenter_password);
        signup = (TextView) view.findViewById(R.id.signup);
    }
}
