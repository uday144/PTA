package com.ptapp.xmpp;

import com.ptapp.entities.Account;
import com.ptapp.xmpp.stanzas.MessagePacket;


public interface OnMessagePacketReceived extends PacketReceived {
    public void onMessagePacketReceived(Account account, MessagePacket packetz);
}
