package com.marcin.hashtagmessengerclient.model;
/**************************************************************
 * Base representation of child user stored modeled on
 * Spring boot server
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import com.marcin.hashtagmessengerclient.chat.Message;

import java.util.List;

public class ChildUser extends BaseUser{

    private List<ParentUser> parents;
    //private List<NewContactRequest> requests;
    //enable to search and add new contact without parent approval
    private boolean canAddNewContacts;
    //enable to be found by other users without parent consent
    private boolean canBeFound;
    //enables child to received filtered messages from strangers (users not on child contact list)
    private boolean canReceiveMessageFromNonConctact;

    private int dailyAllowance;

    public ChildUser(String userName, String firstName, String lastName, Long id, List<BaseUser>
            contacts, List<Message> messages, String password, List<ParentUser> parents, boolean
            canAddNewContacts, boolean canBeFound, boolean canReceiveMessageFromNonConctact,
                     int dailyAllowance) {

        super(userName, firstName, lastName, id, contacts, messages, password);
        this.parents = parents;
        this.canAddNewContacts = canAddNewContacts;
        this.canBeFound = canBeFound;
        this.canReceiveMessageFromNonConctact = canReceiveMessageFromNonConctact;
        this.dailyAllowance = dailyAllowance;
    }



    public int getDailyAllowance() {
        return dailyAllowance;
    }

    public void setDailyAllowance(int dailyAllowance) {
        this.dailyAllowance = dailyAllowance;
    }

    public List<ParentUser> getParents() {
        return parents;
    }

    public void setParents(List<ParentUser> parents) {
        this.parents = parents;
    }

    public boolean isCanAddNewContacts() {
        return canAddNewContacts;
    }

    public void setCanAddNewContacts(boolean canAddNewContacts) {
        this.canAddNewContacts = canAddNewContacts;
    }

    public boolean isCanBeFound() {
        return canBeFound;
    }

    public void setCanBeFound(boolean canBeFound) {
        this.canBeFound = canBeFound;
    }

    public boolean isCanReceiveMessageFromNonConctact() {
        return canReceiveMessageFromNonConctact;
    }

    public void setCanReceiveMessageFromNonConctact(boolean canReceiveMessageFromNonConctact) {
        this.canReceiveMessageFromNonConctact = canReceiveMessageFromNonConctact;
    }
}
