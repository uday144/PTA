package com.ptapp.xmpp.jingle;

import com.ptapp.entities.Account;
import com.ptapp.xmpp.PacketReceived;
import com.ptapp.xmpp.jingle.stanzas.JinglePacket;


public interface OnJinglePacketReceived extends PacketReceived {
    public void onJinglePacketReceived(Account account, JinglePacket packet);
}
