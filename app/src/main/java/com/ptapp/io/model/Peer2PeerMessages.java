package com.ptapp.io.model;

/**
 * This class is our model and contains the data we will save in the database
 * and show in the user interface.
 */
public class Peer2PeerMessages {
    // public variables

    public int p2pId;
    public int senderUserId;
    public String senderRole;
    public int recipientUserId;
    public String recipientRole;
    public int studentId;

    // Empty constructor
    public Peer2PeerMessages() {

    }

}
