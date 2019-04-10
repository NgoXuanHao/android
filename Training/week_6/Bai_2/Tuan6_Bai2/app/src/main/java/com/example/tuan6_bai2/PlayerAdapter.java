package com.example.tuan6_bai2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerAdapter extends ArrayAdapter<Player> {
    Context context;
    int layoutResourceId;
    ArrayList<Player> data = null;
    public PlayerAdapter(Context context, int layoutResourceId, ArrayList<Player> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();
            holder.img_player = row.findViewById(R.id.imgPlayer);
            holder.txt_name = row.findViewById(R.id.playerName);
            holder.txt_club = row.findViewById(R.id.playerClub);
            holder.txt_star = row.findViewById(R.id.playerStar);

            row.setTag(holder);
        } else {
            holder = (PlayerHolder) row.getTag();
        }

        Player item = data.get(position);
        holder.img_player.setImageResource(item.img);
        holder.txt_name.setText(item.name);
        holder.txt_star.setText(item.star);
        holder.txt_club.setText(item.club);
        return row;
    }
    static class PlayerHolder {
        ImageView img_player;
        TextView txt_name;
        TextView txt_club;
        TextView txt_star;
    }
}
