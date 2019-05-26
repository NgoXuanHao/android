package com.example.cuoiki.task;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.cuoiki.R;
import com.example.cuoiki.database.Database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ch.halcyon.squareprogressbar.SquareProgressBar;
import ch.halcyon.squareprogressbar.utils.PercentStyle;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{
    SimpleDateFormat day = new SimpleDateFormat("EEE dd MM yyyy");
    SimpleDateFormat full = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
    public class ViewHolder extends RecyclerView.ViewHolder {
        private SquareProgressBar process_per;
        private TextView txt_title_item, txt_create_home;
        private RoundCornerProgressBar process_time;
        private CardView cardViewHome;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            process_per = itemView.findViewById(R.id.process_per);
            txt_title_item = itemView.findViewById(R.id.txt_title_item);
            txt_create_home = itemView.findViewById(R.id.txt_create_home);
            process_time = itemView.findViewById(R.id.process_time);
            cardViewHome = itemView.findViewById(R.id.cardview_home);
        }
    }

    public ArrayList<Task> tasks;
    private ArrayList<Task> arrayList;
    public TaskAdapter(ArrayList<Task> mtask){
        tasks = mtask;
    }

    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View taskView = inflater.inflate(R.layout.item_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder viewHolder, int position) {
        full.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Task task = tasks.get(position);
        TextView title_item = viewHolder.txt_title_item;
        TextView create_home = viewHolder.txt_create_home;
        RoundCornerProgressBar process_time = viewHolder.process_time;
        SquareProgressBar process_per = viewHolder.process_per;
        CardView cardView = viewHolder.cardViewHome;
        float time = percent_time(position)*100;
        if(task.isDone()) {
            cardView.setCardBackgroundColor(Color.CYAN);
            process_time.setProgress(0);
        }
        else process_time.setProgress((float)time);
        title_item.setText(task.getTitle());
        long create = task.getCreated();
        Date date = new Date(create);
        create_home.setText("Create at " + full.format(date));
        PercentStyle percentStyle = new PercentStyle(Paint.Align.CENTER, 70, true);
        percentStyle.setTextColor(Color.rgb(111, 111, 89));
        Bitmap bmp = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.parseColor("#0000FF"));
        process_per.setImageBitmap(bmp);
        process_per.setProgress((float)percent_process(position)*100);
        process_per.setPercentStyle(percentStyle);
        process_per.setWidth(5);
        process_per.showProgress(true);
        process_per.setColor("#0000FF");
        process_per.setOpacity(true, true);
        process_time.setReverse(false);
        if(time <= 10){
            process_time.setProgressColor(Color.parseColor("#DC143C"));
        }
        else if(time > 10 && time <= 30){
            process_time.setProgressColor(Color.parseColor("#FF0000"));
        }
        else if(time > 30 && time <= 50){
            process_time.setProgressColor(Color.parseColor("#FFA500"));
        }
        else if(time > 50 && time <= 80){
            process_time.setProgressColor(Color.parseColor("#FFFF00"));
        }
        else if(time > 80)
            process_time.setProgressColor(Color.parseColor("#7FFF00"));
        process_time.setProgressBackgroundColor(Color.parseColor("#DCDCDC"));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(tasks, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(tasks, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void swipe(int position, int direction) {
    }

    private float percent_time(int pos){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        float per = -1;
        Task task = tasks.get(pos);
        long date_start = task.getDate_start();
        long date_end = task.getDate_end();
        long current = System.currentTimeMillis();
        long count = date_end - current;
        long total = date_end - date_start;
        per = (float) count / (float)total;
        return per;
    }

    private float percent_process(int pos){
        float per = -1;
        Task task = tasks.get(pos);
        int count = task.getCount();
        int total = task.getTotal();
        if(total == 0)
            return 0;
        else
            per = (float) count / (float) total;
        return per;
    }

    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        tasks.clear();
        if(charText.length()==0)
        {
            tasks.addAll(arrayList);
        }else
        {
            for(Task task : arrayList)
            {
                if (task.getTitle().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    tasks.add(task);
                }
            }
        }
        notifyDataSetChanged();
    }

}