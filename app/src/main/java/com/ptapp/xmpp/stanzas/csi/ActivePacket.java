package com.ptapp.xmpp.stanzas.csi;

import com.ptapp.xmpp.stanzas.AbstractStanza;

public class ActivePacket extends AbstractStanza {
    public ActivePacket() {
        super("active");
        setAttribute("xmlns", "urn:xmpp:csi:0");
    }
}
