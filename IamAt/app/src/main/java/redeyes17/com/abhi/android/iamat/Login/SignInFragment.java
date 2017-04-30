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
import com.google.firebase.auth.FirebaseUser;

import redeyes17.com.abhi.android.iamat.R;
import redeyes17.com.abhi.android.iamat.UI.user_profile.UserHome;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class SignInFragment extends Fragment {

    public static final String SIGNIN_FRAGMENT = "signinfragment";
    ProgressDialog dialog;
    TextView signup,email,password,login;
    FragmentTransaction transaction;
    FragmentManager manager;
    FirebaseAuth mauth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_in_fragment,container,false);

        configureViews(view);
        
        //clicking login
        
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("");
                dialog.show();
                String emailValue,passswordValue;
                emailValue = email.getText().toString().trim();
                passswordValue = password.getText().toString().trim();

                if(!TextUtils.isEmpty(emailValue) && !TextUtils.isEmpty(passswordValue)){

                    mauth.signInWithEmailAndPassword(emailValue,passswordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if(task.isSuccessful()){
                                FirebaseUser user = mauth.getCurrentUser();
                                Intent intent = new Intent(getActivity(), UserHome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getActivity(), "login not successfull", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    
                }

            }
        });
        
        //clicking signup
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.setMessage("");
                dialog.show();
               transaction.detach(manager.findFragmentByTag(SIGNIN_FRAGMENT));
                transaction.add(R.id.login_fragment_container,new SignUpFragment(),SignUpFragment.SIGNUP_FRAGMENT);
                transaction.addToBackStack(SignUpFragment.SIGNUP_FRAGMENT);
                transaction.commit();
                dialog.dismiss();

            }
        });

        return view;
    }


    private void configureViews(View view) {

        dialog = new ProgressDialog(getActivity());
        signup = (TextView) view.findViewById(R.id.signup_button);
        email = (TextView) view.findViewById(R.id.login_email);
        password = (TextView) view.findViewById(R.id.login_password);
        login = (TextView) view.findViewById(R.id.login_button);
        manager = getActivity().getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mauth = FirebaseAuth.getInstance();
    }
}
