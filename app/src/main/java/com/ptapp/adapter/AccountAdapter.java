package com.ptapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptapp.activity.XmppActivity;
import com.ptapp.entities.Account;
import com.ptapp.app.R;

import java.util.List;

public class AccountAdapter extends ArrayAdapter<Account> {

    private XmppActivity activity;

    public AccountAdapter(XmppActivity activity, List<Account> objects) {
        super(activity, 0, objects);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Account account = getItem(position);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.account_row, parent, false);
        }
        TextView jid = (TextView) view.findViewById(R.id.account_jid);
        jid.setText(account.getJid().toBareJid().toString());
        TextView statusView = (TextView) view.findViewById(R.id.account_status);
        ImageView imageView = (ImageView) view.findViewById(R.id.account_image);
        //SCH: no need
//        imageView.setImageBitmap(activity.avatarService().get(account,
//                activity.getPixel(48)));
        statusView.setText(getContext().getString(account.getStatus().getReadableId()));
        switch (account.getStatus()) {
            case ONLINE:
                statusView.setTextColor(activity.getPrimaryColor());
                break;
            case DISABLED:
            case CONNECTING:
                statusView.setTextColor(activity.getSecondaryTextColor());
                break;
            default:
                statusView.setTextColor(activity.getWarningTextColor());
                break;
        }
        return view;
    }
}
