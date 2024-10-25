package edu.byuh.cis.cs300.gridsproject.ui;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

public class TokenHandler extends Handler {

    public ArrayList<TickListener> tickListeners;

    public TokenHandler() {
        tickListeners = new ArrayList<>();
    }

    public void registerTickListener(TickListener tickListener) {
        tickListeners.add(tickListener);
    }

    public void deregisterTickListener(TickListener tickListener) {
        tickListeners.remove(tickListener);
    }

    @Override
    public void handleMessage(Message msg) {
        for (TickListener listener : tickListeners) {
            listener.onTick();
        }
        sendEmptyMessageDelayed(0, 30); // Adjust 30ms as needed for smoothness
    }
}