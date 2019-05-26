package com.example.cuoiki.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cuoiki.MainActivity;
import com.example.cuoiki.R;
import com.example.cuoiki.database.Database;
import com.example.cuoiki.item.Item;
import com.example.cuoiki.item.ItemAdapter;
import com.example.cuoiki.support.ItemClickSupport;
import com.example.cuoiki.support.ItemTouchListenner;
import com.example.cuoiki.support.SimpleItemTouchHelperCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Edit_Task extends AppCompatActivity {
    EditText edt_title, edt_add;
    Button btn_add;
    TextView tv_save, tv_date_s, tv_time_s, tv_date_e, tv_time_e;
    Switch switchSimple;
    RecyclerView recyclerView_item;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Item> items = new ArrayList<>();
    ItemAdapter adapter_item = null;
    ArrayList<Integer> delete = new ArrayList<Integer>();
    int index = -1;
    int id_task = -1;
    int pos_item = -1;
    SimpleDateFormat day = new SimpleDateFormat("EEE dd MM yyyy");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
    SimpleDateFormat full = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm");
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit);
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View actionBar = mInflater.inflate(R.layout.actionbar_edit, null);
        TextView mTitleTextView = (TextView) actionBar.findViewById(R.id.title_text);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String id = intent.getStringExtra("id");
        if(id != null)
            index = Integer.parseInt(id);
        mTitleTextView.setText(type);
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0,0);
        tv_save = (TextView) actionBar.findViewById(R.id.tv_save);
        setControl();
        setEvents();
    }

    public void setControl() {
        btn_add = findViewById(R.id.btn_add);
        edt_add = findViewById(R.id.edt_add_task);
        tv_date_e = findViewById(R.id.tv_date_end);
        tv_date_s = findViewById(R.id.tv_date_start);
        tv_time_e = findViewById(R.id.tv_time_end);
        tv_time_s = findViewById(R.id.tv_time_start);
        switchSimple = findViewById(R.id.simpleSwitch);
        edt_title = findViewById(R.id.edt_title_task);
        recyclerView_item = findViewById(R.id.recycler_view_list);
    }

    public void setEvents() {
        initTime();
        addItemTouchCallback(recyclerView_item);
        adapter_item = new ItemAdapter(items);
        recyclerView_item.setAdapter(adapter_item);
        recyclerView_item.setLayoutManager(new LinearLayoutManager(this));

        if (index != -1) {
            day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            String query = "select * from " + Database.TABLE_TASK + " where " + Database.COL_TASK_ID + "=" + index;
            Cursor cursor = sql.rawQuery(query, null);
            cursor.moveToFirst();
            edt_title.setText(cursor.getString(1));
            long start = cursor.getLong(2);
            long end = cursor.getLong(3);
            Date date_s = new Date(start);
            Date date_e = new Date(end);
            tv_date_s.setText(day.format(date_s));
            tv_date_e.setText(day.format(date_e));
            tv_time_s.setText(time.format(date_s));
            tv_time_e.setText(time.format(date_e));
            switchSimple.setChecked(cursor.getInt(6) > 0);
            sql.close();
            db.close();
            getAllItem(index);
        }
        tv_date_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                String[] date_s = tv_date_s.getText().toString().split(" ");
                int mYear = Integer.parseInt(date_s[3]);
                int mMonth = Integer.parseInt(date_s[2]) - 1;
                int mDay = Integer.parseInt(date_s[1]);
                final long current = System.currentTimeMillis();
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit_Task.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.YEAR, year);
                        long start = c.getTimeInMillis();
                        if(start <= current){
                            Date date = new Date(current);
                            tv_date_s.setText(day.format(date));
                            tv_date_e.setText(day.format(date));
                        }
                        else{
                            String[] date_e = tv_date_e.getText().toString().split(" ");
                            int mYear_e = Integer.parseInt(date_e[3]);
                            int mMonth_e = Integer.parseInt(date_e[2]) - 1;
                            int mDay_e = Integer.parseInt(date_e[1]);
                            c.set(Calendar.DAY_OF_MONTH, mDay_e);
                            c.set(Calendar.MONTH, mMonth_e);
                            c.set(Calendar.YEAR, mYear_e);
                            long end = c.getTimeInMillis();
                            if(end <= start){
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.YEAR, year);
                                Date date = c.getTime();
                                tv_date_s.setText(day.format(date));
                                tv_date_e.setText(day.format(date));
                                String[] time_s = tv_time_s.getText().toString().split(":");
                                int mHour_s = Integer.parseInt(time_s[0]);
                                int mMinute_s = Integer.parseInt(time_s[1]);
                                c.set(Calendar.HOUR_OF_DAY, mHour_s);
                                c.set(Calendar.MINUTE, mMinute_s);
                                long start1 = c.getTimeInMillis();
                                String[] time_e = tv_time_e.getText().toString().split(":");
                                int mHour_e = Integer.parseInt(time_e[0]);
                                int mMinute_e = Integer.parseInt(time_e[1]);
                                c.set(Calendar.HOUR_OF_DAY, mHour_e);
                                c.set(Calendar.MINUTE, mMinute_e);
                                long end1 = c.getTimeInMillis();
                                if(end1 <= start1){
                                    c.set(Calendar.HOUR_OF_DAY, mHour_s + 1);
                                    date = c.getTime();
                                    tv_time_e.setText(time.format(date));
                                }
                            }
                            else {
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.YEAR, year);
                                Date date = c.getTime();
                                tv_date_s.setText(day.format(date));
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tv_time_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                String[] time_s = tv_time_s.getText().toString().split(":");
                int mHour = Integer.parseInt(time_s[0]);
                int mMinute = Integer.parseInt(time_s[1]);
                final long mCurTime = System.currentTimeMillis();
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit_Task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        String[] date_s = tv_date_s.getText().toString().split(" ");
                        int mYear_s = Integer.parseInt(date_s[3]);
                        int mMonth_s = Integer.parseInt(date_s[2]) - 1;
                        int mDay_s = Integer.parseInt(date_s[1]);
                        c.set(Calendar.DAY_OF_MONTH, mDay_s);
                        c.set(Calendar.MONTH, mMonth_s);
                        c.set(Calendar.YEAR, mYear_s);
                        long start = c.getTimeInMillis();
                        if(start <= mCurTime){
                            Date date = new Date(mCurTime);
                            tv_time_s.setText(time.format(date));
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay + 1);
                            date = c.getTime();
                            tv_time_e.setText(time.format(date));
                        }
                        else{
                            String[] date_e = tv_date_e.getText().toString().split(" ");
                            int mYear_e = Integer.parseInt(date_e[3]);
                            int mMonth_e = Integer.parseInt(date_e[2]) - 1;
                            int mDay_e = Integer.parseInt(date_e[1]);
                            String[] time_e = tv_time_e.getText().toString().split(":");
                            int mHour_e = Integer.parseInt(time_e[0]);
                            int mMinute_e = Integer.parseInt(time_e[1]);
                            c.set(Calendar.DAY_OF_MONTH, mDay_e);
                            c.set(Calendar.MONTH, mMonth_e);
                            c.set(Calendar.YEAR, mYear_e);
                            c.set(Calendar.HOUR_OF_DAY, mHour_e);
                            c.set(Calendar.MINUTE, mMinute_e);
                            long end = c.getTimeInMillis();
                            if(end <= start){
                                Date date = new Date(start);
                                tv_time_s.setText(time.format(date));
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay + 1);
                                date = c.getTime();
                                tv_time_e.setText(time.format(date));
                            }
                            else{
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                Date date = c.getTime();
                                tv_time_s.setText(time.format(date));
                            }
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        tv_date_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                final String[] date_e = tv_date_e.getText().toString().split(" ");
                int mYear = Integer.parseInt(date_e[3]);
                int mMonth = Integer.parseInt(date_e[2]) - 1;
                int mDay = Integer.parseInt(date_e[1]);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Edit_Task.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String[] date_s = tv_date_s.getText().toString().split(" ");
                        int mYear_s = Integer.parseInt(date_s[3]);
                        int mMonth_s = Integer.parseInt(date_s[2]) - 1;
                        int mDay_s = Integer.parseInt(date_s[1]);
                        c.set(Calendar.DAY_OF_MONTH, mDay_s);
                        c.set(Calendar.MONTH, mMonth_s);
                        c.set(Calendar.YEAR, mYear_s);
                        long start = c.getTimeInMillis();
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.YEAR, year);
                        long end = c.getTimeInMillis();
                        if(end <= start){
                            Date date = new Date(start);
                            tv_date_e.setText(day.format(date));
                            String[] time_s = tv_time_s.getText().toString().split(":");
                            int mHour_s = Integer.parseInt(time_s[0]);
                            int mMinute_s = Integer.parseInt(time_s[1]);
                            c.set(Calendar.HOUR_OF_DAY, mHour_s);
                            c.set(Calendar.MINUTE, mMinute_s);
                            long start1 = c.getTimeInMillis();
                            String[] time_e = tv_time_e.getText().toString().split(":");
                            int mHour_e = Integer.parseInt(time_e[0]);
                            int mMinute_e = Integer.parseInt(time_e[1]);
                            c.set(Calendar.HOUR_OF_DAY, mHour_e);
                            c.set(Calendar.MINUTE, mMinute_e);
                            long end1 = c.getTimeInMillis();
                            if(end1 <= start1){
                                c.set(Calendar.HOUR_OF_DAY, mHour_s + 1);
                                date = c.getTime();
                                tv_time_e.setText(time.format(date));
                            }
                        }
                        else{
                            Date date = new Date(end);
                            tv_date_e.setText(day.format(date));
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tv_time_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
                String[] time_e = tv_time_e.getText().toString().split(":");
                int mHour = Integer.parseInt(time_e[0]);
                int mMinute = Integer.parseInt(time_e[1]);
                TimePickerDialog timePickerDialog = new TimePickerDialog(Edit_Task.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String[] date_e = tv_date_e.getText().toString().split(" ");
                        int mYear_e = Integer.parseInt(date_e[3]);
                        int mMonth_e = Integer.parseInt(date_e[2]) - 1;
                        int mDay_e = Integer.parseInt(date_e[1]);
                        c.set(Calendar.DAY_OF_MONTH, mDay_e);
                        c.set(Calendar.MONTH, mMonth_e);
                        c.set(Calendar.YEAR, mYear_e);
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        long end = c.getTimeInMillis();
                        String[] date_s = tv_date_s.getText().toString().split(" ");
                        int mYear_s = Integer.parseInt(date_s[3]);
                        int mMonth_s = Integer.parseInt(date_s[2]) - 1;
                        int mDay_s = Integer.parseInt(date_s[1]);
                        c.set(Calendar.DAY_OF_MONTH, mDay_s);
                        c.set(Calendar.MONTH, mMonth_s);
                        c.set(Calendar.YEAR, mYear_s);
                        String[] time_s = tv_time_s.getText().toString().split(":");
                        int mHour_s = Integer.parseInt(time_s[0]);
                        int mMinute_s = Integer.parseInt(time_s[1]);
                        c.set(Calendar.HOUR_OF_DAY, mHour_s);
                        c.set(Calendar.MINUTE, mMinute_s);
                        long start = c.getTimeInMillis();
                        if(end <= start){
                            Date date = new Date(end);
                            tv_time_e.setText(time.format(date));
                            c.set(Calendar.DAY_OF_MONTH, mDay_e + 1);
                            date = c.getTime();
                            tv_date_e.setText(day.format(date));
                        }
                        else{
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            Date date = c.getTime();
                            tv_time_e.setText(time.format(date));
                        }
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        id_task = randomID(Database.TABLE_TASK, Database.COL_TASK_ID);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pos_item == -1)
                    addItemToAdapter();
                else updateItemToAdapter();
                edt_add.setText("");
                adapter_item.notifyDataSetChanged();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == -1){
                    insertTask(id_task);
                    insertItem(id_task);
                    id_task = randomID(Database.TABLE_TASK, Database.COL_TASK_ID);
                }
                else {
                    updateTask(index);
                    deleteItem();
                    updateItem();
                    insertItem(index);
                }
                Intent intent = new Intent(Edit_Task.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ItemClickSupport.addTo(recyclerView_item).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if(items.get(position).isCheck())
                    items.get(position).setCheck(false);
                else items.get(position).setCheck(true);
                items.get(position).setEdit(true);
                adapter_item.notifyItemChanged(position);
            }
        });

        ItemClickSupport.addTo(recyclerView_item).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                pos_item = position;
                edt_add.setText(items.get(position).getTitle());
                if(items.get(position).isCheck())
                    items.get(position).setCheck(false);
                else items.get(position).setCheck(true);
                adapter_item.notifyItemChanged(position);
                return false;
            }
        });

    }

    private void addItemToAdapter(){
        Item it = getItem();
        if(!it.getTitle().equals("")){
            it.setKt(true);
            it.setEdit(false);
            it.setCheck(false);
            it.setDel(false);
            items.add(it);
            int i = items.indexOf(it);
            adapter_item.notifyItemInserted(i);
        }
        else Toast.makeText(Edit_Task.this, "Please enter the title of item.", Toast.LENGTH_SHORT).show();
    }

    private void updateItemToAdapter(){
        String title = edt_add.getText().toString();
        if(!title.equals("")){
            items.get(pos_item).setTitle(title);
            items.get(pos_item).setEdit(true);
            adapter_item.notifyItemChanged(pos_item);
            pos_item = -1;
        }
        else Toast.makeText(Edit_Task.this, "Please enter the title of item.", Toast.LENGTH_SHORT).show();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Task getTask(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Task task = new Task();
        task.setTitle(edt_title.getText().toString());
        String[] date_s = tv_date_s.getText().toString().split(" ");
        int mYear_s = Integer.parseInt(date_s[3]);
        int mMonth_s = Integer.parseInt(date_s[2]) - 1;
        int mDay_s = Integer.parseInt(date_s[1]);
        String[] time_s = tv_time_s.getText().toString().split(":");
        int mHour_s = Integer.parseInt(time_s[0]);
        int mMinute_s = Integer.parseInt(time_s[1]);
        c.set(Calendar.HOUR_OF_DAY, mHour_s);
        c.set(Calendar.MINUTE, mMinute_s);
        c.set(Calendar.DAY_OF_MONTH, mDay_s);
        c.set(Calendar.MONTH, mMonth_s);
        c.set(Calendar.YEAR, mYear_s);
        task.setDate_start(c.getTimeInMillis());
        String[] date_e = tv_date_e.getText().toString().split(" ");
        int mYear_e = Integer.parseInt(date_e[3]);
        int mMonth_e = Integer.parseInt(date_e[2]) - 1;
        int mDay_e = Integer.parseInt(date_e[1]);
        String[] time_e = tv_time_e.getText().toString().split(":");
        int mHour_e = Integer.parseInt(time_e[0]);
        int mMinute_e = Integer.parseInt(time_e[1]);
        c.set(Calendar.HOUR_OF_DAY, mHour_e);
        c.set(Calendar.MINUTE, mMinute_e);
        c.set(Calendar.DAY_OF_MONTH, mDay_e);
        c.set(Calendar.MONTH, mMonth_e);
        c.set(Calendar.YEAR, mYear_e);
        task.setDate_end(c.getTimeInMillis());
        task.setNotify(switchSimple.isChecked());
        return task;
    }

    public void insertTask(int random){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        full.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Task t = getTask();
        Boolean done = true;
        if(!t.getTitle().equals("")){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            long dateS = t.getDate_start();
            long dateE = t.getDate_end();
            ContentValues ct = new ContentValues();
            ct.put(Database.COL_TASK_ID, random);
            ct.put(Database.COL_TASK_TITLE, t.getTitle());
            ct.put(Database.COL_TASK_DATE_S, dateS);
            ct.put(Database.COL_TASK_DATE_E, dateE);
            long Create = System.currentTimeMillis();
            ct.put(Database.COL_TASK_CREAT, Create);
            if(adapter_item.getItemCount() == 0)
                done = false;
            else{
                for(int i = 0; i < adapter_item.getItemCount(); i++ )
                    if(!items.get(i).isCheck()) {
                        done = false;
                        break;
                    }
            }
            ct.put(Database.COL_TASK_DONE, done);
            ct.put(Database.COL_TASK_NOTIFY, t.isNotify());
            sql.insert(Database.TABLE_TASK, null, ct);
            db.close();
            sql.close();
        }
        else Toast.makeText(this, "Please enter the title of task.", Toast.LENGTH_SHORT).show();
    }

    public void updateTask(int id){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Task t = getTask();
        Boolean done = true;
        if(!t.getTitle().equals("")) {
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            long dateS = t.getDate_start();
            long dateE = t.getDate_end();
            ContentValues ct = new ContentValues();
            ct.put(Database.COL_TASK_TITLE, t.getTitle());
            ct.put(Database.COL_TASK_DATE_S, dateS);
            ct.put(Database.COL_TASK_DATE_E, dateE);
            ct.put(Database.COL_TASK_NOTIFY, t.isNotify());
            if(adapter_item.getItemCount() == 0)
                done = false;
            else{
                for(int i = 0; i < adapter_item.getItemCount(); i++ )
                    if(!items.get(i).isCheck()) {
                        done = false;
                        break;
                    }
            }
            ct.put(Database.COL_TASK_DONE, done);
            sql.update(Database.TABLE_TASK, ct, Database.COL_TASK_ID + "=?",
                    new String[] {String.valueOf(Integer.toString(id))});
            db.close();
            sql.close();
        }
        else Toast.makeText(this, "Please enter the title of task.", Toast.LENGTH_SHORT).show();
    }

    public Item getItem(){
        Item it = new Item();
        it.setTitle(edt_add.getText().toString());
        return it;
    }

    public void insertItem(int idTask){
        int count = adapter_item.getItemCount();
        for (int i = 0; i < count; i++){
            if(items.get(i).isKt()){
                Database db = new Database(getApplicationContext());
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues ct = new ContentValues();
                ct.put(Database.COL_ITEM_ID, randomID(Database.TABLE_ITEM, Database.COL_ITEM_ID));
                ct.put(Database.COL_ITEM_ID_TASk, idTask);
                ct.put(Database.COL_ITEM_TITLE, items.get(i).getTitle());
                ct.put(Database.COL_ITEM_CHECKED, items.get(i).isCheck());
                sql.insert(Database.TABLE_ITEM, null, ct);
                items.get(i).setKt(false);
                adapter_item.notifyItemChanged(i);
            }
        }
    }

    public void updateItem(){
        int count = adapter_item.getItemCount();
        for(int i = 0; i < count; i++){
            if(items.get(i).isEdit()){
                Database db = new Database(getApplicationContext());
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues ct = new ContentValues();
                ct.put(Database.COL_ITEM_TITLE, items.get(i).getTitle());
                ct.put(Database.COL_ITEM_CHECKED, items.get(i).isCheck());
                sql.update(Database.TABLE_ITEM, ct, Database.COL_ITEM_ID + "=?",
                        new String[]{String.valueOf(Integer.toString(items.get(i).getId()))});
                db.close();
                sql.close();
                items.get(i).setEdit(false);
                adapter_item.notifyItemChanged(i);
            }
        }
    }

    public void deleteItem(){
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getWritableDatabase();
        int count = delete.size();
        for(int i = 0; i < count; i++) {
            sql.delete(Database.TABLE_ITEM, Database.COL_ITEM_ID + "=?",
                    new String[]{String.valueOf(Integer.toString(delete.get(i)))});
        }
        db.close();
        sql.close();
        delete.clear();
    }

    private void addItemTouchCallback(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListenner() {

            @Override
            public void onMove(int oldPosition, int newPosition) {
                adapter_item.onMove(oldPosition, newPosition);
            }

            @Override
            public void swipe(int position, int direction) {
                delete.add(items.get(position).getId());
                items.remove(position);
                adapter_item.notifyItemRemoved(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void initTime(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        day.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        time.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        Date date = c.getTime();
        tv_date_s.setText(day.format(date));
        tv_date_e.setText(day.format(date));
        tv_time_s.setText(time.format(date));
        String[] time_s = tv_time_s.getText().toString().split(":");
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time_s[0]) + 1);
        date = c.getTime();
        tv_time_e.setText(time.format(date));
    }

    private int randomID(String table, String col) {
        int random = -1;
        Cursor cursor;
        do{
            Database db = new Database(getApplicationContext());
            random = (int) (Math.random() * 999999999 + 1);
            SQLiteDatabase sql1 = db.getReadableDatabase();
            String query = "Select * from " + table + " where " + col + "='" + random +"'";
            cursor = sql1.rawQuery(query, null);
        } while(cursor.getCount() != 0);
        return random;
    }

    private void getAllItem(int id_task){
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        String query = "Select *  from " + Database.TABLE_ITEM + " where " + Database.COL_ITEM_ID_TASk + "=" + id_task;
        Cursor cursor = sql.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Item it = new Item();
            it.setId(cursor.getInt(0));
            it.setId_task(cursor.getInt(1));
            it.setTitle(cursor.getString(2));
            it.setCheck(cursor.getInt(3) > 0);
            it.setKt(false);
            it.setEdit(false);
            cursor.moveToNext();
            items.add(it);
            adapter_item.notifyItemInserted(items.indexOf(it));
        }
        sql.close();
        db.close();
    }
}
