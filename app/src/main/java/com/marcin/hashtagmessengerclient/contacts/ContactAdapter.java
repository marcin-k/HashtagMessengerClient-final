package com.marcin.hashtagmessengerclient.contacts;
/**************************************************************
 * Custom Array adapter to display list of users, used in
 * contacts (child/parent) as well as in childs contact preview
 * in parent interface
 * List only shows username
 *
 * author: Marcin Krzeminski
 *         x17158851
 *
 **************************************************************/
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marcin.hashtagmessengerclient.R;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    private Activity activity;
    private List<Contact> contacts;

    public ContactAdapter(Activity context, int resource, List<Contact> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.contacts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        Contact contact = getItem(position);
        int viewType = getItemViewType(position);
        layoutResource = R.layout.contact_item;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
        holder.contactName.setText(contact.getUsername());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    //view holder only stores the name
    private class ViewHolder {
        private TextView contactName;

        public ViewHolder(View v) {
            contactName = (TextView) v.findViewById(R.id.contactName);
        }
    }
}
