package com.example.sulochanasalon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends BaseAdapter
{
    private Context context;
    private List<String> appointments;
    private LayoutInflater inflater;

    public AppointmentAdapter(Context context, List<String> appointments)
    {
        this.context = context;
        this.appointments = appointments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public String getItem(int position) {
        return appointments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_appointment, parent, false);
        }

        // Bind appointment details to the TextView
        TextView appointmentTextView = convertView.findViewById(R.id.appointmentTextView);
        appointmentTextView.setText(getItem(position));

        return convertView;
    }
}
