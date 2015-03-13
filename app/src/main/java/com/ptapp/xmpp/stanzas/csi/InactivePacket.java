package com.ptapp.xmpp.stanzas.csi;

import com.ptapp.xmpp.stanzas.AbstractStanza;

public class InactivePacket extends AbstractStanza {
    public InactivePacket() {
        super("inactive");
        setAttribute("xmlns", "urn:xmpp:csi:0");
    }
}
