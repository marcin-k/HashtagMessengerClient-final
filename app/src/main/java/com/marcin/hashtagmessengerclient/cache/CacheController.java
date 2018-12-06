package com.marcin.hashtagmessengerclient.cache;
/**************************************************************
 * Class used to build a local cache of users ids and their
 * first names to prevent multiple lookups (in parent home screen)
 * Singleton controller
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class CacheController {

    //singleton constructs
    private static final CacheController ourInstance = new CacheController();
    private ArrayList<CachedUser> cachedFirstNames;
    private List<Contact> childUsers;

    public static CacheController getInstance() {
        return ourInstance;
    }

    private CacheController() {
        cachedFirstNames = parseUsers(ServerController.getInstance().getCache());
        childUsers = ServerController.getInstance().getChildren(ServerController.getInstance().getId());
    }

    //converts the string of users and ids return from the server to simple array of Cached Users
    private ArrayList<CachedUser> parseUsers(String str){
        ArrayList<CachedUser> users = new ArrayList<>();
        String [] usersArray = str.split(",");
        for (int i =0; i< usersArray.length; i=i+2){
            CachedUser c = new CachedUser(Long.parseLong(usersArray[i]), usersArray[i+1]);
            users.add(c);
            Log.v("MyLog","Caching user -"+Long.parseLong(usersArray[i])+" -"+ usersArray[i+1]);
        }
        return users;
    }

    //initialize the array of children
    public void initChildren(){
        childUsers = ServerController.getInstance().getChildren(ServerController.getInstance().getId());
    }

    public List<Contact> getChildren(){
        return this.childUsers;
    }

    //returns a firs name from cache if the id is not in the cache checks the server
    //and caches the user for any further queries
    public String getFirstName(Long id){
        String firstName = "";
        for (CachedUser c : cachedFirstNames){
            if (c.getId()==id){
                Log.v("MyLog","getting first name from cache");
                return c.getFirstName();
            }
        }
        Log.v("MyLog","calling server for firs name");
        ChildUser c = ServerController.getInstance().getChild(id);
        //adding to cache
        cachedFirstNames.add(new CachedUser(c.getId(), c.getFirstName()));
        return c.getFirstName();
    }
}
