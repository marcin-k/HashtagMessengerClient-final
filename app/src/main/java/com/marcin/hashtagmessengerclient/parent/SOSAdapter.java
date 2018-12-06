package com.marcin.hashtagmessengerclient.parent;
/**************************************************************
 * Custom adapter to store the list of sos messages(alerts)
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
import com.marcin.hashtagmessengerclient.cache.CacheController;
import com.marcin.hashtagmessengerclient.chat.Message;
import com.marcin.hashtagmessengerclient.serverConnectivity.ServerController;

import java.util.List;

public class SOSAdapter extends ArrayAdapter<Message> {

    private Activity activity;
    private List<Message> soslist;

    public SOSAdapter(Activity context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.soslist = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        Message chatMessage = getItem(position);
        int viewType = getItemViewType(position);
        layoutResource = R.layout.sos;

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
//        ServerController.getInstance().getChildren(ServerController.getInstance().getId());
        holder.child.setText(CacheController.getInstance().getFirstName(chatMessage.getSenderId())+" send SOS");
        String [] latitudeLongitude = chatMessage.getBody().split(",");
        holder.latitude.setText(""+latitudeLongitude[0]);
        holder.longitude.setText(""+latitudeLongitude[1]);

        return convertView;
    }


    private class ViewHolder {
        private TextView child;
        private TextView latitude;
        private TextView longitude;

        public ViewHolder(View v) {
            child = (TextView) v.findViewById(R.id.childsNameTV);
            latitude = (TextView) v.findViewById(R.id.latitudeTV);
            longitude = (TextView) v.findViewById(R.id.longitudeTV);
        }
    }
}
