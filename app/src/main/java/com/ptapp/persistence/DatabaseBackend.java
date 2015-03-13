package com.ptapp.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.ptapp.entities.Account;
import com.ptapp.entities.Contact;
import com.ptapp.entities.Conversation;
import com.ptapp.entities.Message;
import com.ptapp.entities.MessageStatus;
import com.ptapp.entities.Roster;
import com.ptapp.provider.PTAppContract;
import com.ptapp.provider.PTAppDatabase;
import com.ptapp.service.XmppConnectionService;
import com.ptapp.xmpp.jid.Jid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ptapp.utils.LogUtils.LOGW;

public class DatabaseBackend extends SQLiteOpenHelper {

    private static DatabaseBackend instance = null;
    private Context context;

    private static final String TAG = "PTApp - DatabaseBackend";

    private static final String DATABASE_NAME = "apphistory";
    private static final int DATABASE_VERSION = 13;

    private static String CREATE_CONTATCS_STATEMENT = "create table "
            + Contact.TABLENAME + "(" + Contact.ACCOUNT + " TEXT, "
            + Contact.SERVERNAME + " TEXT, " + Contact.SYSTEMNAME + " TEXT,"
            + Contact.JID + " TEXT," + Contact.KEYS + " TEXT,"
            + Contact.PHOTOURI + " TEXT," + Contact.OPTIONS + " NUMBER,"
            + Contact.SYSTEMACCOUNT + " NUMBER, " + Contact.AVATAR + " TEXT, "
            + Contact.LAST_PRESENCE + " TEXT, " + Contact.LAST_TIME + " NUMBER, "
            + Contact.GROUPS + " TEXT, FOREIGN KEY(" + Contact.ACCOUNT + ") REFERENCES "
            + Account.TABLENAME + "(" + Account.UUID
            + ") ON DELETE CASCADE, UNIQUE(" + Contact.ACCOUNT + ", "
            + Contact.JID + ") ON CONFLICT REPLACE);";

    private DatabaseBackend(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        db.execSQL("create table " + Account.TABLENAME + "(" + Account.UUID
                + " TEXT PRIMARY KEY," + Account.USERNAME + " TEXT,"
                + Account.SERVER + " TEXT," + Account.PASSWORD + " TEXT,"
                + Account.ROSTERVERSION + " TEXT," + Account.OPTIONS
                + " NUMBER, " + Account.AVATAR + " TEXT, " + Account.KEYS
                + " TEXT)");

        db.execSQL("create table " + Conversation.TABLENAME + " ( "
                + Conversation.UUID + " TEXT PRIMARY KEY, "
                + Conversation.NAME + " TEXT, "
                + Conversation.CONTACT + " TEXT, "
                + Conversation.ACCOUNT + " TEXT, "
                + Conversation.CONTACTJID + " TEXT, "
                + Conversation.CREATED + " NUMBER, "
                + Conversation.STATUS + " NUMBER, "
                + Conversation.MODE + " NUMBER, "
                + Conversation.GROUP_ID + " INTEGER, "
                + Conversation.STUDENT_ID + " INTEGER, "
                + Conversation.ATTRIBUTES + " TEXT, FOREIGN KEY("
                + Conversation.ACCOUNT + ") REFERENCES "
                + Account.TABLENAME + "(" + Account.UUID + ") ON DELETE CASCADE);");

        db.execSQL("create table " + Message.TABLENAME + "( "
                + Message.UUID + " TEXT PRIMARY KEY, "
                + Message.CONVERSATION + " TEXT, "
                + Message.TIME_SENT + " NUMBER, "
                + Message.COUNTERPART + " TEXT, "
                + Message.BODY + " TEXT, "
                + Message.ENCRYPTION + " NUMBER, "
                + Message.STATUS + " NUMBER, "
                + Message.DELIVERY_COUNT + " NUMBER, "
                + Message.STUDENT_ID + " INTEGER, "
                + Message.FLAGS + " TEXT, "
                + Message.TYPE + " NUMBER, "
                + Message.RELATIVE_FILE_PATH + " TEXT, "
                + Message.SERVER_MSG_ID + " TEXT, "
                + Message.MSG_TYPE + " TEXT, "
                + Message.REMOTE_MSG_ID + " TEXT, FOREIGN KEY("
                + Message.CONVERSATION + ") REFERENCES "
                + Conversation.TABLENAME + "(" + Conversation.UUID
                + ") ON DELETE CASCADE);");

        db.execSQL("create table " + MessageStatus.TABLENAME + "( "
                + BaseColumns._ID + " INTEGER PRIMARY KEY, "
                + MessageStatus.MESSAGE_ID + " TEXT, "
                + MessageStatus.RECIPIENT_JID + " TEXT, "
                + MessageStatus.STATUS + " INTEGER, "
                + MessageStatus.TIMESTAMP + " INTEGER )");

        db.execSQL(CREATE_CONTATCS_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2 && newVersion >= 2) {
            db.execSQL("update " + Account.TABLENAME + " set "
                    + Account.OPTIONS + " = " + Account.OPTIONS + " | 8");
        }
        if (oldVersion < 3 && newVersion >= 3) {
            db.execSQL("ALTER TABLE " + Message.TABLENAME + " ADD COLUMN "
                    + Message.TYPE + " NUMBER");
        }
        if (oldVersion < 5 && newVersion >= 5) {
            db.execSQL("DROP TABLE " + Contact.TABLENAME);
            db.execSQL(CREATE_CONTATCS_STATEMENT);
            db.execSQL("UPDATE " + Account.TABLENAME + " SET "
                    + Account.ROSTERVERSION + " = NULL");
        }
        if (oldVersion < 6 && newVersion >= 6) {
            db.execSQL("ALTER TABLE " + Message.TABLENAME + " ADD COLUMN "
                    + Message.TRUE_COUNTERPART + " TEXT");
        }
        if (oldVersion < 7 && newVersion >= 7) {
            db.execSQL("ALTER TABLE " + Message.TABLENAME + " ADD COLUMN "
                    + Message.REMOTE_MSG_ID + " TEXT");
            db.execSQL("ALTER TABLE " + Contact.TABLENAME + " ADD COLUMN "
                    + Contact.AVATAR + " TEXT");
            db.execSQL("ALTER TABLE " + Account.TABLENAME + " ADD COLUMN "
                    + Account.AVATAR + " TEXT");
        }
        if (oldVersion < 8 && newVersion >= 8) {
            db.execSQL("ALTER TABLE " + Conversation.TABLENAME + " ADD COLUMN "
                    + Conversation.ATTRIBUTES + " TEXT");
        }
        if (oldVersion < 9 && newVersion >= 9) {
            db.execSQL("ALTER TABLE " + Contact.TABLENAME + " ADD COLUMN "
                    + Contact.LAST_TIME + " NUMBER");
            db.execSQL("ALTER TABLE " + Contact.TABLENAME + " ADD COLUMN "
                    + Contact.LAST_PRESENCE + " TEXT");
        }
        if (oldVersion < 10 && newVersion >= 10) {
            db.execSQL("ALTER TABLE " + Message.TABLENAME + " ADD COLUMN "
                    + Message.RELATIVE_FILE_PATH + " TEXT");
        }
        if (oldVersion < 11 && newVersion >= 11) {
            db.execSQL("ALTER TABLE " + Contact.TABLENAME + " ADD COLUMN "
                    + Contact.GROUPS + " TEXT");
            db.execSQL("delete from " + Contact.TABLENAME);
            db.execSQL("update " + Account.TABLENAME + " set " + Account.ROSTERVERSION + " = NULL");
        }
        if (oldVersion < 12 && newVersion >= 12) {
            db.execSQL("ALTER TABLE " + Message.TABLENAME + " ADD COLUMN "
                    + Message.SERVER_MSG_ID + " TEXT");
        }
        if (oldVersion < 13 && newVersion >= 13) {
            db.execSQL("delete from " + Contact.TABLENAME);
            db.execSQL("update " + Account.TABLENAME + " set " + Account.ROSTERVERSION + " = NULL");
        }
    }

    public static synchronized DatabaseBackend getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseBackend(context);
        }
        return instance;
    }

    public void createConversation(Conversation conversation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Conversation.TABLENAME, null, conversation.getContentValues());
        XmppConnectionService.takeDbBackup(context);
    }

    public void createMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Message.TABLENAME, null, message.getContentValues());
        XmppConnectionService.takeDbBackup(context);
    }

    public void createAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Account.TABLENAME, null, account.getContentValues());
        XmppConnectionService.takeDbBackup(context);
    }

    public CopyOnWriteArrayList<Conversation> getConversations(int status) {
        CopyOnWriteArrayList<Conversation> list = new CopyOnWriteArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = {Integer.toString(status)};
        Cursor cursor = db.rawQuery("select * from " + Conversation.TABLENAME
                + " where " + Conversation.STATUS + " = ? order by "
                + Conversation.CREATED + " desc", selectionArgs);
        while (cursor.moveToNext()) {
            list.add(Conversation.fromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    public ArrayList<Message> getMessages(Conversation conversations, int limit) {
        return getMessages(conversations, limit, -1);
    }

    public ArrayList<Message> getMessages(Conversation conversation, int limit, long timestamp) {
        ArrayList<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (timestamp == -1) {
            String[] selectionArgs = {conversation.getUuid()};
            cursor = db.query(Message.TABLENAME, null, Message.CONVERSATION
                    + "=?", selectionArgs, null, null, Message.TIME_SENT
                    + " DESC", String.valueOf(limit));
        } else {
            String[] selectionArgs = {conversation.getUuid(), Long.toString(timestamp)};
            cursor = db.query(Message.TABLENAME, null, Message.CONVERSATION
                            + "=? and " + Message.TIME_SENT + "<?", selectionArgs,
                    null, null, Message.TIME_SENT + " DESC",
                    String.valueOf(limit));
        }
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                Message message = Message.fromCursor(cursor);
                if (message.getCounterpartJIds() != null) {
                    ArrayList<String> flags = getFlagsFromDatabase(message);
                    message.setFlags(flags);
                }
                message.setConversation(conversation);
                list.add(message);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return list;
    }

    private ArrayList<String> getFlagsFromDatabase(Message message) {
        ArrayList<String> flags = new ArrayList<>();
        StringBuilder sb = getQuestionPlaceholders(message.getCounterpartJIds().size());
        ArrayList<String> jids = convertToStringList(message.getCounterpartJIds());
        Uri parentChildUri = PTAppContract.ParentChildRelation.CONTENT_URI;
        String selection = PTAppContract.Parent.JID + " in (" + sb.toString() + ")";
        String[] args = jids.isEmpty() ? null : jids.toArray(new String[0]);
        String[] projection = {PTAppContract.ParentChildRelation.PARENT_TYPE};
        Cursor c = context.getContentResolver().query(parentChildUri, projection, selection, args, null);

        while (c.moveToNext()) {
            flags.add(c.getString(c.getColumnIndex(PTAppContract.ParentChildRelation.PARENT_TYPE))); //ParentType will be as F,M
        }
        return flags;
    }

    //converts ArrayList of Jid to ArrayList of string jid
    private ArrayList<String> convertToStringList(ArrayList<Jid> jIds) {
        ArrayList<String> list = new ArrayList<>();
        for (Jid jid : jIds) {
            list.add(jid.toString());
        }
        return list;
    }

    private StringBuilder getQuestionPlaceholders(int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        int len = count;
        for (int i = 1; i < len; i++) {
            sb.append(",?");
        }
        return sb;
    }

    public Conversation findConversation(final Account account, final Jid contactJid,
                                         final int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if (studentId > 0) { //group messaging
            String[] selectionArgs = {account.getUuid(),
                    contactJid.toBareJid().toString() + "/%",
                    contactJid.toBareJid().toString(),
                    String.valueOf(studentId)
            };
            cursor = db.query(Conversation.TABLENAME, null,
                    Conversation.ACCOUNT + "=? AND (" + Conversation.CONTACTJID
                            + " like ? OR " + Conversation.CONTACTJID + "=?) AND "
                            + Conversation.STUDENT_ID + "=?", selectionArgs, null, null, null);
        } else { //peer to peer
            String[] selectionArgs = {account.getUuid(),
                    contactJid.toBareJid().toString() + "/%",
                    contactJid.toBareJid().toString()
            };
            cursor = db.query(Conversation.TABLENAME, null,
                    Conversation.ACCOUNT + "=? AND (" + Conversation.CONTACTJID
                            + " like ? OR " + Conversation.CONTACTJID + "=?) ", selectionArgs, null, null, null);
        }

        if (cursor.getCount() == 0)
            return null;
        cursor.moveToFirst();
        Conversation conversation = Conversation.fromCursor(cursor);
        cursor.close();
        return conversation;
    }

    public void updateConversation(final Conversation conversation) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final String[] args = {conversation.getUuid()};
        db.update(Conversation.TABLENAME, conversation.getContentValues(),
                Conversation.UUID + "=?", args);
        XmppConnectionService.takeDbBackup(context);
    }

    public List<Account> getAccounts() {
        List<Account> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Account.TABLENAME, null, null, null, null,
                null, null);
        while (cursor.moveToNext()) {
            list.add(Account.fromCursor(cursor));
        }
        cursor.close();
        return list;
    }

    public void updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {account.getUuid()};
        db.update(Account.TABLENAME, account.getContentValues(), Account.UUID
                + "=?", args);
        XmppConnectionService.takeDbBackup(context);
    }

    public void deleteAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {account.getUuid()};
        db.delete(Account.TABLENAME, Account.UUID + "=?", args);
        XmppConnectionService.takeDbBackup(context);
    }

    public boolean hasEnabledAccounts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(" + Account.UUID + ")  from "
                + Account.TABLENAME + " where not options & (1 <<1)", null);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return (count > 0);
        } catch (SQLiteCantOpenDatabaseException e) {
            return true; // better safe than sorry
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
        return db;
    }

    public void updateMessage(Message message) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {message.getUuid()};
        db.update(Message.TABLENAME, message.getContentValues(), Message.UUID
                + "=?", args);
        XmppConnectionService.takeDbBackup(context);
    }

    //[Birender] New method to maintain individual and group message delivery status into separate table "MessageStatus"
    public void updateMessageStatus(MessageStatus messageStatusNew) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

            String[] selectionArgs = {messageStatusNew.getMessageId(),
                    messageStatusNew.getRecipientJid().toString()};

        //There should be only ONE record with unique messageId, recipientId combination
            cursor = db.query(MessageStatus.TABLENAME, null,
                    MessageStatus.MESSAGE_ID + "=? AND " + MessageStatus.RECIPIENT_JID
                    + "=?", selectionArgs, null, null, null);

        if (cursor.getCount() == 0){
            //insert...
            db.insert(MessageStatus.TABLENAME, null, messageStatusNew.getContentValues());
        }
        else if (cursor.getCount() == 1){
            //update...
            cursor.moveToFirst();
            MessageStatus messageStatusOld = MessageStatus.fromCursor(cursor);

            if (!(messageStatusNew.getStatus() == Message.STATUS_SEND_FAILED
                    && (messageStatusOld.getStatus() == Message.STATUS_SEND_RECEIVED || messageStatusOld
                    .getStatus() == Message.STATUS_SEND_DISPLAYED))) {
                db.update(Message.TABLENAME, messageStatusNew.getContentValues(),
                        MessageStatus.MESSAGE_ID + "=? AND " + MessageStatus.RECIPIENT_JID
                                + "=?", selectionArgs);
            }
        }else{
            Log.wtf(TAG, "We found more than one records against MessageId, Recipient combination, find why");
        }

        cursor.close();
        XmppConnectionService.takeDbBackup(context);
    }


    public void readRoster(Roster roster) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        String args[] = {roster.getAccount().getUuid()};
        cursor = db.query(Contact.TABLENAME, null, Contact.ACCOUNT + "=?",
                args, null, null, null);
        while (cursor.moveToNext()) {
            roster.initContact(Contact.fromCursor(cursor));
        }
        cursor.close();
    }

    public void writeRoster(final Roster roster) {
        final Account account = roster.getAccount();
        final SQLiteDatabase db = this.getWritableDatabase();
        for (Contact contact : roster.getContacts()) {
            if (contact.getOption(Contact.Options.IN_ROSTER)) {
                db.insert(Contact.TABLENAME, null, contact.getContentValues());
            } else {
                String where = Contact.ACCOUNT + "=? AND " + Contact.JID + "=?";
                String[] whereArgs = {account.getUuid(), contact.getJid().toString()};
                db.delete(Contact.TABLENAME, where, whereArgs);
            }
        }
        account.setRosterVersion(roster.getVersion());
        updateAccount(account);
        XmppConnectionService.takeDbBackup(context);
    }


    public void deleteMessagesInConversation(Conversation conversation) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = {conversation.getUuid()};
        db.delete(Message.TABLENAME, Message.CONVERSATION + "=?", args);
        XmppConnectionService.takeDbBackup(context);
    }


    public static Cursor getParentKidInfo(Context context, String parentJid, int studentId) {
        //int studentId = 6; //This will come in the xmpp message packet. //TODO:get it from packet
        Uri mQueryUri = PTAppContract.Parent.CONTENT_PARENT_RELATION_STUDENT_URI;
        String selection = PTAppDatabase.Tables.PARENT + "." + PTAppContract.Parent.JID + "=? AND "
                + PTAppDatabase.Tables.PARENT_CHILD_RELATION + "." + PTAppContract.ParentChildRelation.STUDENT_ID
                + "=?";
        String[] args = new String[]{String.valueOf(parentJid), String.valueOf(studentId)};
        Cursor c = context.getContentResolver().query(mQueryUri, ParentQuery.PROJECTION, selection, args, null);
        if (c.moveToFirst()) {
            return c;
        } else {
            return null;
        }
    }

    public static Cursor getGroupRecord(Context context, String jid) {
        Uri groupUri = PTAppContract.DefaultGroups.CONTENT_URI;
        String selection = PTAppContract.DefaultGroups.JID + "=?";
        String[] args = {jid};
        Cursor c = context.getContentResolver().query(groupUri,null,selection,args,null);
        if(c.moveToFirst()){
            return c;
        }
        return null;
    }


    public static String getStaffName(Context context, String staffJid) {
        Uri staffUri = PTAppContract.Staff.CONTENT_URI;
        String selection = PTAppContract.Staff.JID + "=?";
        String[] args = {staffJid};
        Cursor c = context.getContentResolver().query(staffUri, StaffQuery.PROJECTION, selection, args, null);
        if (c.moveToFirst()) {
            return c.getString(StaffQuery.FIRST_NAME);
        } else {
            return "";
        }
    }

    private interface ParentQuery {
        //int _TOKEN = 0x1;
        String[] PROJECTION = {
                PTAppDatabase.Tables.PARENT + "." + PTAppContract.Parent.FIRST_NAME,
                PTAppContract.ParentChildRelation.PARENT_TYPE,
                PTAppDatabase.Tables.STUDENT + "." + PTAppContract.Student.FIRST_NAME,
                /*PTAppDatabase.Tables.STUDENT + "." + PTAppContract.Student.LAST_NAME,*/
        };

        int PAR_FIRST_NAME = 0;
        int PARENT_TYPE = 1;
        int STU_FIRST_NAME = 2;
        int STU_LAST_NAME = 3;


    }

    private interface StaffQuery {
        //int _TOKEN = 0x1;
        String[] PROJECTION = {
                PTAppDatabase.Tables.STAFF + "." + PTAppContract.Staff.FIRST_NAME
        };

        int FIRST_NAME = 0;
    }
}
