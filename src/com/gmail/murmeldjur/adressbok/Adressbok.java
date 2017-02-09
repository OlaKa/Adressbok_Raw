package com.gmail.murmeldjur.adressbok;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Adressbok {
    private static final Logger log = Logger.getLogger(Adressbok.class.getName());
    private ArrayList<Contact> contacts = new ArrayList<>();
    private Hashtable<UUID,Contact> remoteContacts = new Hashtable<>();
    private Serialisering ser = new Serialisering();


    private UUID generateUUID() {
        UUID idOne = UUID.randomUUID();
        return idOne;
    }

    private boolean searchForContactsRemote(String svar) {
        String uuids;
        for (UUID key : remoteContacts.keySet()) {
            uuids = key.toString();
            if (uuids.equals(svar))
                return true;
        }
        return false;
    }

    public void quitSession(String svar) {
        String[] splitString = svar.split(" ");
        log.info("Trying to exit program...");
        if ("quit".equals(svar.trim())) {
            quitSession();
        } else if ((!"quit".equals(splitString[0].trim()) && splitString.length > 1) ||
                (!"quit".equals(splitString[0].trim()) && splitString.length == 1)) {
            displayFaultyInput(splitString[0]);
        } else {
            System.err.println("Felaktigt antal inparametrar. Vill du avsluta skriv 'quit'");
            log.info("Wrong number of parameters was given");
        }
    }

    public void quitSession() {
        try {
            saveToSer();
            System.out.println("Avslutar program! Alla kontakter är sparade!");
            log.info("Exiting program!");
            System.exit(0);
        } catch (IOException e) {
            log.log(Level.SEVERE, Thread.currentThread().getName() + ": IOException caught", e);
        }
    }

    public void initSer() {
        log.info("Checking and loading saved contacts");
        ArrayList<Contact> temp;
        temp = ser.initSerialization();
        if (temp != null)
            contacts = temp;
    }

    public void saveToSer() throws IOException {
        if (contacts != null) {
            log.info(Thread.currentThread().getName() + ": Saving contacts to file");
            ser.saveToSer(contacts);
        }
    }

    public void addContact(String svar) throws IllegalFormatException, IOException {
        String[] splittedString = svar.split(" ");
        log.fine("Trying to add a contact...");
        if (!"add".equals(splittedString[0].trim())) {
            displayFaultyInput(splittedString[0].trim());
            return;
        }

        if (splittedString.length == 4) {
            contacts.add(new Contact(splittedString[1], splittedString[2], splittedString[3], generateUUID()));
            System.out.println("Kontakt har lagts till!");
            log.fine("Adding user: " + splittedString[1] + " " + splittedString[2]);
        } else {
            System.err.println("Felaktigt antal inparametrar!");
            log.fine("Wrong number of parameters was given");
        }
    }

    public void deleteContact(String svar) {
        String[] splittedString = svar.split(" ");
        log.fine("Trying to remove contact...");

        if (!"delete".equals(splittedString[0].trim())) {
            displayFaultyInput(splittedString[0].trim());
        } else if (splittedString.length == 2) {
            int i = 0;
            boolean answer = searchForContactsRemote(splittedString[1].trim());
            if (answer) {
                System.err.println("Kontakten kan inte tas bort");
                return;
            }
            for (Contact t : contacts) {
                String suuid = t.getUuid().toString();
                if (suuid.trim().equals(splittedString[1].trim())) {
                    contacts.remove(i);
                    log.fine("Deleting contact with id: " + splittedString[1]);
                    System.out.println("Kontakt har tagits bort!");
                    return;
                }
                i++;
            }
            System.err.println("Ingen kontakt med det Id:t hittades");
            log.fine("Removing contact with id " + splittedString[1] + " failed");
        } else {
            System.err.println("Felaktigt antal inparametrar.");
            log.fine("Wrong number of parameters was given");
        }
    }

    public void listContacts(String svar) {
        log.fine("Listing contacts");
        String[] splittedList = svar.split(" ");
        ArrayList<Contact> tempArray = new ArrayList<>();
        tempArray.addAll(remoteContacts.values());
        tempArray.addAll(contacts);
        if ("list".equals(svar.trim()) && tempArray.isEmpty() && splittedList.length == 1) {
            System.err.println("Inga kontakter sparade");
            return;
        }

        if ((!"list".equals(svar) && splittedList.length == 1) ||
                (!"list".equals(splittedList[0]) && splittedList.length > 1)) {
            displayFaultyInput(splittedList[0]);
        } else if ("list".equals(svar) && splittedList.length == 1) {
            Collections.sort(tempArray);
            for (Contact cont : tempArray) {
                System.out.println("Id: " + cont.getUuid());
                System.out.println("Förnamn: " + cont.getFirstName());
                System.out.println("Efternamn: " + cont.getLastName());
                System.out.println("E-postadress: " + cont.getMail() + "\n");
            }
        } else {
            System.err.println("Felaktigt antal inparametrar. Om du vill lista dina kontakter skriv 'list'");
            log.fine("Wrong number of parameters was given");
        }
    }

    public void searchContacts(String svar) {
        log.fine("Searching contacts");
        String[] split = svar.split(" ");
        ArrayList<Contact> tempArray = new ArrayList<>();
        boolean found = false;

        if (!"search".equals(split[0].trim()) && split.length == 2) {
            displayFaultyInput(split[0]);
            return;
        } else if (split.length == 2) {
            tempArray.addAll(remoteContacts.values());
            tempArray.addAll(contacts);
            Collections.sort(tempArray);
            for (Contact cont : tempArray) {
                if (cont.isContactFound(cont, split[1])) {
                    found = true;
                    System.out.println("Id: " + cont.getUuid());
                    System.out.println("Förnamn: " + cont.getFirstName());
                    System.out.println("Efternamn: " + cont.getLastName());
                    System.out.println("E-postadress: " + cont.getMail() + "\n");
                }
            }
        } else {
            System.err.println("Felaktigt antal inparametrar");
            log.fine("Wrong number of parameters was given");
        }

        if (!found && split.length == 2 && "search".equals(split[0].trim())) {
            System.err.println("Inga kontakter med namnet '" + split[1] + "' hittades");
            log.fine("No contact(s) with name " + split[1] + " was found");
        }
    }

    public static void displayHelpMenu(String svar) {
        String[] splitString = svar.split(" ");
        log.fine("Displaying help menu");
        if ("help".equals(svar.trim()) && splitString.length == 1) {
            System.out.println("add\t\t Lägger till ny kontakt");
            System.out.println("delete\t Tar bort kontakt");
            System.out.println("list\t Listar alla kontakter");
            System.out.println("search\t Sök bland kontakterna");
            System.out.println("quit\t Avslutar programmet");
        } else if (!"help".equals(splitString[0])) {
            displayFaultyInput(splitString[0]);
        } else {
            System.err.println("Felaktigt antal inparametrar. Vill du ha hjälp skriv 'help'");
            log.fine("Illegal number of arguments");
        }
    }

    public static void displayFaultyInput(String svar) {
        log.fine("User had faulty input parameter");
        String[] split = svar.split(" ");
        System.err.println("Ogiltigt kommando " + "\'" + split[0] + "\'");
    }

    public synchronized void loadRemoteContacts(String contact) {
        if (contact != null) {
            String[] splittedString = contact.split(" ");
            remoteContacts.put(UUID.fromString(splittedString[0]), new Contact(splittedString[1], splittedString[2],
                    splittedString[3], UUID.fromString(splittedString[0])));
        } else {
            System.err.println("Kunde inte hämta kontakter");
        }
    }
}





