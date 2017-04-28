package redeyes17.com.abhi.android.iamat.FireBase_references;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class FirebaseAuthentication {

    static FirebaseAuth mauth;

    //using singleton pattern and getting instances to the FirebaseAuth
    public static FirebaseAuth getInstance(){
        if(mauth!=null){
            return mauth;
        }
        else {
            mauth = FirebaseAuth.getInstance();
            return mauth;
        }
    }

}
