package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Activity used to display a list of children for a parent
 * include a button to create a new childs account.
 * On child selected moves to child update (which also allows
 * to preview childs contacts / messages)
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.MyApplication;
import com.marcin.hashtagmessengerclient.R;
import com.marcin.hashtagmessengerclient.chat.ChatActivity;
import com.marcin.hashtagmessengerclient.contacts.Contact;
import com.marcin.hashtagmessengerclient.contacts.ContactAdapter;
import com.marcin.hashtagmessengerclient.model.ChildUser;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.List;

public class ParentMyKidsActivity extends ListActivity {

    private ImageView contacts;
    private ImageView home;
    private ImageView mykids;
    private ImageView settings;
    private TextView createchild;

    //list
    private ListView listView;
    private List<Contact> childrenList;
    private ArrayAdapter<Contact> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_my_kids);

        //buttons
        contacts = findViewById(R.id.contacts);
        home = findViewById(R.id.home);
        mykids = findViewById(R.id.mykids);
        settings = findViewById(R.id.settings);
        createchild = findViewById(R.id.createchild);

        contacts.setOnTouchListener(_ParentNavigation.getInstance());
        home.setOnTouchListener(_ParentNavigation.getInstance());
        mykids.setOnTouchListener(_ParentNavigation.getInstance());
        settings.setOnTouchListener(_ParentNavigation.getInstance());
        createchild.setOnTouchListener(_ParentNavigation.getInstance());

        childrenList = ServerController.getInstance().getChildren(ServerController.getInstance().getId());
        listView = this.getListView();
        //set ListView adapter first
        adapter = new ContactAdapter(this, R.layout.contact_item, childrenList);
        listView.setAdapter(adapter);

        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyApplication.getAppContext(), ParentUpdateChildActivity.class);
                intent.putExtra("contactId", childrenList.get(position).getContactId());
                MyApplication.getAppContext().startActivity(intent);
            }
        });


    }
}
