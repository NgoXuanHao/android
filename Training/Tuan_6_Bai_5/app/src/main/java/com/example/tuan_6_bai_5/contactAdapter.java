package com.example.tuan_6_bai_5;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class contactAdapter extends ArrayAdapter<contact> {
    Context context;
    int layoutResourceid;
    ArrayList<contact> data= null;

    public contactAdapter( Context context, int layoutResourceid,  List<contact> data) {
        super(context, layoutResourceid, data);
        this.context = context;
        this.layoutResourceid = layoutResourceid;
        this.data = (ArrayList<contact>) data;
    }

    public View getView (int position, View convertView, ViewGroup parent){
        View row = convertView;
        HolderContact holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceid, parent, false);
            holder = new HolderContact();
            holder.holderContact = row.findViewById(R.id.contact);

            row.setTag(holder);
        } else {
            holder =(HolderContact) row.getTag();
        }
        contact item = data.get(position);

        holder.holderContact.setText(item.contact);

        return  row;
    }
    static class HolderContact{

        TextView holderContact;
    }
}

