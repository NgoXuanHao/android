package com.example.quanlytour.phieu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.quanlytour.R;

import java.util.ArrayList;

public class PhieuAdapter extends ArrayAdapter<Phieu> {
    Context context;
    int layoutResourceId;
    ArrayList<Phieu> data;

    public PhieuAdapter(Context context, int layoutResourceId, ArrayList<Phieu> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    static class PhieuHolder{
        TextView tv_soPhieu, tv_soNguoi, tv_ngayDK;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        PhieuHolder holder = null;
        if(row != null){
            holder = (PhieuHolder) row.getTag();
        }
        else
        {
            holder = new PhieuHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_phieu, parent, false);
            holder.tv_soPhieu = (TextView) row.findViewById(R.id.tv_sophieu);
            holder.tv_soNguoi = (TextView) row.findViewById(R.id.tv_soNguoi);
            holder.tv_ngayDK = (TextView) row.findViewById(R.id.tv_ngayDK);
            row.setTag(holder);
        }
        Phieu phieu = data.get(position);
        holder.tv_soPhieu.setText(Integer.toString(phieu.getSoPhieu()));
        holder.tv_ngayDK.setText("Ngày ĐK: " + phieu.getNgayDk());
        holder.tv_soNguoi.setText("Số người: " + Integer.toString(phieu.getSoNguoi()));
        return row;
    }
}
