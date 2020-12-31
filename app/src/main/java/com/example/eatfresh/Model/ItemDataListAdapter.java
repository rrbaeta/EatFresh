package com.example.eatfresh.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.eatfresh.R;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class ItemDataListAdapter extends ArrayAdapter<ItemData> {

    private Context mContext;
    private int mResource;
    private long currentDate;
    private long daysLeft;

    public ItemDataListAdapter(Context context, int resource, ArrayList<ItemData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get Item Information
        String name = getItem(position).getItemName();
        String itemId = getItem(position).getItemId();
        long expiryDate = getItem(position).getExpiryDate();

        Timestamp currentDateTimestamp = new Timestamp(new Date().getTime());
        currentDate = currentDateTimestamp.getTime();
        daysLeft = (expiryDate * 1000) - currentDate;
        daysLeft = (daysLeft / (1000*60*60*24));
        String daysLeftString = String.valueOf(daysLeft);

        ItemData itemData = new ItemData(name, itemId, expiryDate);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        TextView itemNameTextView = (TextView) convertView.findViewById(R.id.itemNameTextView);
        TextView daysLeftTextView = (TextView) convertView.findViewById(R.id.daysLeftTextView);

        itemNameTextView.setText(name);

        if (daysLeft > 0)
        {
            daysLeftTextView.setText(daysLeftString  + " Days Left");
        }
        else
        {
            daysLeftTextView.setText("Expired");
        }


        return convertView;
    }

}
