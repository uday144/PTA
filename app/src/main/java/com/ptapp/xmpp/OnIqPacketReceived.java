package com.ptapp.xmpp;

import com.ptapp.entities.Account;
import com.ptapp.xmpp.stanzas.IqPacket;

/**
 * Created by lifestyle on 30-01-15.
 */
public interface OnIqPacketReceived extends PacketReceived {
    public void onIqPacketReceived(Account account, IqPacket packet);
}
