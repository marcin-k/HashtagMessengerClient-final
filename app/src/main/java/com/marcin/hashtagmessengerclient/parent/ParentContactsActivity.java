package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Contact activity used to display list of contacts
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.ChatActivity;
import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.contacts.ContactAdapter;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.List;

public class ParentContactsActivity extends ListActivity {

    private ImageView contacts;
    private ImageView home;
    private ImageView mykids;
    private ImageView settings;
    private ImageView searchIV;
    private ListView listView;
    private List<Contact> contactList;
    private ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_contacts);
        //buttons
        contacts = findViewById(R.id.contacts);
        home = findViewById(R.id.home);
        mykids = findViewById(R.id.mykids);
        settings = findViewById(R.id.settings);
        searchIV = findViewById(R.id.searchET);

        contacts.setOnTouchListener(_ParentNavigation.getInstance());
        home.setOnTouchListener(_ParentNavigation.getInstance());
        mykids.setOnTouchListener(_ParentNavigation.getInstance());
        settings.setOnTouchListener(_ParentNavigation.getInstance());
        searchIV.setOnTouchListener(_ParentNavigation.getInstance());

        contactList = ServerController.getInstance().getContacts(ServerController.getInstance().getId());
        listView = this.getListView();
        //set ListView adapter first
        adapter = new ContactAdapter(this, R.layout.contact_item, contactList);
        listView.setAdapter(adapter);

        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ChildContactsActivity.this, "contact id: "+
//                                contactList.get(position).getContactId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyApplication.getAppContext(), ChatActivity.class);
                intent.putExtra("contactId", contactList.get(position).getContactId());
                intent.putExtra("isItParent", true);
                MyApplication.getAppContext().startActivity(intent);
            }
        });

    }
}
