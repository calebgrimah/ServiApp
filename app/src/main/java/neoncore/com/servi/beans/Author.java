package neoncore.com.servi.beans;

import com.firebase.ui.auth.data.model.User;

/**
 * Created by Musa on 3/19/2018.
 */


public class Author {

    private String name, userID,photoUrl,email,phoneNumber;
    private String userLat,usrLong;

    public Author(String name, String userID, String photoUrl, String email, String phoneNumber, String userLat, String usrLong) {
        this.name = name;
        this.userID = userID;
        this.photoUrl = photoUrl;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userLat = userLat;
        this.usrLong = usrLong;
    }

    public Author() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getUsrLong() {
        return usrLong;
    }

    public void setUsrLong(String usrLong) {
        this.usrLong = usrLong;
    }
}
