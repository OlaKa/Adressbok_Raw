package com.gmail.murmeldjur.adressbok;

import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ola on 2016-11-23.
 */
public class MainAdressbok {
    private static final Logger log = Logger.getLogger(MainAdressbok.class.getName());
    private static Scanner sc = new Scanner(System.in);
    private static Adressbok adressbok = new Adressbok();
    private static Logging logProgram = new Logging();
    private static Map<String,Integer> servers = new HashMap<>();

    private static final int THREAD_POLLING_INTERVAL = 5000;
    private static String svar;

    public static void main(String[] args) {
        Thread.currentThread().setName("Main");
        logProgram.setupLogging();
        Thread autosave = new Thread(new AutoSaveContacts(adressbok, THREAD_POLLING_INTERVAL));
        autosave.start();
        //Add as many servers as you want..
        servers.put("localhost",61616);
        try {
            log.info("Starting system...");
            adressbok.initSer();
            Thread pool = new Thread(new ThreadPool(adressbok,servers));
            pool.start();
            setUI();
        } catch (NoSuchElementException e) {
            //Handle Ctrl + D
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": NoSuchElementException caught", e);
            adressbok.quitSession();
        } catch (IOException e) {
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": IOException caught", e);
        } catch (IllegalArgumentException e){
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": IllegalArgumentException caught", e);
        }
    }

    private static void setUI() throws IOException, NoSuchElementException {
        log.info("Initializing User Interface");
        System.out.println("Welcome!");
        System.out.println("För hjälpmeny skriv 'help'");
        while (true) {
            try {
                svar = sc.nextLine();
                if (svar.startsWith("add")) {
                    adressbok.addContact(svar);
                } else if (svar.startsWith("delete")) {
                    adressbok.deleteContact(svar);
                } else if (svar.startsWith("list")) {
                    adressbok.listContacts(svar);
                } else if (svar.startsWith("help")) {
                    adressbok.displayHelpMenu(svar);
                } else if (svar.startsWith("search")) {
                    adressbok.searchContacts(svar);
                } else if (svar.startsWith("quit")) {
                    adressbok.quitSession(svar);
                } else {
                    adressbok.displayFaultyInput(svar);
                }
            } catch (MalformedParametersException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": Malformed parameters", e);
            }
        }
    }
}
