package neoncore.com.servi.beans;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Musa on 3/15/2018.
 */

public class TaskRequest {
    private String categoryID,taskMessage,senderID,taskMessageID,taskDescription;
    @ServerTimestamp
    private Date timestamp;
    private String dueDate;
    private boolean isTaskComplete;
    private GeoPoint geoPoint;

    public TaskRequest() {
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public TaskRequest(String categoryID, String taskMessage, String senderID, String taskMessageID,
                       String taskDescription, boolean isTaskComplete, String dueDate, GeoPoint point) {
        this.categoryID = categoryID;
        this.taskMessage = taskMessage;
        this.senderID = senderID;
        this.taskMessageID = taskMessageID;
        this.taskDescription = taskDescription;
        this.isTaskComplete = isTaskComplete;
        this.dueDate = dueDate;
        this.geoPoint = point;

    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getTaskMessage() {
        return taskMessage;
    }

    public void setTaskMessage(String taskMessage) {
        this.taskMessage = taskMessage;
    }


    //person posting the task
    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getTaskMessageID() {
        return taskMessageID;
    }

    public void setTaskMessageID(String taskMessageID) {
        this.taskMessageID = taskMessageID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }

    public boolean isTaskComplete() {
        return isTaskComplete;
    }

    public void setTaskComplete(boolean taskComplete) {
        isTaskComplete = taskComplete;
    }
}
