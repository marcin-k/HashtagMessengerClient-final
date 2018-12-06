package com.marcin.hashtagmessengerclient.contacts;
/**************************************************************
 * Activity used to search for new contacts
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.ChatActivity;
import com.marcin.hashtagmessengerclient.child.ChildContactsActivity;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.parent.ParentContactsActivity;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends ListActivity implements View.OnTouchListener {

    boolean isItParent;
    private ImageView backToContacts;
    private EditText searchET;

    private List<Contact> searchList;
    private ListView listView;
    private ArrayAdapter<Contact> adapter;
    private static Long tempId = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        isItParent = getIntent().getExtras().getBoolean("isItParent");
        backToContacts = findViewById(R.id.backToContacts);
        searchET = findViewById(R.id.searchET);

        backToContacts.setOnTouchListener(this);

        searchList = new ArrayList<>();
        listView = this.getListView();
        //set ListView adapter first
        adapter = new ContactAdapter(this, R.layout.contact_item, searchList);
        listView.setAdapter(adapter);

        searchET.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) ){
                    clearList();
                    setSearchList(searchET.getText().toString());
                    return true;
                }
                return false;
            }
        });

        //on contact click prompts if the user should be added to list of cotnacts
        //verifies that user is not already in the list (on the server)
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MyApplication.getAppContext(), "id: "+searchList.get(position).getContactId(),
//                        Toast.LENGTH_LONG).show();
                SearchActivity.tempId = searchList.get(position).getContactId();
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Add new contact");
                builder.setMessage("You are about add "+searchList.get(position).getUsername()+" to your contact list, would you like to proceed ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fromServer = ServerController.getInstance().addContact(tempId);
                        if(fromServer.equalsIgnoreCase("added")){
                            Toast.makeText(getApplicationContext(), "Contact added!", Toast.LENGTH_SHORT).show();
                        }
                        else if(fromServer.equalsIgnoreCase("already in your contact list")){
                            Toast.makeText(getApplicationContext(), "This contact is already in your" +
                                    " contact list", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Something went wrong contact " +
                                    "not added, check your network connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You've changed your mind, thats ok:)", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    //removes all elements from the list
    private void clearList(){
        ServerController.getInstance().clearSearch();
        searchList.clear();
    }

    //sets the list to whats returned from the server
    private void setSearchList(String name){
        List<Contact> tempList = ServerController.getInstance().getSearch(name);
        if (tempList.size()>0) {
            for (Contact c : tempList) {
                searchList.add(c);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //used to go back to previous view
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.backToContacts:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent;
                    if (isItParent){
                        intent = new Intent(MyApplication.getAppContext(), ParentContactsActivity.class);
                    }
                    else{
                        intent = new Intent(MyApplication.getAppContext(), ChildContactsActivity.class);
                    }
                    MyApplication.getAppContext().startActivity(intent);
                }
                break;

        }
        return true;
    }
}
