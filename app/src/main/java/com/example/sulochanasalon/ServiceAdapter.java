package com.example.sulochanasalon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ServiceAdapter extends BaseAdapter
{
    private Context context;
    private List<Service> serviceList;
    private LayoutInflater inflater;
    private boolean isAdminView;

    public ServiceAdapter(Context context, List<Service> serviceList, boolean isAdminView)
    {
        this.context = context;
        this.serviceList = serviceList;
        this.isAdminView = isAdminView;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_service, parent, false);
        }

        Service service = serviceList.get(position);

        TextView serviceName = convertView.findViewById(R.id.serviceName);
        TextView servicePrice = convertView.findViewById(R.id.servicePrice);
        TextView serviceDescription = convertView.findViewById(R.id.serviceDescription);

        serviceName.setText(service.getName());
        servicePrice.setText("Price: $" + service.getPrice());
        serviceDescription.setText(service.getDescription());

        if (isAdminView) {
            // Additional UI setup for admin (optional)
        }

        return convertView;
    }
}
