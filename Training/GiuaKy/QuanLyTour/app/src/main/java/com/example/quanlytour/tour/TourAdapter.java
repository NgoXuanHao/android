package com.example.quanlytour.tour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.quanlytour.R;

import java.util.ArrayList;

public class TourAdapter extends ArrayAdapter<Tour> {
    Context context;
    int layoutResourceId;
    ArrayList<Tour> data = null;

    public TourAdapter(Context context, int layoutResourceId, ArrayList<Tour> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    static class TourHolder{
        TextView tv_maTour, tv_loTrinh, tv_hanhTrinh, tv_giaTour;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TourHolder holder = null;
        if(row != null){
            holder = (TourHolder) row.getTag();
        }
        else
        {
            holder = new TourHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_tour, parent, false);
            holder.tv_maTour = (TextView) row.findViewById(R.id.tv_matour);
            holder.tv_loTrinh = (TextView) row.findViewById(R.id.tv_lotrinh);
            holder.tv_hanhTrinh = (TextView) row.findViewById(R.id.tv_hanhtrinh);
            holder.tv_giaTour = (TextView) row.findViewById(R.id.tv_giatour);
            row.setTag(holder);
        }
        Tour tour = data.get(position);
        holder.tv_maTour.setText(tour.getMaTour());
        holder.tv_loTrinh.setText("Lộ trình: "+tour.getLotrinh());
        holder.tv_hanhTrinh.setText("Hành trình: "+tour.getHanhtrinhTour() + " km");
        holder.tv_giaTour.setText("Giá tour: "+tour.getGiaTour() + " VND");
        return row;
    }
}