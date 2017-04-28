package redeyes17.com.abhi.android.iamat.UI.user_profile;

/**
 * Created by Abhilash Reddy on 4/25/2017.
 */

public class User {

    String username, status;
    int img;

    public User(String username, String status, int img) {
        this.username = username;
        this.status = status;
        this.img = img;
    }

    public User(String username, String status) {
        this.username = username;
        this.status = status;
        this.img = 0;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
