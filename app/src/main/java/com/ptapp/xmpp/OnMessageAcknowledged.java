package com.ptapp.xmpp;

import com.ptapp.entities.Account;


public interface OnMessageAcknowledged {
    public void onMessageAcknowledged(Account account, String id);
}
