package com.ptapp.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;

/**
 * Created by lifestyle on 03-03-15.
 */
public class MessageStatus extends AbstractEntity {
    public static final String TABLENAME = "messages_status";

    public static final String MESSAGE_ID = "message_id";
    public static final String RECIPIENT_JID = "recipient_jid";
    public static final String STATUS = "status";
    public static final String TIMESTAMP = "timestamp";

    private MessageStatus(){}

    public MessageStatus(final String messageId, final Jid recipientJid,
                    final int status, final long timestamp) {
        this.messageId = messageId;
        this.recipientJid = recipientJid;
        this.status = status;
        this.timestamp = timestamp;
    }


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Jid getRecipientJid() {
        return recipientJid;
    }

    public void setRecipientJid(Jid recipientJid) {
        this.recipientJid = recipientJid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    protected String messageId;
    protected Jid recipientJid;
    protected int status;
    protected long timestamp;

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(MESSAGE_ID, messageId);
        values.put(RECIPIENT_JID, recipientJid.toString());
        values.put(STATUS, status);
        values.put(TIMESTAMP, timestamp);
        return values;
    }


    public static MessageStatus fromCursor(Cursor cursor) {
        Jid jid;
        try {
            jid = Jid.fromString(cursor.getString(cursor.getColumnIndex(RECIPIENT_JID)));
        } catch (final InvalidJidException e) {
            // Borked DB..
            jid = null;
        }

        return new MessageStatus(
                cursor.getString(cursor.getColumnIndex(MESSAGE_ID)),
                jid,
                cursor.getInt(cursor.getColumnIndex(STATUS)),
                cursor.getLong(cursor.getColumnIndex(TIMESTAMP)));
    }


}
