package com.marcin.hashtagmessengerclient.serverConnectivity;
/**************************************************************
 * Activity used for all contact (user) releated queries,
 * Functions defined here include:
 *  - adding user to conact list
 *  - getting list of children for user (for simplicity they are stored as contact on client side)
 *  - getting search results
 *  - getting list of contacts
 *
 *  Lists are paresed using the parser methods
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.util.Log;

import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.contacts.SearchActivity;
import com.sendbird.android.shadow.okhttp3.Call;
import com.sendbird.android.shadow.okhttp3.Callback;
import com.sendbird.android.shadow.okhttp3.Credentials;
import com.sendbird.android.shadow.okhttp3.OkHttpClient;
import com.sendbird.android.shadow.okhttp3.Request;
import com.sendbird.android.shadow.okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ContactsAndChildrenParser {

    private static String addContactReturn = "";

//------------------------------ Add Contact -------------------------------------------------------
    public String addContact(Long id){
        addContactReturn = "";
        String url = ServerController.getInstance().getIp()+"/api/contacts/new?contactId="+id+"&userId=" +
                ServerController.getInstance().getId();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
        Call call = client.newCall(request);
        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer kids");
                try {
                    String jsonData = response.body().string();
//                    Log.v("MyLog", "json data-"+jsonData);

                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        addContactReturn = jsonData;
                    } else {
                        Log.e("MyLog", "Exception caught 1 - Contact Parser");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught 2", e);
                }
            }
        });

        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return addContactReturn;
    }
//---------------------------- Gets Children -------------------------------------------------------
    public List<Contact> getChildren(Long id){

        String url = ServerController.getInstance().getIp()+"/api/parent/read?parentId=" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
        Log.v("MyLog", "id-"+id);
        Call call = client.newCall(request);
        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer kids");
                try {
                    String jsonData = response.body().string();
                    Log.v("MyLog", "json data-"+jsonData);

                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        try {
                            if (jsonData.length() > 4) {
                                ServerController.getInstance().setKids(parseObject(jsonData));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("MyLog", "Exception caught 1 - Contact Parser");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught 2", e);
                }
            }
        });

        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerController.getInstance().kids;
    }
//---------------------------- Gets Search results -------------------------------------------------
    public List<Contact> getSearch(String s){
        String url = ServerController.getInstance().getIp()+"/api/contacts/search?username=" + s;
//
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
        Call call = client.newCall(request);

        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer contacts");
                try {
                    String jsonData = response.body().string();
                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        try {
                            if (jsonData.length() > 4) {
                                ServerController.getInstance().setSearch(parseObject(jsonData));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("MyLog", "Exception caught 1 - Contact Parser");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught 2", e);
                }
            }
        });

        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerController.getInstance().search;
    }

//---------------------------- Gets Contacts -------------------------------------------------------
    public List<Contact> getContacts(Long id){

        String url = ServerController.getInstance().getIp()+"/api/contacts/read?userId=" + id;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic(
                        ServerController.getInstance().getUsername(),
                        ServerController.getInstance().getPassword()))
                .build();
        Call call = client.newCall(request);

        //Enqueue - onResponse is executed as a call back once the 2nd thread is finished
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MyLog", "Exception caught 0", e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.v("MyLog", "On response from Contact and Children Parer contacts");
                try {
                    String jsonData = response.body().string();
                    //checks if the response was received successfully
                    if (response.isSuccessful()) {
                        try {
                            if (jsonData.length() > 4) {
                                ServerController.getInstance().setContacts(parseObject(jsonData));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("MyLog", "Exception caught 1 - Contact Parser");
                    }
                } catch (IOException e) {
                    Log.e("MyLog", "Exception caught 2", e);
                }
            }
        });

        //gives the other thread opportunity to finish up
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ServerController.getInstance().contacts;
    }

//----------------------------------- Parse JSON Objects -------------------------------------------
    private ArrayList<Contact> parseObject(String jsonData) throws JSONException{

        JSONArray data = new JSONArray(jsonData);
        ArrayList<Contact> contacts = new ArrayList<>();
        Log.v("MyLog", "---------------------------------------");
        Log.v("MyLog", "whole json"+jsonData);
        for(int i = 0; i<data.length(); i++){
            try {
                JSONObject jsonContact = data.getJSONObject(i);
                contacts.add(new Contact(jsonContact.getString("username"),
                        jsonContact.getString("firstName"),
                        jsonContact.getString("lastName"),
                        jsonContact.getLong("id")));
                Log.v("MyLog", "cotnact last name:"+jsonContact.getString("lastName"));
            }
            catch (JSONException e){
                Log.v("MyLog", "JSON Expected Exception in ContactParser" +i);
            }
        }
        return contacts;
    }
}
