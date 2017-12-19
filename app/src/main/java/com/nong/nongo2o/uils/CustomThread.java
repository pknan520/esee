package com.nong.nongo2o.uils;

/**
 * Created by Administrator on 2017-12-16.
 */

public class CustomThread extends Thread{

    @Override
    public void run() {
        super.run();
        if (listener != null) {
            listener.run();
        }
    }

    public void cancel() {
        interrupt();
    }

    private Listener listener;

    public void addListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        public void run();
    }
}
