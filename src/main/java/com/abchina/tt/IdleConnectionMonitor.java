package com.abchina.tt;

import org.apache.http.conn.HttpClientConnectionManager;

public class IdleConnectionMonitor implements Runnable {
    private final HttpClientConnectionManager manager;

    public IdleConnectionMonitor(HttpClientConnectionManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000);
                manager.closeExpiredConnections();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
