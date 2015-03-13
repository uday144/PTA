package com.ptapp.xmpp.jingle;

import com.ptapp.entities.DownloadableFile;

public interface OnFileTransmissionStatusChanged {
    public void onFileTransmitted(DownloadableFile file);

    public void onFileTransferAborted();
}
