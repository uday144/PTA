package com.ptapp.entities;

import android.content.ContentValues;
import android.database.Cursor;

import com.ptapp.Config;
import com.ptapp.xmpp.jid.InvalidJidException;
import com.ptapp.xmpp.jid.Jid;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Message extends AbstractEntity {
    public static final String TABLENAME = "messages";

    public static final int STATUS_RECEIVED = 0;
    public static final int STATUS_UNSEND = 1;
    public static final int STATUS_SEND = 2;
    public static final int STATUS_SEND_FAILED = 3;
    public static final int STATUS_WAITING = 5;
    public static final int STATUS_OFFERED = 6;
    public static final int STATUS_SEND_RECEIVED = 7;
    public static final int STATUS_SEND_DISPLAYED = 8;

    public static final int ENCRYPTION_NONE = 0;
    public static final int ENCRYPTION_PGP = 1;
    public static final int ENCRYPTION_OTR = 2;
    public static final int ENCRYPTION_DECRYPTED = 3;
    public static final int ENCRYPTION_DECRYPTION_FAILED = 4;

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_FILE = 2;
    public static final int TYPE_STATUS = 3;
    public static final int TYPE_PRIVATE = 4;

    public static final String CONVERSATION = "conversationUuid";
    public static final String COUNTERPART = "counterpart";
    public static final String TRUE_COUNTERPART = "trueCounterpart";
    public static final String BODY = "body";
    public static final String TIME_SENT = "timeSent";
    public static final String ENCRYPTION = "encryption";
    public static final String STATUS = "status";
    public static final String DELIVERY_COUNT = "deliveryCount";
    public static final String STUDENT_ID = "student_id";
    public static final String FLAGS = "flags";
    public static final String TYPE = "type";
    public static final String REMOTE_MSG_ID = "remoteMsgId";
    public static final String SERVER_MSG_ID = "serverMsgId";
    public static final String RELATIVE_FILE_PATH = "relativeFilePath";
    public static final String MSG_TYPE = "msgType";

    //public static final String USER_TYPE_S = "S"; //STUDENT
    public static final String PARENT_TYPE_F = "F"; //FATHER
    public static final String PARENT_TYPE_M = "M"; //MOTHER

    public boolean markable = false;
    protected String conversationUuid;
    /*protected Jid counterpart;*/ //replaced by counterpartJIds
    /*protected Jid trueCounterpart;*/
    protected String body;
    protected String encryptedBody;
    protected long timeSent;
    protected int encryption;
    protected int status;
    protected int deliveryCount;
    protected int studentId;
    protected int type;
    protected String relativeFilePath;
    protected String msgType;
    protected boolean read = true;
    protected String remoteMsgId = null;
    protected String serverMsgId = null;
    protected Conversation conversation = null;
    protected Downloadable downloadable = null;
    private Message mNextMessage = null;
    private Message mPreviousMessage = null;
    private ArrayList<Jid> counterpartJIds = new ArrayList<>();
    private ArrayList<String> flags = new ArrayList<>();

    private Message() {

    }

    public Message(Conversation conversation, String body, int encryption,
                   ArrayList<Jid> counterpartJIds, int studentId) {
        this(conversation, body, encryption, STATUS_UNSEND, 0, counterpartJIds, studentId);
    }

    public Message(Conversation conversation, String body, int encryption, int status, int deliveryCount,
                   ArrayList<Jid> counterpartJIds, int studentId) {

        this(java.util.UUID.randomUUID().toString(),
                conversation.getUuid(),
                counterpartJIds,
                body,
                System.currentTimeMillis(),
                encryption,
                status,
                deliveryCount,
                TYPE_TEXT,
                null,
                null,
                null,
                null,
                studentId
        );
        this.conversation = conversation;
    }

    private Message(final String uuid, final String conversationUUid, ArrayList<Jid> counterpartJIds,
                    final String body, final long timeSent, final int encryption, final int status, final int deliveryCount,
                    final int type, final String remoteMsgId, final String relativeFilePath,
                    final String serverMsgId, final String msgType, final int studentId) {
        this.uuid = uuid;
        this.conversationUuid = conversationUUid;
        this.counterpartJIds = counterpartJIds;
        this.body = body;
        this.timeSent = timeSent;
        this.encryption = encryption;
        this.status = status;
        this.deliveryCount = deliveryCount;
        this.type = type;
        this.remoteMsgId = remoteMsgId;
        this.relativeFilePath = relativeFilePath;
        this.serverMsgId = serverMsgId;
        this.msgType = msgType;
        this.studentId = studentId;
    }

    public static Message fromCursor(Cursor cursor) {
        ArrayList<Jid> jids = new ArrayList<>();
        try {
            String value = cursor.getString(cursor.getColumnIndex(COUNTERPART));
            if (value != null) {
                String[] separated = value.split(",");
                for (String s : separated) {
                    jids.add(Jid.fromString(s));
                }
            } else {
                jids = null;
            }
        } catch (InvalidJidException e) {
            jids = null;
        }
        return new Message(cursor.getString(cursor.getColumnIndex(UUID)),
                cursor.getString(cursor.getColumnIndex(CONVERSATION)),
                jids,
                cursor.getString(cursor.getColumnIndex(BODY)),
                cursor.getLong(cursor.getColumnIndex(TIME_SENT)),
                cursor.getInt(cursor.getColumnIndex(ENCRYPTION)),
                cursor.getInt(cursor.getColumnIndex(STATUS)),
                cursor.getInt(cursor.getColumnIndex(DELIVERY_COUNT)),
                cursor.getInt(cursor.getColumnIndex(TYPE)),
                cursor.getString(cursor.getColumnIndex(REMOTE_MSG_ID)),
                cursor.getString(cursor.getColumnIndex(RELATIVE_FILE_PATH)),
                cursor.getString(cursor.getColumnIndex(SERVER_MSG_ID)),
                cursor.getString(cursor.getColumnIndex(MSG_TYPE)),
                cursor.getInt(cursor.getColumnIndex(STUDENT_ID))
        );
    }

    public static Message createStatusMessage(Conversation conversation) {
        Message message = new Message();
        message.setType(Message.TYPE_STATUS);
        message.setConversation(conversation);
        return message;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(UUID, uuid);
        values.put(CONVERSATION, conversationUuid);
        /*if (counterpart == null) {
            values.putNull(COUNTERPART);
        } else {
            values.put(COUNTERPART, counterpart.toString());
        }*/
        if (counterpartJIds == null) {
            values.putNull(COUNTERPART);
        } else {
            String counterpart = "";
            for (Jid jid : counterpartJIds) {
                counterpart += jid + ",";
            }
            values.put(COUNTERPART, counterpart);
        }
        /*if (trueCounterpart == null) {
            values.putNull(TRUE_COUNTERPART);
        } else {
            values.put(TRUE_COUNTERPART, trueCounterpart.toString());
        }*/
        values.put(BODY, body);
        values.put(TIME_SENT, timeSent);
        values.put(ENCRYPTION, encryption);
        values.put(STATUS, status);
        values.put(DELIVERY_COUNT, deliveryCount);
        values.put(TYPE, type);
        values.put(REMOTE_MSG_ID, remoteMsgId);
        values.put(RELATIVE_FILE_PATH, relativeFilePath);
        values.put(SERVER_MSG_ID, serverMsgId);
        values.put(STUDENT_ID, studentId);
        return values;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public ArrayList<String> getFlags() {
        return flags;
    }

    public void setFlags(ArrayList<String> flags) {
        this.flags = flags;
    }

    public ArrayList<Jid> getCounterpartJIds() {
        return counterpartJIds;
    }

    public void setCounterpartJIds(ArrayList<Jid> jIds) {
        this.counterpartJIds = jIds;
    }

    public String getConversationUuid() {
        return conversationUuid;
    }

    public Conversation getConversation() {
        return this.conversation;
    }

    public void setConversation(Conversation conv) {
        this.conversation = conv;
    }

    /*public Jid getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(final Jid counterpart) {
        this.counterpart = counterpart;
    }*/

    public Contact getContact() {
        if (this.conversation.getMode() == Conversation.MODE_SINGLE) {
            return this.conversation.getContact();
        } else {
            /*if (this.trueCounterpart == null) {
                return null;
            } else {
                return this.conversation.getAccount().getRoster()
                        .getContactFromRoster(this.trueCounterpart);
            }*/
            return null;
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public int getEncryption() {
        return encryption;
    }

    public void setEncryption(int encryption) {
        this.encryption = encryption;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public String getRelativeFilePath() {
        return this.relativeFilePath;
    }

    public void setRelativeFilePath(String path) {
        this.relativeFilePath = path;
    }

    public String getRemoteMsgId() {
        return this.remoteMsgId;
    }

    public void setRemoteMsgId(String id) {
        this.remoteMsgId = id;
    }

    public String getServerMsgId() {
        return this.serverMsgId;
    }

    public void setServerMsgId(String id) {
        this.serverMsgId = id;
    }

    public boolean isRead() {
        return this.read;
    }

    public void markRead() {
        this.read = true;
    }

    public void markUnread() {
        this.read = false;
    }

    public void setTime(long time) {
        this.timeSent = time;
    }

    public String getEncryptedBody() {
        return this.encryptedBody;
    }

    public void setEncryptedBody(String body) {
        this.encryptedBody = body;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /*public void setTrueCounterpart(Jid trueCounterpart) {
        this.trueCounterpart = trueCounterpart;
    }*/

    public Downloadable getDownloadable() {
        return this.downloadable;
    }

    public void setDownloadable(Downloadable downloadable) {
        this.downloadable = downloadable;
    }

    public boolean equals(Message message) {
        if (this.serverMsgId != null && message.getServerMsgId() != null) {
            return this.serverMsgId.equals(message.getServerMsgId());
        } else if (this.body == null || this.counterpartJIds.isEmpty()) {
            return false;
        } else if (message.getRemoteMsgId() != null) {
            return (message.getRemoteMsgId().equals(this.remoteMsgId) || message.getRemoteMsgId().equals(this.uuid))
                    && this.counterpartJIds.equals(message.getCounterpartJIds())
                    && this.body.equals(message.getBody());
        } else {
            return this.remoteMsgId == null
                    && this.counterpartJIds.equals(message.getCounterpartJIds())
                    && this.body.equals(message.getBody())
                    && Math.abs(this.getTimeSent() - message.getTimeSent()) < Config.PING_TIMEOUT * 500;
        }
    }

    public Message next() {
        synchronized (this.conversation.messages) {
            if (this.mNextMessage == null) {
                int index = this.conversation.messages.indexOf(this);
                if (index < 0 || index >= this.conversation.messages.size() - 1) {
                    this.mNextMessage = null;
                } else {
                    this.mNextMessage = this.conversation.messages.get(index + 1);
                }
            }
            return this.mNextMessage;
        }
    }

    public Message prev() {
        synchronized (this.conversation.messages) {
            if (this.mPreviousMessage == null) {
                int index = this.conversation.messages.indexOf(this);
                if (index <= 0 || index > this.conversation.messages.size()) {
                    this.mPreviousMessage = null;
                } else {
                    this.mPreviousMessage = this.conversation.messages.get(index - 1);
                }
            }
            return this.mPreviousMessage;
        }
    }

    public boolean mergeable(final Message message) {
        return message != null &&
                (message.getType() == Message.TYPE_TEXT &&
                        this.getDownloadable() == null &&
                        message.getDownloadable() == null &&
                        message.getEncryption() != Message.ENCRYPTION_PGP &&
                        this.getType() == message.getType() &&
                        this.getStatus() == message.getStatus() &&
                        this.getDeliveryCount() == message.getDeliveryCount() &&
                        this.getEncryption() == message.getEncryption() &&
                        /*this.getCounterpart() != null &&
                        this.getCounterpart().equals(message.getCounterpart()) &&*/
                        this.getCounterpartJIds() != null &&
                        this.getCounterpartJIds().equals(message.getCounterpartJIds()) &&
                        (message.getTimeSent() - this.getTimeSent()) <= (Config.MESSAGE_MERGE_WINDOW * 1000) &&
                        !message.bodyContainsDownloadable() &&
                        !this.bodyContainsDownloadable() &&
                        !this.body.startsWith("/me ")
                );
    }

    public String getMergedBody() {
        final Message next = this.next();
        if (this.mergeable(next)) {
            return getBody() + '\n' + next.getMergedBody();
        }
        return getBody();
    }

    public boolean hasMeCommand() {
        return getMergedBody().startsWith("/me ");
    }

    public int getMergedStatus() {
        return getStatus();
    }

    public long getMergedTimeSent() {
        Message next = this.next();
        if (this.mergeable(next)) {
            return next.getMergedTimeSent();
        } else {
            return getTimeSent();
        }
    }

    public boolean wasMergedIntoPrevious() {
        Message prev = this.prev();
        return prev != null && prev.mergeable(this);
    }

    public boolean trusted() {
        Contact contact = this.getContact();
        return (status > STATUS_RECEIVED || (contact != null && contact.trusted()));
    }

    public boolean bodyContainsDownloadable() {
        try {
            URL url = new URL(this.getBody());
            if (!url.getProtocol().equalsIgnoreCase("http")
                    && !url.getProtocol().equalsIgnoreCase("https")) {
                return false;
            }
            if (url.getPath() == null) {
                return false;
            }
            String[] pathParts = url.getPath().split("/");
            String filename;
            if (pathParts.length > 0) {
                filename = pathParts[pathParts.length - 1].toLowerCase();
            } else {
                return false;
            }
            String[] extensionParts = filename.split("\\.");
            if (extensionParts.length == 2
                    && Arrays.asList(Downloadable.VALID_IMAGE_EXTENSIONS).contains(
                    extensionParts[extensionParts.length - 1])) {
                return true;
            } else if (extensionParts.length == 3
                    && Arrays
                    .asList(Downloadable.VALID_CRYPTO_EXTENSIONS)
                    .contains(extensionParts[extensionParts.length - 1])
                    && Arrays.asList(Downloadable.VALID_IMAGE_EXTENSIONS).contains(
                    extensionParts[extensionParts.length - 2])) {
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public ImageParams getImageParams() {
        ImageParams params = getLegacyImageParams();
        if (params != null) {
            return params;
        }
        params = new ImageParams();
        if (this.downloadable != null) {
            params.size = this.downloadable.getFileSize();
        }
        if (body == null) {
            return params;
        }
        String parts[] = body.split("\\|");
        if (parts.length == 1) {
            try {
                params.size = Long.parseLong(parts[0]);
            } catch (NumberFormatException e) {
                params.origin = parts[0];
                try {
                    params.url = new URL(parts[0]);
                } catch (MalformedURLException e1) {
                    params.url = null;
                }
            }
        } else if (parts.length == 3) {
            try {
                params.size = Long.parseLong(parts[0]);
            } catch (NumberFormatException e) {
                params.size = 0;
            }
            try {
                params.width = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                params.width = 0;
            }
            try {
                params.height = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                params.height = 0;
            }
        } else if (parts.length == 4) {
            params.origin = parts[0];
            try {
                params.url = new URL(parts[0]);
            } catch (MalformedURLException e1) {
                params.url = null;
            }
            try {
                params.size = Long.parseLong(parts[1]);
            } catch (NumberFormatException e) {
                params.size = 0;
            }
            try {
                params.width = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                params.width = 0;
            }
            try {
                params.height = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                params.height = 0;
            }
        }
        return params;
    }

    public ImageParams getLegacyImageParams() {
        ImageParams params = new ImageParams();
        if (body == null) {
            return params;
        }
        String parts[] = body.split(",");
        if (parts.length == 3) {
            try {
                params.size = Long.parseLong(parts[0]);
            } catch (NumberFormatException e) {
                return null;
            }
            try {
                params.width = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return null;
            }
            try {
                params.height = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                return null;
            }
            return params;
        } else {
            return null;
        }
    }

    public void untie() {
        this.mNextMessage = null;
        this.mPreviousMessage = null;
    }

    public boolean isFileOrImage() {
        return type == TYPE_FILE || type == TYPE_IMAGE;
    }

    public class ImageParams {
        public URL url;
        public long size = 0;
        public int width = 0;
        public int height = 0;
        public String origin;
    }
}
