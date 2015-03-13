package com.ptapp.xmpp;

import com.ptapp.entities.Contact;

public interface OnContactStatusChanged {
    public void onContactStatusChanged(final Contact contact, final boolean online);
}
