package com.example.quanlytour.khach_hang;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
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

public class MainKH extends AppCompatActivity {
    ListView listViewKH;
    ArrayList<KhachHang> data = new ArrayList<>();
    AdapterKH adapterkh = null;
    Button btnInsert, btnUpdate;
    EditText maKH, tenKH, diaChi;
    int index=-1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.khachhang);
        setControl();
        setEvents();
    }
    public void setControl() {
        listViewKH = findViewById(R.id.list_KH);
        btnInsert = findViewById(R.id.btn_insKH);
        btnUpdate = findViewById(R.id.btn_updateKH);
        maKH = findViewById(R.id.edt_maKH);
        tenKH = findViewById(R.id.edt_tenKH);
        diaChi = findViewById(R.id.edt_diachi);
    }

    public void setEvents() {
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        adapterkh = new AdapterKH(this, R.layout.listview_khachhang, data);
        listViewKH.setAdapter(adapterkh);
        getAlls();
        btnUpdate.setEnabled(false);
        listViewKH.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainKH.this);
                alertDialogBuilder.setMessage("Update or Delete?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteKH();
                                maKH.setEnabled(true);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                KhachHang kh = data.get(index);
                                maKH.setText(kh.getMaKH());
                                maKH.setEnabled(false);
                                tenKH.setText(kh.getTenKH());
                                diaChi.setText(kh.getDiaChi());
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
                updateKH();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                insertKH();
            }
        });
    }
    private KhachHang getKH(){
        KhachHang kh = new KhachHang();
        kh.setMaKH(maKH.getText().toString());
        kh.setTenKH(tenKH.getText().toString());
        kh.setDiaChi(diaChi.getText().toString());
        return kh;
    }

    public void insertKH(){
        KhachHang kh = getKH();
        if(!kh.getMaKH().equals("") && !kh.getTenKH().equals("") && !kh.getDiaChi().equals("")){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql1 = db.getReadableDatabase();
            String query = "Select * from " + Database.TABLE_DMKHACHHANG + " where " + Database.COL_PHIEUDANGKY_MAKH + "='" + kh.getMaKH() +"'";
            Cursor cursor = sql1.rawQuery(query, null);
            if(cursor.getCount() == 0){
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues ct = new ContentValues();
                ct.put(Database.COL_DMKHACHHANG_MA, kh.getMaKH());
                ct.put(Database.COL_DMKHACHHANG_TEN, kh.getTenKH());
                ct.put(Database.COL_DMKHACHHANG_DIACHI, kh.getDiaChi());
                sql.insert(Database.TABLE_DMKHACHHANG, null, ct);
                data.add(kh);
                adapterkh.notifyDataSetChanged();
                db.close();
                sql.close();
                clear();
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                maKH.setEnabled(true);
            }
            else  Toast.makeText(this, "Mã Khách hàng đã tồn tại!", Toast.LENGTH_SHORT).show();
            sql1.close();
        }
        else Toast.makeText(this, "Vui lòng điền đầy đủ các trường!", Toast.LENGTH_SHORT).show();
    }

    public void deleteKH() {
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            sql.delete(Database.TABLE_DMKHACHHANG, Database.COL_DMKHACHHANG_MA + "=?", new String[]{String.valueOf(data.get(index).getMaKH())});
            db.close();
            sql.close();
            data.remove(index);
            adapterkh.notifyDataSetChanged();
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            clear();
        }
        else {Toast.makeText(this, "Xóa không thành công", Toast.LENGTH_SHORT).show();}

    }

    public void updateKH(){
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            String maKh = maKH.getText().toString();
            String tenKh = tenKH.getText().toString();
            String diachi = diaChi.getText().toString();
            if(!tenKh.equals("") && !diachi.equals("")){
                ContentValues ct = new ContentValues();
                ct.put(Database.COL_DMKHACHHANG_TEN, tenKh);
                ct.put(Database.COL_DMKHACHHANG_DIACHI, diachi);
                sql.update(Database.TABLE_DMKHACHHANG, ct,Database.COL_DMKHACHHANG_MA + "=?", new String[]{String.valueOf(maKh)});
                db.close();
                sql.close();
                data.get(index).setTenKH(tenKh);
                data.get(index).setDiaChi(diachi);
                Toast.makeText(this, "Update Thành Công!", Toast.LENGTH_SHORT).show();
                adapterkh.notifyDataSetChanged();
                clear();
                maKH.setEnabled(true);
                btnInsert.setEnabled(true);
                btnUpdate.setEnabled(false);
            }
            else {
                Toast.makeText(this, "Vui lòng điền đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                maKH.setEnabled(false);
                btnInsert.setEnabled(false);
                btnUpdate.setEnabled(true);
            }
        }
        else {Toast.makeText(this, "Update không thành công!", Toast.LENGTH_SHORT).show();}
    }

    public void getAlls(){
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.query(Database.TABLE_DMKHACHHANG, null, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            KhachHang kh = new KhachHang();
            kh.setMaKH(cursor.getString(0));
            kh.setTenKH(cursor.getString(1));
            kh.setDiaChi(cursor.getString(2));
            data.add(kh);
            adapterkh.notifyDataSetChanged();
            cursor.moveToNext();
        }
        db.close();
        sql.close();
    }

    public void clear(){
        maKH.getText().clear();
        tenKH.getText().clear();
        diaChi.getText().clear();
    }
}
