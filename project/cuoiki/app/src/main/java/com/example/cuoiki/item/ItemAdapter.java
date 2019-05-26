package com.example.cuoiki.item;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.cuoiki.R;

import java.util.Collections;
import java.util.List;

public class ItemAdapter extends  RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title_item;
        private CheckBox cb_check;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_item = itemView.findViewById(R.id.title_item);
            cb_check = itemView.findViewById(R.id.cb_check);
        }
    }

    private List<Item> items;
    public ItemAdapter(List<Item> mitem){
        items = mitem;
    }

    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View item_View = inflater.inflate(R.layout.row_edit_task, parent, false);

        ViewHolder viewHolder = new ViewHolder(item_View);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        TextView title = viewHolder.tv_title_item;
        CheckBox cb = viewHolder.cb_check;
        title.setText(item.getTitle());
        if(item.isCheck())
            cb.setChecked(true);
        else cb.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onMove(int fromPosition, int toPosition) {

    }

    public void swipe(int position, int direction) {
    }
}
