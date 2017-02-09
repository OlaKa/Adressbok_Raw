package com.gmail.murmeldjur.adressbok;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Ola on 2016-12-01.
 */
public class LoadCentralCatalogue implements Runnable {
    private static final Logger log = Logger.getLogger(LoadCentralCatalogue.class.getName());
    private Adressbok adressbok;
    private String ipadress;
    private int port;

    public LoadCentralCatalogue(Adressbok adressbok, String ipadress, int port) {
        this.adressbok = adressbok;
        this.ipadress = ipadress;
        this.port = port;
    }

    @Override
    public void run() {
        log.info("Connecting to remote address server");

        try (Socket socket = new Socket(ipadress, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println("getall");
            writer.flush();

            for (String line = reader.readLine(); !line.equals(""); line = reader.readLine()) {
                adressbok.loadRemoteContacts(line);
            }
            writer.println("exit");
            writer.flush();

        } catch (ConnectException e) {
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": ConnectException caught in thread", e);
            System.err.println("Kan inte ansluta till serverporten: '" + port + "' på adressen: '" + ipadress + "'");
        } catch (UnknownHostException e) {
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": UnknownHostException caught in thread", e);
            System.err.println("Kan inte kontakta servern med adressen: '" + ipadress + "' Okänd host!");
        } catch (Exception e) {
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": Exception caught in thread", e);
            System.err.println("Ett fel uppstod under anslutning till servern");
        }
    }
}
