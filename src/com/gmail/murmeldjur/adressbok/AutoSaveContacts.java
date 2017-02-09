package com.gmail.murmeldjur.adressbok;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ola on 2016-11-24.
 */
public class AutoSaveContacts implements Runnable {
    private static final Logger log = Logger.getLogger(AutoSaveContacts.class.getName());
    private Adressbok adressbok;
    private int pollinterval;

    public AutoSaveContacts(Adressbok adressbok, int pollinterval) {
        this.adressbok = adressbok;
        this.pollinterval = pollinterval;
    }

    @Override
    public void run() {
        log.info("Starting auto saver...");
        Thread.currentThread().setName("AutoSaver");
        while (true) {
            try {
                Thread.sleep(pollinterval);
                adressbok.saveToSer();
            } catch (InterruptedException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": InterruptedException caught in thread", e);
            } catch (IOException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": IOException caught in thread", e);
            }
        }
    }
}
