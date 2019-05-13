package com.example.quanlytour.khach_hang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quanlytour.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AdapterKH extends ArrayAdapter<KhachHang> {
    Context context;
    int layoutResourceId;
    ArrayList<KhachHang> data;

    public AdapterKH(Context context, int layoutResourceId, ArrayList<KhachHang> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    static class KhachHangHolder{
        TextView tv_maKH, tv_tenKH, tv_diaChi;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        KhachHangHolder holder = null;
        if(row != null){
            holder = (KhachHangHolder) row.getTag();
        }
        else
        {
            holder = new KhachHangHolder();
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.listview_khachhang, parent, false);
            holder.tv_tenKH = (TextView) row.findViewById(R.id.tv_tenKH);
            holder.tv_maKH = (TextView) row.findViewById(R.id.tv_makh);
            holder.tv_diaChi = (TextView) row.findViewById(R.id.tv_diachiKH);
            row.setTag(holder);
        }
        KhachHang kh = data.get(position);
        holder.tv_maKH.setText(kh.getMaKH());
        holder.tv_tenKH.setText("Tên KH: " + kh.getTenKH());
        holder.tv_diaChi.setText("Địa chỉ: " + kh.getDiaChi());
        return row;
    }
}
