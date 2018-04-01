package neoncore.com.servi.beans;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.firestore.ServerTimestamp;

/**
 * Created by Musa on 3/19/2018.
 */

public class Interaction {

    String taskID,senderID;
    Author author;
    @ServerTimestamp
    String timeStamp;


    public Interaction() {
    }

    public Interaction(String taskID,String senderID, Author author) {
        this.taskID = taskID;
        this.senderID = senderID;
        this.author = author;

    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public Author getAuthor() {
        return author;
    }

    public void setUserID(String userID) {
        this.author = author;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;

    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
