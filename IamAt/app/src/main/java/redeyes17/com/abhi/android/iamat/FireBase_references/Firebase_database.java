package redeyes17.com.abhi.android.iamat.FireBase_references;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class Firebase_database {

    static FirebaseDatabase mfirebasedatabase;

    //using singleton pattern and getting instances to the FirebaseDatabase
    public static FirebaseDatabase getInstance(){
        if(mfirebasedatabase != null ){
            mfirebasedatabase = FirebaseDatabase.getInstance();
            return mfirebasedatabase;
        }
        else {
            return mfirebasedatabase;
        }
    }

}
