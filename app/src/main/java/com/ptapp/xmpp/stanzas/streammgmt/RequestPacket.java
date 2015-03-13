package com.ptapp.xmpp.stanzas.streammgmt;

import com.ptapp.xmpp.stanzas.AbstractStanza;

public class RequestPacket extends AbstractStanza {

    public RequestPacket(int smVersion) {
        super("r");
        this.setAttribute("xmlns", "urn:xmpp:sm:" + smVersion);
    }
}
