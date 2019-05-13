package com.example.quanlytour.tour;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.quanlytour.R;
import com.example.quanlytour.data.Database;

import java.util.ArrayList;
import java.util.List;

public class MainTour extends AppCompatActivity {
    ListView listViewTour;
    ArrayList<Tour> data = new ArrayList<>();
    TourAdapter adapterTour = null;
    Button btnInsert, btnUpdate;
    EditText maTour, loTrinh, hanhTrinh, giaTour;
    int index=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour);
        setControl();
        setEvents();
    }

    public void setControl() {
        listViewTour = findViewById(R.id.list_tour);
        btnInsert = findViewById(R.id.btn_insTour);
        btnUpdate = findViewById(R.id.btn_updateTour);
        maTour = findViewById(R.id.edt_maTour);
        loTrinh = findViewById(R.id.edt_lotrin);
        hanhTrinh = findViewById(R.id.edt_hanhtrinh);
        giaTour = findViewById(R.id.edt_giatour);
    }

    public void setEvents() {
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        adapterTour = new TourAdapter(this, R.layout.listview_tour, data);
        listViewTour.setAdapter(adapterTour);
        getAllsTour();
        btnUpdate.setEnabled(false);
        listViewTour.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainTour.this);
                alertDialogBuilder.setMessage("Update or Delete?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteKH();
                                maTour.setEnabled(true);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Tour tour = data.get(index);
                                maTour.setText(tour.getMaTour());
                                maTour.setEnabled(false);
                                loTrinh.setText(tour.getLotrinh());
                                hanhTrinh.setText(Integer.toString(tour.getHanhtrinhTour()));
                                giaTour.setText(Integer.toString(tour.getGiaTour()));
                                btnInsert.setEnabled(false);
                                btnUpdate.setEnabled(true);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTour();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                insertTours();
            }
        });
    }

    private Tour getTour(){
        Tour tour = new Tour();
        tour.setMaTour(maTour.getText().toString());
        tour.setLotrinh(loTrinh.getText().toString());
        boolean check = true;
        try {
            tour.setHanhtrinhTour(Integer.parseInt(hanhTrinh.getText().toString()));
            tour.setGiaTour(Integer.parseInt(giaTour.getText().toString()));
        } catch (NumberFormatException e) {
            check = false;
        }
        if(check)
            return tour;
        else
            return null;
    }

    public void insertTours(){
        ContentValues ct = new ContentValues();
        Tour tour = getTour();
        if(tour != null){
            if(!tour.getMaTour().equals("") && !tour.getLotrinh().equals("")){
                Database db = new Database(getApplicationContext());
                SQLiteDatabase sql1 = db.getWritableDatabase();
                String query = "Select * from " + Database.TABLE_TOUR + " where " + Database.COL_TOUR_MA + "='" + tour.getMaTour() +"'";
                Cursor cursor = sql1.rawQuery(query, null);
                if(cursor.getCount() == 0){
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ct.put(Database.COL_TOUR_MA, tour.getMaTour());
                    ct.put(Database.COL_TOUR_LOTRINH, tour.getLotrinh());
                    ct.put(Database.COL_TOUR_HANHTRINH, tour.getHanhtrinhTour());
                    ct.put(Database.COL_TOUR_GIATOUR, tour.getGiaTour());
                    sql.insert(Database.TABLE_TOUR, null, ct);
                    data.add(tour);
                    adapterTour.notifyDataSetChanged();
                    db.close();
                    sql.close();
                    clear();
                    maTour.setEnabled(true);
                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(this, "Mã tour đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
        }
        else  Toast.makeText(this, "Vui lòng nhập giá tour và hành trình là một số!", Toast.LENGTH_SHORT).show();
    }


    public void deleteKH() {
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            sql.delete(Database.TABLE_TOUR, Database.COL_TOUR_MA + "=?", new String[]{String.valueOf(data.get(index).getMaTour())});
            db.close();
            sql.close();
            data.remove(index);
            adapterTour.notifyDataSetChanged();
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            clear();
        }
        else {Toast.makeText(this, "Xóa không thành công", Toast.LENGTH_SHORT).show();}

    }

    public void updateTour(){
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            String matour = maTour.getText().toString();
            String lotrinh = loTrinh.getText().toString();
            int giatour = -1;
            int hanhtrinh = -1;
            boolean check = true;
            try {
                giatour = Integer.parseInt(giaTour.getText().toString());
                hanhtrinh = Integer.parseInt(hanhTrinh.getText().toString());
            } catch (NumberFormatException e) {
                check = false;
            }
            if(check){
                if(!lotrinh.equals("")){
                    ContentValues ct = new ContentValues();
                    ct.put(Database.COL_TOUR_LOTRINH, lotrinh);
                    ct.put(Database.COL_TOUR_HANHTRINH, hanhtrinh);
                    ct.put(Database.COL_TOUR_GIATOUR, giatour);
                    sql.update(Database.TABLE_TOUR, ct,Database.COL_TOUR_MA + "=?", new String[]{String.valueOf(matour)});
                    db.close();
                    sql.close();
                    data.get(index).setLotrinh(lotrinh);
                    data.get(index).setHanhtrinhTour(hanhtrinh);
                    data.get(index).setGiaTour(giatour);
                    Toast.makeText(this, "Update Thành Công!", Toast.LENGTH_SHORT).show();
                    adapterTour.notifyDataSetChanged();
                    clear();
                    maTour.setEnabled(true);
                    btnInsert.setEnabled(true);
                    btnUpdate.setEnabled(false);
                }
                else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                    maTour.setEnabled(false);
                    btnInsert.setEnabled(false);
                    btnUpdate.setEnabled(true);
                }
            }
            else {Toast.makeText(this, "Vui lòng nhập giá tour và hành trình là một số!", Toast.LENGTH_SHORT).show();}
        }
        else {Toast.makeText(this, "Update không thành công!", Toast.LENGTH_SHORT).show();}
    }

    public void getAllsTour(){
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.query(Database.TABLE_TOUR, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Tour tour = new Tour();
            tour.setMaTour(cursor.getString(0));
            tour.setLotrinh(cursor.getString(1));
            tour.setHanhtrinhTour(cursor.getInt(2));
            tour.setGiaTour(cursor.getInt(3));
            data.add(tour);
            adapterTour.notifyDataSetChanged();
            cursor.moveToNext();
        }
        db.close();
        sql.close();
    }
    public void clear(){
        maTour.getText().clear();
        loTrinh.getText().clear();
        hanhTrinh.getText().clear();
        giaTour.getText().clear();
    }
}
