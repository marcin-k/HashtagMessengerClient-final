package com.marcin.hashtagmessengerclient.model;
/**************************************************************
 * Base representation of parent user stored modeled on
 * Spring boot server
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import com.marcin.hashtagmessengerclient.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class ParentUser extends BaseUser{
    private String emailAddress;
    private List<ChildUser> children;

    public ParentUser(String userName, String firstName, String lastName,  String password, String emailAddress) {
        super(userName, firstName, lastName, 0l, new ArrayList<BaseUser>(), new ArrayList<Message>(), password);
        this.emailAddress = emailAddress;
        this.children = new ArrayList<>();
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<ChildUser> getChildren() {
        return children;
    }

    public void setChildren(List<ChildUser> children) {
        this.children = children;
    }
}
