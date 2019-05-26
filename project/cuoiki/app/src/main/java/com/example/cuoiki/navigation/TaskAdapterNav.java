package com.example.cuoiki.navigation;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.cuoiki.R;
import com.example.cuoiki.task.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TaskAdapterNav extends RecyclerView.Adapter<com.example.cuoiki.navigation.TaskAdapterNav.ViewHolder>{
    SimpleDateFormat day = new SimpleDateFormat("EEE dd MM yyyy");
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title_item_nav, txt_time_left_nav, txt_count_task_nav;
        private CardView cardViewNav;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_title_item_nav = itemView.findViewById(R.id.txt_title_item_nav);
            txt_time_left_nav = itemView.findViewById(R.id.txt_time_left_nav);
            txt_count_task_nav = itemView.findViewById(R.id.txt_count_task_nav);
            cardViewNav = itemView.findViewById(R.id.cardview_nav);
        }
    }

    private List<Task> tasks_nav;
    public TaskAdapterNav(List<Task> mtask){
        tasks_nav = mtask;
    }

    public com.example.cuoiki.navigation.TaskAdapterNav.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_task, parent, false);
        com.example.cuoiki.navigation.TaskAdapterNav.ViewHolder viewHolder = new com.example.cuoiki.navigation.TaskAdapterNav.ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(com.example.cuoiki.navigation.TaskAdapterNav.ViewHolder viewHolder, int position) {
        Task task_nav = tasks_nav.get(position);
        TextView title_item_nav = viewHolder.txt_title_item_nav;
        TextView count_task_nav = viewHolder.txt_count_task_nav;
        TextView time_left_nav = viewHolder.txt_time_left_nav;
        CardView cardViewNav = viewHolder.cardViewNav;
        if(task_nav.isDone()){
            time_left_nav.setText(R.string.done);
            cardViewNav.setCardBackgroundColor(Color.CYAN);
        }
        else time_left(position, time_left_nav);
        title_item_nav.setText(task_nav.getTitle());
        String count = Integer.toString(task_nav.getCount());
        String total = Integer.toString(task_nav.getTotal());
        count_task_nav.setText(count + "/" + total);
    }

    @Override
    public int getItemCount() {
        return tasks_nav.size();
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(tasks_nav, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(tasks_nav, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swipe(int position, int direction) {
        tasks_nav.remove(position);
        notifyItemRemoved(position);
    }

    private void time_left(int pos, TextView tv){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Task task = tasks_nav.get(pos);
        long date_end = task.getDate_end();
        long cur = System.currentTimeMillis();
        long left = date_end - cur;
        long d = TimeUnit.MILLISECONDS.toDays(left);
        long h, mi;
        if(d <= 0){
            h = TimeUnit.MILLISECONDS.toHours(left);
            if(h > 0)
                tv.setText("About " + Long.toString(h) + " hours");
            else{
                mi = TimeUnit.MILLISECONDS.toMinutes(left);
                if(mi > 0)
                    tv.setText("About " + Long.toString(mi) + " minutes");
                else if(mi == 0)
                    tv.setText(R.string.nows);
                else tv.setText(R.string.to_late);
            }
        }
        else{
            if(d < 365)
                tv.setText("About " + Long.toString(d) + " days");
            else
                tv.setText("About " + Long.toString(d / 365) + " years");
        }

    }
}
