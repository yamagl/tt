package com.abchina.tt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CountDown implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(CountDown.class);
    private volatile boolean stop = false;
    private final long sleepMillis;
    private final String warning;

    public CountDown(long sleepMillis, String warning) {
        this.sleepMillis = sleepMillis;
        this.warning = warning;
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(sleepMillis);
            if (!stop) {
                logger.warn(warning);
                System.exit(-1);
            } else {
                logger.debug("task completed");
            }
        } catch (InterruptedException ignored) { }
    }
}
