package com.marcin.hashtagmessengerclient.child;
/**************************************************************
 * Activity for list of contacts for a child
 * on contact selected moves to chat with selected user
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

public class ChildContactsActivity extends ListActivity {

    private ImageView contacts;
    private ImageView myallowance;
    private ImageView sos;
    private ImageView searchIV;

    private ListView listView;
    private List<Contact> contactList;
    private ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_contacts);

        //---------------------Navigation-----------------------------------------------------------
        contacts = findViewById(R.id.contacts);
        myallowance = findViewById(R.id.myallowance);
        sos = findViewById(R.id.sos);
        searchIV = findViewById(R.id.searchET);

        contacts.setOnTouchListener(_ChildNavigation.getInstance());
        myallowance.setOnTouchListener(_ChildNavigation.getInstance());
        sos.setOnTouchListener(_ChildNavigation.getInstance());
        searchIV.setOnTouchListener(_ChildNavigation.getInstance());
        //------------------------------------------------------------------------------------------

        contactList = ServerController.getInstance().getContacts(ServerController.getInstance().getId());
        listView = this.getListView();
        //set ListView adapter first
        adapter = new ContactAdapter(this, R.layout.contact_item, contactList);
        listView.setAdapter(adapter);

        //on click moves to chat with that contact
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ChildContactsActivity.this, "contact id: "+
//                                contactList.get(position).getContactId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyApplication.getAppContext(), ChatActivity.class);
                intent.putExtra("contactId", contactList.get(position).getContactId());
                intent.putExtra("isItParent", false);
                MyApplication.getAppContext().startActivity(intent);
            }
        });
    }
}
