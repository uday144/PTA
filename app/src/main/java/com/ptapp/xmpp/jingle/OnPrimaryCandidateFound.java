package com.ptapp.xmpp.jingle;

public interface OnPrimaryCandidateFound {
    public void onPrimaryCandidateFound(boolean success,
                                        JingleCandidate canditate);
}
