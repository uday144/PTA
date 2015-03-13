package com.ptapp.xmpp.jingle.stanzas;

import com.ptapp.xml.Element;

public class Reason extends Element {
    private Reason(String name) {
        super(name);
    }

    public Reason() {
        super("reason");
    }
}
