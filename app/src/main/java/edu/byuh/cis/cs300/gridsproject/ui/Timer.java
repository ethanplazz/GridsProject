package edu.byuh.cis.cs300.gridsproject.ui;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class pumps out "timer" events at
 * regular intervals, so we can do animation.
 */
public class Timer extends Handler {

    private final List<TickListener> fans;

    public Timer() {
        fans = new ArrayList<>();
        sendMessageDelayed(obtainMessage(), 100);
    }

    public void register(TickListener t) {
        fans.add(t);
    }

    public void unregister(TickListener t) {
        fans.remove(t);
    }

    @Override
    public void handleMessage(@NonNull Message m) {
        for (TickListener b : fans) {
            b.onTick();
        }
        sendMessageDelayed(obtainMessage(), 30);
    }
}