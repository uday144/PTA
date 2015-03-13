package com.ptapp.io.model;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class MessageDetails {
    // public variables

    public int messageId;
    public int groupMasterId;
    public int p2pId;
    public int senderUserId;
    public String senderRole;
    public String messageType;
    public String recipientFilter;
    public  long timestamp;
    public long sentTimestamp;
    public long recipientDeviceTimestamp;
    public String status;
    public String data;
    public String mediaMimeType;
    public int mediaSize;
    public String mediaName;
    public String duration;
    public String mediaURL;
    public String mediaThumbURL;

    // Empty constructor
    public MessageDetails() {

    }

}
