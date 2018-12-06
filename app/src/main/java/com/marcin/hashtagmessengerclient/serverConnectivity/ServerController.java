package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * This singleton controller is used for all interaction
 * between the client and the server. This class all other classes
 * defined in this package to fulfill all of the GET and POST
 * requests. All of the requests that returns lists are setting them
 * up in this class, the string/ int return values are typically
 * stored in the class that returns them
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/

import android.content.res.Resources;
import android.util.Log;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.model.ParentUser;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.System.getString;
import static java.lang.Thread.sleep;


public class ServerController {
    private static final ServerController ourInstance = new ServerController();

    public static ServerController getInstance() {
        return ourInstance;
    }

    private ServerController() {
        contactsAndChildrenParser = new ContactsAndChildrenParser();
        messagesParser = new MessagesParser();
        newUserCreator = new NewUserCreator();
        childUserParser = new ChildUserParser();
        cacheGetter = new CacheGetter();
        allowance = new Allowance();
        contacts = new ArrayList<>();
        kids = new ArrayList<>();
        search = new ArrayList<>();
        login = new Login();
        loginCheck = new LoginCheck();
        id = 0l;
        username = "";
        password = "";
    }
    //--------------------------------------------------------------------------------------------------
    //Variables
    //stores all contacts
    protected ArrayList<Contact> contacts;
    //stores all contacts
    protected ArrayList<Contact> kids;
    //stores temp list of search
    protected ArrayList<Contact> search;

    //stores all messages
    protected ArrayList<Message> messages;
    //stores all sos messages
    protected ArrayList<Message> sos_msgs;
    //potentially inappropriate messages to review
    protected ArrayList<Message> msgToReview;

    //logged in user ID
    private Long id;
    private String username;
    private String password;

    //classes used to get data
    private ContactsAndChildrenParser contactsAndChildrenParser;
    private MessagesParser messagesParser;
    private NewUserCreator newUserCreator;
    private Allowance allowance;
    private Login login;
    private ChildUserParser childUserParser;
    private LoginCheck loginCheck;
    private CacheGetter cacheGetter;

    //tries to log user in
    public String login(String username, String password){
        return login.login(username, password);
    }

    //sends msg to server
    public void sendMsg(Message msg){
        messagesParser.sendMsg(msg);
    }

    //creates a parent account
    public String registerParent(ParentUser p){
        return newUserCreator.registerParent(p);
    }

    //creates a new child
    public String createChild(ChildUser c){
        return newUserCreator.newChild(c);
    }

    //sets password for a user
    public String setPassword(Long id, String password){
        return newUserCreator.setPassword(id, password);
    }

    //add new contact
    public String addContact(Long id){
        return contactsAndChildrenParser.addContact(id);
    }

    //updates a child profile
    public String updateChild(ChildUser childUser){
        return childUserParser.update(childUser);
    }

    public void instantiateMessages(Long id){
        for(Message m : messages){
            try {
                if (m.getSenderId()== id){
                    getContactById(m.getRecipientId()).addMessage(m);
                }
                else if(m.getRecipientId()== id){
                    getContactById(m.getSenderId()).addMessage(m);
                }
            }
            catch (NullPointerException e){
                Log.v("MyLog", "user with id:"+id+" is not in the contact list");
            }
        }
    }

    //deletes message with the id
    public String deleteMsg(Long id){
        return messagesParser.deleteMsg(id);
    }

    //approves (for children) message with the id
    public String approveMsg(Long id){
        return messagesParser.approve(id);
    }

    //----------------------------------------Getters---------------------------------------------------
    public String getCache(){
        return cacheGetter.getCache();
    }

    public String getIp(){
        String ip = MyApplication.getAppContext().getString(R.string.ip_address_of_spring_server);
        return ip;
    }


    public boolean isLoginFree(String login){
        return loginCheck.isLoginFree(login);
    }

    public boolean isItaParent(Long id){
        return loginCheck.isItaParent(id);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Contact getContactById(Long id){
        for(Contact c: contacts){
            if (c.getContactId()==id) {
                return c;
            }
        }
        return null;
    }

    public String getAllowance(){
        return allowance.getAllowance(this.id);
    }

    //gets list of contacts for a user with a given id
    public List<Contact> getContacts(Long id){
        contacts.clear();
        return contactsAndChildrenParser.getContacts(id);
    }

    //gets list of children for parent with id (id)
    public List<Contact> getChildren(Long id){

        return contactsAndChildrenParser.getChildren(id);
    }

    //gets the list of search results
    public List<Contact> getSearch(String s){
        return contactsAndChildrenParser.getSearch(s);
    }

    //gets a child details with specific id
    public ChildUser getChild(Long id){
        return childUserParser.getChild(id);
    }

    //gets list of messages
    public List<Message> getMessages(Long id){
        return messagesParser.getMessages(id);
    }

    //get list of sos messages
    public List<Message> getSOS(Long id){
        return messagesParser.getSOS(id);
    }

    //get list of messages to review
    public List<Message> getMessagesToReview(Long id){
        return messagesParser.getMessagesToReview(id);
    }
    //----------------------------------------Setters---------------------------------------------------
    protected void setMessages(ArrayList<Message> messages){
        this.messages = messages;
    }
    //sets sos messegas array
    protected void setSOS(ArrayList<Message> sos_msgs){
        this.sos_msgs = sos_msgs;
    }
    //sets messages to review array
    protected void setMessagesToReview(ArrayList<Message> messagesToReview){
        this.msgToReview = messagesToReview;
    }

    //sets list of contacts, used by ContactAndChildrenParser
    protected void setContacts(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }
    //sets list of kids, used by ContactAndChildrenParser
    protected void setKids(ArrayList<Contact> kids){
        this.kids = kids;
    }
    //sets list of search contacts
    protected void setSearch(ArrayList<Contact> search){
        this.search = search;
    }
    //sets the list empty
    public void clearSearch(){
        search.clear();
    }
    //sets logged in user id
    public void setId(Long id){
        this.id = id;
    }
    //sets logged in user username
    public void setUsername(String username) {
        this.username = username;
    }
    //sets logged in user password
    public void setPassword(String password) {
        this.password = password;
    }

}
