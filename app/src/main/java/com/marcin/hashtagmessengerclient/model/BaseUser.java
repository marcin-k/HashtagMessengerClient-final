package com.marcin.hashtagmessengerclient.model;
/**************************************************************
 * Base representation of base user stored modeled on
 * Spring boot server
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import com.marcin.hashtagmessengerclient.chat.Message;

import java.util.List;

public class BaseUser {
    private String userName;
    private String firstName;
    private String lastName;
    private  Long id;
    private List<BaseUser> contacts;
    private List<Message> messages;
    private String password;

    public BaseUser(String userName, String firstName, String lastName, Long id, List<BaseUser> contacts, List<Message> messages, String password) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.contacts = contacts;
        this.messages = messages;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BaseUser> getContacts() {
        return contacts;
    }

    public void setContacts(List<BaseUser> contacts) {
        this.contacts = contacts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
