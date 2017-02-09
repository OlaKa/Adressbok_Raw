package com.gmail.murmeldjur.adressbok;

import java.io.Serializable;
import java.util.UUID;

/**
 * Contact class to store contacts
 *
 * @author Ola Karlsson
 */
public class Contact implements Serializable, Comparable<Contact> {
    private String firstName;
    private String lastName;
    private String mail;
    private UUID uuid;

    public Contact(String firstName, String lastName, String mail, UUID uuid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMail() {
        return mail;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isContactFound(Contact person, String searchPattern) {
        if (person.getFirstName().toLowerCase().startsWith(searchPattern.toLowerCase()) ||
                person.getLastName().toLowerCase().startsWith(searchPattern.toLowerCase())) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Contact name) {
        return firstName.toLowerCase().compareTo(name.firstName.toLowerCase());
    }
}
