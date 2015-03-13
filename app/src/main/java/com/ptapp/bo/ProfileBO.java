package com.ptapp.bo;

import com.ptapp.bean.MessagesBean;
import com.ptapp.bean.MsgProfileBean;

import java.util.ArrayList;

/**
 * Stores list of Profiles.
 */
public class ProfileBO {


    private ArrayList<MsgProfileBean> lstPrfs;
    private ArrayList<MessagesBean> lstMsgs;


    public ProfileBO() {

    }

    public ArrayList<MsgProfileBean> getLstPrfs() {
        return lstPrfs;
    }

    public void setLstPrfs(ArrayList<MsgProfileBean> lstPrfs) {
        this.lstPrfs = lstPrfs;
    }

    public ArrayList<MessagesBean> getLstMsgs() {
        return lstMsgs;
    }

    public void setLstMsgs(ArrayList<MessagesBean> lstMsgs) {
        this.lstMsgs = lstMsgs;
    }

}
