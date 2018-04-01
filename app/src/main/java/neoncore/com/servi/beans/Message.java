package neoncore.com.servi.beans;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import neoncore.com.servi.beans.Author;

/**
 * Created by Musa on 2/21/2018.
 */

public class Message {

    String receiverID;
    String text;
    @ServerTimestamp
    Date timestamp;
    Author author;

    public Message(String receiverID, String text, Author author) {
        this.receiverID = receiverID;
        this.text = text;
        this.author = author;
    }

    public Message() {
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
