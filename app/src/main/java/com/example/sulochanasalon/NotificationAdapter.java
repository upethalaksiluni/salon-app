package com.example.sulochanasalon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NotificationAdapter extends BaseAdapter
{
    private Context context;
    private List<UserNotification> notifications;
    private LayoutInflater inflater;

    public NotificationAdapter(Context context, List<UserNotification> notifications)
    {
        this.context = context;
        this.notifications = notifications;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public UserNotification getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_notification, parent, false);
        }

        UserNotification notification = getItem(position);
        TextView messageTextView = convertView.findViewById(R.id.notificationMessage);
        TextView dateTextView = convertView.findViewById(R.id.notificationDate);

        messageTextView.setText(notification.getMessage());
        dateTextView.setText(notification.getDate());

        return convertView;
    }
}