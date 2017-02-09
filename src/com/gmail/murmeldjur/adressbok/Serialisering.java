package com.gmail.murmeldjur.adressbok;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ola Karlsson on 2016-11-24.
 */
public class Serialisering {
    private static final Logger log = Logger.getLogger(Serialisering.class.getName());

    public static ArrayList<Contact> initSerialization() {
        ArrayList<Contact> contacts = null;
        //Check if file exists
        File f = new File("contacts.data");

        if (f.isFile() && f.canRead()) {
            try {
                FileInputStream fileIn = new FileInputStream("contacts.data");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                contacts = (ArrayList<Contact>) in.readObject();
                in.close();
                fileIn.close();
            } catch (FileNotFoundException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": FileNotFoundException caught", e);
            } catch (IOException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": IOException caught", e);
            } catch (ClassNotFoundException e) {
                log.log(Level.SEVERE, Thread.currentThread().getName() + ": ClassNotFoundException caught", e);
            }
        }
        return contacts;
    }

    public synchronized static void saveToSer(ArrayList<Contact> c) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream("contacts.data")) {
            try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(c);
            }
        }
    }
}
