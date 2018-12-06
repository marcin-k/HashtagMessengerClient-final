package com.marcin.hashtagmessengerclient.contacts;
/**************************************************************
 * Represents user entity stored in the list of contacts
 * used by child and parent user
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import com.marcin.hashtagmessengerclient.chat.Message;

import java.util.ArrayList;

public class Contact {
    private String username;
    private String firstName;
    private String lastName;
    private Long contactId;
    private ArrayList<Message> msgsExchanged;

//------------------------------Constructor---------------------------------------------------------
    public Contact(String username) {
        this.username = username;
    }

//    public Contact(String username, Long contactId){
//        this.username = username;
//        this.contactId = contactId;
//        msgsExchanged = new ArrayList<>();
//    }


    public Contact(String username, String firstName, String lastName, Long contactId) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactId = contactId;
        msgsExchanged = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Message> getMsgsExchanged() {
        return msgsExchanged;
    }
    public void addMessage(Message m){
        msgsExchanged.add(m);
    }


    public Long getContactId() {
        return contactId;
    }
}
