package redeyes17.com.abhi.android.iamat.Login;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class FireApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
}
