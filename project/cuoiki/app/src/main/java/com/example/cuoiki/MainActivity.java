package com.example.cuoiki;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cuoiki.database.Database;
import com.example.cuoiki.item.Item;
import com.example.cuoiki.item.ItemAdapter;
import com.example.cuoiki.navigation.TaskAdapterNav;
import com.example.cuoiki.note.MainActionModeCallback;
import com.example.cuoiki.note.MainNote;
import com.example.cuoiki.note.NoteEventListener;
import com.example.cuoiki.note.Notes;
import com.example.cuoiki.note.NotesAdapter;
import com.example.cuoiki.note.ViewNote;
import com.example.cuoiki.support.ItemClickSupport;
import com.example.cuoiki.support.ItemTouchListenner;
import com.example.cuoiki.support.Notification;
import com.example.cuoiki.support.SimpleItemTouchHelperCallback;
import com.example.cuoiki.task.Edit_Task;
import com.example.cuoiki.task.Task;
import com.example.cuoiki.task.TaskAdapter;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener{
    private MaterialSearchBar searchBar;
    private DrawerLayout drawer;
    private LinearLayout l_languega, l_add_task, l_add_note;
    private RecyclerView recy_view_home, recy_view_home_nav;
    private FloatingActionButton fab, fab_add_task, fab_add_note;
    private TaskAdapter home_adapter = null;
    private TaskAdapter search_adapter;
    private TaskAdapterNav nav_task_adapter = null;
    private ArrayList<Task> todaytasks;
    private ArrayList<Task> alltasks;
    private ArrayList<Task> searchs;
    private Locale myLocale;
    private boolean isFABOpen = false;
    private BoomMenuButton boomLang;
    private String currentLanguage = "en";
    private String currentLang;
    private ArrayList<String> txt = new ArrayList<>();
    SimpleDateFormat day = new SimpleDateFormat("EEE dd MM yyyy");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        txt.add("EN");
        txt.add("VI");
        txt.add("JP");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        currentLanguage = getIntent().getStringExtra(currentLang);
        setControl();
        setEvent();
    }

    private void setControl(){
        searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        recy_view_home = (RecyclerView) findViewById(R.id.view_home);
        recy_view_home_nav = (RecyclerView) findViewById(R.id.view_home_nav);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        fab_add_task = (FloatingActionButton) findViewById(R.id.fab_add_task);
        fab_add_note = (FloatingActionButton) findViewById(R.id.subfab_note);
        boomLang = (BoomMenuButton) findViewById(R.id.bmb);
        l_add_task = (LinearLayout) findViewById(R.id.linear_task);
        l_add_note = (LinearLayout) findViewById(R.id.liner_add_note);
        l_languega = (LinearLayout) findViewById(R.id.linear_languare);
    }
    private void setEvent(){
        getAllTasks();
        getTodayTask();
        recy_view_home.setAdapter(home_adapter);
        recy_view_home.setLayoutManager(new LinearLayoutManager(this));
        task_open();
        searchBar.setOnSearchActionListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen) showFABMenu();
                else closeFABMenu();
            }
        });
        closeFABMenu();

        addItemTouchCallback_home(recy_view_home);

        fab_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Edit_Task.class);
                intent.putExtra("type", "Add Task");
                startActivity(intent);
            }
        });

        fab_add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainNote.class);
                startActivity(intent);
            }
        });

        for (int i = 0; i < boomLang.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalText(txt.get(i))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            if(index == 0)
                                setLocale("en");
                            if(index == 1)
                                setLocale("vi");
                            if(index == 2)
                                setLocale("ja");
                        }
                    });
            boomLang.addBuilder(builder);
        }


        ItemClickSupport.addTo(recy_view_home).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(MainActivity.this, Edit_Task.class);
                Task t = todaytasks.get(position);
                intent.putExtra("id", Integer.toString(t.getId()));
                intent.putExtra("type", "Edit task");
                startActivity(intent);
            }
        });

        ItemClickSupport.addTo(recy_view_home_nav).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(MainActivity.this, Edit_Task.class);
                Task t = alltasks.get(position);
                intent.putExtra("id", Integer.toString(t.getId()));
                intent.putExtra("type", "Edit task");
                startActivity(intent);
            }
        });

        ItemClickSupport.addTo(recy_view_home).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                final int pos = position;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Is this task done?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!todaytasks.get(pos).isDone()){
                                    setDone(pos, todaytasks);
                                    home_adapter.notifyDataSetChanged();
                                }
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });
    }

    private void task_open(){
        getAllTasks();
        recy_view_home_nav.setAdapter(nav_task_adapter);
        recy_view_home_nav.setLayoutManager(new LinearLayoutManager(this));
        addItemTouchCallback_task_nav(recy_view_home_nav);
    }

    public void onSearchStateChanged(boolean enabled) {
        searchs = new ArrayList<>();
        search_adapter = new TaskAdapter(searchs);
        recy_view_home.setAdapter(search_adapter);
        recy_view_home.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        search_task(String.valueOf(text));
        recy_view_home.setAdapter(search_adapter);
        recy_view_home.setLayoutManager(new LinearLayoutManager(this));
    }

    public void onButtonClicked(int buttonCode) {
        switch (buttonCode) {
            case MaterialSearchBar.BUTTON_NAVIGATION:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case MaterialSearchBar.BUTTON_BACK:
                searchBar.disableSearch();
                getTodayTask();
                recy_view_home.setAdapter(home_adapter);
                recy_view_home.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void showFABMenu(){
        isFABOpen=true;
        l_languega.setVisibility(View.VISIBLE);
        l_add_note.setVisibility(View.VISIBLE);
        l_add_task.setVisibility(View.VISIBLE);
        fab.setImageResource(R.drawable.ic_clear_black_24dp);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        l_languega.setVisibility(View.INVISIBLE);
        l_add_note.setVisibility(View.INVISIBLE);
        l_add_task.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
    }

    private void addItemTouchCallback_home(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListenner() {

            @Override
            public void onMove(int oldPosition, int newPosition) {
            }

            @Override
            public void swipe(int position, int direction) {
                final int pos = position;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage(R.string.sure_delete_task);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteTask(pos, todaytasks);
                                alltasks.remove(todaytasks.get(pos));
                                nav_task_adapter.notifyDataSetChanged();
                                task_open();
                                todaytasks.remove(pos);
                                home_adapter.notifyItemRemoved(pos);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                home_adapter.notifyDataSetChanged();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void addItemTouchCallback_task_nav(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListenner() {

            @Override
            public void onMove(int oldPosition, int newPosition) {
            }

            @Override
            public void swipe(int position, int direction) {
                final int pos = position;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage(R.string.sure_delete_task);
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteTask(pos, alltasks);
                                todaytasks.remove(alltasks.get(pos));
                                home_adapter.notifyDataSetChanged();
                                getTodayTask();
                                recy_view_home.setAdapter(home_adapter);
                                recy_view_home.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                addItemTouchCallback_task_nav(recy_view_home_nav);
                                alltasks.remove(pos);
                                nav_task_adapter.notifyItemRemoved(pos);
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                nav_task_adapter.notifyDataSetChanged();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void deleteTask(int position, ArrayList<Task> list){
        Task task = list.get(position);
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.delete(Database.TABLE_TASK, Database.COL_TASK_ID + "=?",
                new String[] {String.valueOf(Integer.toString(task.getId()))});
        sql.delete(Database.TABLE_ITEM, Database.COL_ITEM_ID_TASk + "=?",
                new String[] {String.valueOf(Integer.toString(task.getId()))});
        db.close();
        sql.close();
    }


    public void getTodayTask(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        todaytasks = new ArrayList<>();
        home_adapter = new TaskAdapter(todaytasks);
        long cur = System.currentTimeMillis();
        Date date_cur = new Date(cur);
        String[] curDate = day.format(date_cur).split(" ");
        int dayCur = Integer.parseInt(curDate[3]);
        int monthCur = Integer.parseInt(curDate[2]) - 1;
        int yearCur = Integer.parseInt(curDate[1]);
        int size = alltasks.size();
        for(int i = 0; i < size; i++){
            Task task = alltasks.get(i);
            Date date_s = new Date(task.getDate_start());
            String[] startDate = day.format(date_s).split(" ");
            int dayS = Integer.parseInt(startDate[3]);
            int monthS = Integer.parseInt(startDate[2]) - 1;
            int yearS = Integer.parseInt(startDate[1]);
            Date date_e = new Date(task.getDate_start());
            String[] endDate = day.format(date_e).split(" ");
            int dayE = Integer.parseInt(endDate[3]);
            int monthE = Integer.parseInt(endDate[2]) - 1;
            int yearE = Integer.parseInt(endDate[1]);
            if(yearS <= yearCur && yearCur <= yearE && monthS <= monthCur && monthCur <= monthE && dayS <= dayCur && dayCur <= dayE){
                Task t = new Task();
                t.setId(task.getId());
                t.setTitle(task.getTitle());
                t.setDate_start(task.getDate_start());
                t.setDate_end(task.getDate_end());
                t.setCreated(task.getCreated());
                t.setDone(task.isDone());
                t.setNotify(task.isNotify());
                t.setTotal(task.getTotal());
                t.setCount(task.getCount());
                todaytasks.add(t);
                home_adapter.notifyItemInserted(todaytasks.indexOf(t));

            }
        }
        sort(todaytasks);
        home_adapter.notifyDataSetChanged();
    }

    private void getAllTasks(){
        alltasks = new ArrayList<>();
        nav_task_adapter = new TaskAdapterNav(alltasks);
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        String query = "Select *  from " + Database.TABLE_TASK;
        Cursor cursor = sql.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Task t = new Task();
            t.setId(cursor.getInt(0));
            t.setTitle(cursor.getString(1));
            t.setDate_start(cursor.getLong(2));
            t.setDate_end(cursor.getLong(3));
            t.setCreated(cursor.getLong(4));
            t.setDone(cursor.getInt(5) > 0);
            if(cursor.getInt(6) > 0) {
                setNotification(cursor.getInt(0), cursor.getLong(3), cursor.getString(1));
            }
            t.setNotify(cursor.getInt(6) > 0);
            String query1 = "SELECT * FROM " + Database.TABLE_ITEM + " WHERE " + Database.COL_ITEM_ID_TASk + "=" + t.getId();
            Cursor cursor1 = sql.rawQuery(query1, null);
            int total = cursor1.getCount();
            t.setTotal(total);
            cursor1.close();
            String query2 = "SELECT * FROM " + Database.TABLE_ITEM + " WHERE " + Database.COL_ITEM_ID_TASk + "=" + t.getId() +
                    " AND " + Database.COL_ITEM_CHECKED + "=1";
            cursor1 = sql.rawQuery(query2, null);
            int count = cursor1.getCount();
            t.setCount(count);
            cursor1.close();
            alltasks.add(t);
            nav_task_adapter.notifyDataSetChanged();
            cursor.moveToNext();
        }
        sql.close();
        db.close();
        sort(alltasks);
    }

    private void sort(ArrayList<Task> list){
        int size = list.size();
        int tmp = size - 1;
        long min;
        long time;
        int index = -1;
        long cur = System.currentTimeMillis();
        for(int i = 0; i < size; i++){
            Task task = list.get(i);
            while(task.isDone() && tmp > i) {
                Collections.swap(list, i, tmp);
                tmp--;
            }
        }
        for(int i = 0; i < size - 1; i++){
            Task task = list.get(i);
            if(task.isDone()) break;
            min = task.getDate_end() - cur;
            for(int j = i + 1; j < size; j++){
                Task task1 = list.get(j);
                if(task1.isDone()) break;
                time = task1.getDate_end() - cur;
                if(time < min) index = j;
            }
            if(index != -1)
                Collections.swap(list, i, index);
                index = -1;
        }
    }

    private void setDone(int pos, ArrayList<Task> list){
        Task task = list.get(pos);
        task.setDone(true);
        task.setCount(task.getTotal());
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues ct = new ContentValues();
        ct.put(Database.COL_ITEM_CHECKED, true);
        sql.update(Database.TABLE_ITEM, ct, Database.COL_ITEM_ID_TASk + "=?",
                new String[]{String.valueOf(Integer.toString(task.getId()))});
        db.close();
        sql.close();
    }

    private void search_task(String q){
        searchs = new ArrayList<>();
        search_adapter = new TaskAdapter(searchs);
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        String query = "Select *  from " + Database.TABLE_TASK + " where " + Database.COL_TASK_TITLE + " like '%" + q + "%'";
        Cursor cursor = sql.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Task t = new Task();
            t.setId(cursor.getInt(0));
            t.setTitle(cursor.getString(1));
            t.setDate_start(cursor.getLong(2));
            t.setDate_end(cursor.getLong(3));
            t.setCreated(cursor.getLong(4));
            t.setDone(cursor.getInt(5) > 0);
            t.setNotify(cursor.getInt(6) > 0);
            String query1 = "SELECT * FROM " + Database.TABLE_ITEM + " WHERE " + Database.COL_ITEM_ID_TASk + "=" + t.getId();
            Cursor cursor1 = sql.rawQuery(query1, null);
            int total = cursor1.getCount();
            t.setTotal(total);
            cursor1.close();
            String query2 = "SELECT * FROM " + Database.TABLE_ITEM + " WHERE " +
                    Database.COL_ITEM_ID_TASk + "=" + t.getId() + " AND " + Database.COL_ITEM_CHECKED + "=1";
            cursor1 = sql.rawQuery(query2, null);
            int count = cursor1.getCount();
            t.setCount(count);
            cursor1.close();
            searchs.add(t);
            search_adapter.notifyDataSetChanged();
            cursor.moveToNext();
        }
        sql.close();
        db.close();
    }

    public void setNotification(int notification, long date_e, String title){
            Calendar startTime = Calendar.getInstance();
            startTime.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Date date = new Date(date_e);
            String[] day_end = day.format(date).split(" ");
            String[] time_end = time.format(date).split(":");
            int eDate = Integer.parseInt(day_end[1]);
            int eMonth = Integer.parseInt(day_end[2]) - 1;
            int eYear = Integer.parseInt(day_end[3]);
            int eHour = Integer.parseInt(time_end[0]);
            int eMinute = Integer.parseInt(time_end[1]);

            Intent intent = new Intent(MainActivity.this, Notification.class);
            intent.putExtra("notificationId", notification);
            intent.putExtra("todo", title);

            // getBroadcast(context, requestCode, intent, flags)
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                    0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

            startTime.set(Calendar.YEAR, eYear);
            startTime.set(Calendar.MONTH, eMonth);
            startTime.set(Calendar.DAY_OF_MONTH, eDate);
            startTime.set(Calendar.HOUR_OF_DAY, eHour);
            startTime.set(Calendar.MINUTE, eMinute);
            startTime.set(Calendar.SECOND, 0);
            long alarmStartTime = startTime.getTimeInMillis();
            alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);
    }

    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage)) {
            myLocale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            getResources().getConfiguration().locale = myLocale;
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);
        } else {
            Toast.makeText(MainActivity.this, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
    }
}
