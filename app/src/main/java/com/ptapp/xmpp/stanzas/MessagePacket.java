package com.ptapp.xmpp.stanzas;

import com.ptapp.xml.Element;
import com.ptapp.utils.CommonConstants;

public class MessagePacket extends AbstractStanza {

    public static final int TYPE_CHAT = 0;
    public static final int TYPE_NORMAL = 2;
    public static final int TYPE_GROUPCHAT = 3;
    public static final int TYPE_ERROR = 4;
    public static final int TYPE_HEADLINE = 5;

    public MessagePacket() {
        super(CommonConstants.M_MESSAGE);
    }

    public String getBody() {
        Element body = this.findChild(CommonConstants.M_BODY);
        if (body != null) {
            return body.getContent();
        } else {
            return null;
        }
    }

    public void setBody(String text) {
        this.children.remove(findChild(CommonConstants.M_BODY));
        Element body = new Element(CommonConstants.M_BODY);
        body.setContent(text);
        this.children.add(body);
    }

    /*public int getGroupId() {
        Element customElement = this.findChild(CommonConstants.M_CUSTOM);
        String groupid = customElement.getAttribute(CommonConstants.M_GROUP_ID);
        if (groupid != null) {
            return Integer.parseInt(groupid);
        } else {
            return -1;
        }
    }*/

    public String getGroupJid() {
        Element customElement = this.findChild(CommonConstants.M_CUSTOM);
        String groupjid = customElement.getAttribute(CommonConstants.M_GROUP_JID);
        if (groupjid != null) {
            return groupjid;
        } else {
            return null;
        }
    }

    public int getStudentId() {
        Element customElement = this.findChild(CommonConstants.M_CUSTOM);
        String stuId = customElement.getAttribute(CommonConstants.M_STUDENT_ID);
        if (stuId != null) {
            return Integer.parseInt(stuId);
        } else {
            return -1;
        }
    }

    public boolean getIsBroadcast() {
        Element customElement = this.findChild(CommonConstants.M_CUSTOM);
        String bcId = customElement.getAttribute(CommonConstants.M_IS_BROADCAST);

        if (bcId != null && (1 == Integer.parseInt(bcId))) {
            return true;
        } else {
            return false;
        }
    }


    public void setCustomElement(String groupJid, boolean isBroadcast, int studentId) {

        // Insert group JID
        Element customElement = new Element(CommonConstants.M_CUSTOM);

//        if (groupId >=0 ) {
//            customElement.setAttribute(CommonConstants.M_GROUP_ID, groupId);
//        }

        // Insert group JID
        customElement.setAttribute(CommonConstants.M_GROUP_JID, groupJid);

        // Insert broadcast indicator only when user sends broadcast message, else don't insert element at all (keep payload size minimal)
        if (isBroadcast) {
            customElement.setAttribute(CommonConstants.M_IS_BROADCAST, "1");
        }

        if (studentId > 0) {
            customElement.setAttribute(CommonConstants.M_STUDENT_ID, studentId);
        }

        this.children.add(customElement);
    }

    public void setType(int type) {
        switch (type) {
            case TYPE_CHAT:
                this.setAttribute("type", "chat");
                break;
            case TYPE_GROUPCHAT:
                this.setAttribute("type", "groupchat");
                break;
            case TYPE_NORMAL:
                break;
            default:
                this.setAttribute("type", "chat");
                break;
        }
    }

    public int getType() {
        String type = getAttribute("type");
        if (type == null) {
            return TYPE_NORMAL;
        } else if (type.equals("normal")) {
            return TYPE_NORMAL;
        } else if (type.equals("chat")) {
            return TYPE_CHAT;
        } else if (type.equals("groupchat")) {
            return TYPE_GROUPCHAT;
        } else if (type.equals("error")) {
            return TYPE_ERROR;
        } else if (type.equals("headline")) {
            return TYPE_HEADLINE;
        } else {
            return TYPE_NORMAL;
        }
    }
}