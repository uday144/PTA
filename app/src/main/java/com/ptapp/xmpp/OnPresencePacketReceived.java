package com.ptapp.xmpp;

import com.ptapp.entities.Account;
import com.ptapp.xmpp.stanzas.PresencePacket;


public interface OnPresencePacketReceived extends PacketReceived {
    public void onPresencePacketReceived(Account account, PresencePacket packet);
}
