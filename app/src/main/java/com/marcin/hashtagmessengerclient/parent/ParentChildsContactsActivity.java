package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to preview childs contacts
 * its a shrink down version of Contacts activity
 * without the option to search for contact
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.ChatActivity;
import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.contacts.ContactAdapter;
import com.marcin.hashtagmessengerclient.login.LoginController;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.List;

public class ParentChildsContactsActivity extends ListActivity {

    private ListView listView;
    private List<Contact> contactList;
    private ArrayAdapter<Contact> adapter;
    private ImageView mykids2;
    Long childsId = 0l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_childs_contacts);

        childsId = getIntent().getExtras().getLong("childsId");
        //------------------------------------------------------------------------------------------
        mykids2 = findViewById(R.id.mykids2);
        mykids2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    LoginController.getInstance().animateButtonTouched(v);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    LoginController.getInstance().animateButtonReleased(v);
                    Intent intent = new Intent(MyApplication.getAppContext(), ParentUpdateChildActivity.class);
                    intent.putExtra("contactId", childsId);
                    MyApplication.getAppContext().startActivity(intent);
                }
                return true;
            }
        });

        contactList = ServerController.getInstance().getContacts(childsId);
        listView = this.getListView();
        //set ListView adapter first
        adapter = new ContactAdapter(this, R.layout.contact_item, contactList);
        listView.setAdapter(adapter);

        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MyApplication.getAppContext(), "contact id: "+
                                contactList.get(position).getContactId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MyApplication.getAppContext(), ParentChildsChatsActivity.class);
                intent.putExtra("childsId", childsId);
                intent.putExtra("contactId", contactList.get(position).getContactId());
                MyApplication.getAppContext().startActivity(intent);
            }
        });
    }
}
