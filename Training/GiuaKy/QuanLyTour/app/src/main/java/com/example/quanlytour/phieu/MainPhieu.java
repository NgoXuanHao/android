package com.example.quanlytour.phieu;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlytour.R;
import com.example.quanlytour.data.Database;
import com.example.quanlytour.khach_hang.KhachHang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainPhieu extends AppCompatActivity {
    ListView listviewPhieu;
    ArrayList<Phieu> data = new ArrayList<>();
    PhieuAdapter adapterphieu = null;
    List<String> listKH = new ArrayList<>();
    ArrayAdapter<String> adapter_maKh = null;
    List<String> listTour =  new ArrayList<>();
    ArrayAdapter<String> adapter_tour = null;
    Button btnInsert, btnUpdate;
    Spinner maTourPhieu, maKHPhieu;
    EditText soPhieu, soNguoi;
    TextView ngayDK;
    int mYear, mMonth, mDay;
    int index=-1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phieudangky);
        setControl();
        setEvents();
    }
    public void setControl() {
        listviewPhieu = findViewById(R.id.list_phieu);
        btnInsert = findViewById(R.id.btn_insPhieu);
        btnUpdate = findViewById(R.id.btn_updatePhieu);
        soPhieu = findViewById(R.id.edt_sophieu);
        ngayDK = findViewById(R.id.tv_dateTime);
        maTourPhieu = findViewById(R.id.spi_maTour);
        maKHPhieu = findViewById(R.id.spi_maKH);
        soNguoi = findViewById(R.id.edt_soNguoi);
    }

    public void setEvents() {
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        spinnerKH();
        spinnerTour();
        btnUpdate.setEnabled(false);
        adapterphieu = new PhieuAdapter(this, R.layout.listview_phieu, data);
        listviewPhieu.setAdapter(adapterphieu);
        getAllsPhieu();
        ngayDK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainPhieu.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                ngayDK.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        listviewPhieu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Phieu phieu = data.get(position);
                index = position;
                String mtour = listTour.get(Integer.parseInt(phieu.getMaTour_phieu()));
                String mkh = listKH.get(Integer.parseInt(phieu.getMaKH_phieu()));
                Intent intent = new Intent(MainPhieu.this, ChiTiet.class);
                intent.putExtra("sophieu", Integer.toString(phieu.getSoPhieu()));
                intent.putExtra("ngaydk", phieu.getNgayDk());
                intent.putExtra("matour", mtour);
                intent.putExtra("makh", mkh);
                intent.putExtra("songuoi",Integer.toString(phieu.getSoNguoi()));
                startActivity(intent);
            }
        });

        listviewPhieu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainPhieu.this);
                alertDialogBuilder.setMessage("Update or Delete?");
                alertDialogBuilder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteKH();
                                soPhieu.setEnabled(true);
                                maTourPhieu.setEnabled(true);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Phieu phieu = data.get(index);
                                soPhieu.setText(Integer.toString(phieu.getSoPhieu()));
                                soPhieu.setEnabled(false);
                                maTourPhieu.setSelection(Integer.parseInt(phieu.getMaTour_phieu()));
                                maKHPhieu.setSelection(Integer.parseInt(phieu.getMaKH_phieu()));
                                ngayDK.setText(phieu.getNgayDk());
                                soNguoi.setText(Integer.toString(phieu.getSoNguoi()));
                                maTourPhieu.setEnabled(false);
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
                updatePhieu();
            }
        });
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                insertPhieu();
            }
        });

    }

    private Phieu getPhieu(){
        Phieu phieu = new Phieu();
        phieu.setMaKH_phieu(Integer.toString(maKHPhieu.getSelectedItemPosition()));
        phieu.setMaTour_phieu(Integer.toString(maTourPhieu.getSelectedItemPosition()));
        phieu.setNgayDk(ngayDK.getText().toString());
        boolean check = true;
        try {
            phieu.setSoPhieu(Integer.parseInt(soPhieu.getText().toString()));
            phieu.setSoNguoi(Integer.parseInt(soNguoi.getText().toString()));
        } catch (NumberFormatException e) {
            check = false;
        }
        if(check)
            return phieu;
        else
            return null;

    }

    public void insertPhieu(){
        ContentValues ct = new ContentValues();
        Phieu phieu = getPhieu();
        if(phieu != null){
            if(!phieu.getMaTour_phieu().equals("-1") && !phieu.getMaKH_phieu().equals("-1")){
                if(!phieu.getNgayDk().equals("")){
                    Database db = new Database(getApplicationContext());
                    SQLiteDatabase sql1 = db.getReadableDatabase();
                    String query = "Select * from " + Database.TABLE_PHIEUDANGKY + " where " + Database.COL_PHIEUDANGKY_SOPHIEU + "=" + phieu.getSoPhieu();
                    Cursor cursor = sql1.rawQuery(query, null);
                    if(cursor.getCount() == 0){
                        SQLiteDatabase sql = db.getWritableDatabase();
                        ct.put(Database.COL_PHIEUDANGKY_SOPHIEU, phieu.getSoPhieu());
                        ct.put(Database.COL_PHIEUDANGKY_NGAY, phieu.getNgayDk());
                        ct.put(Database.COL_PHIEUDANGKY_MAKH, phieu.getMaKH_phieu());
                        sql.insert(Database.TABLE_PHIEUDANGKY, null, ct);
                        ContentValues ct1 = new ContentValues();
                        ct1.put(Database.COL_CT_PHIEUDK_SOPHIEU, phieu.getSoPhieu());
                        ct1.put(Database.COL_CT_PHIEUDK_MATOUR, phieu.getMaTour_phieu());
                        ct1.put(Database.COL_CT_PHIEUDK_SONGUOI, phieu.getSoNguoi());
                        sql.insert(Database.TABLE_CT_PHIEUDK, null, ct1);
                        data.add(phieu);
                        adapterphieu.notifyDataSetChanged();
                        db.close();
                        sql.close();
                        clear();
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(this, "Số phiếu đã tồn tại!", Toast.LENGTH_SHORT).show();
                    sql1.close();
                }
                else  Toast.makeText(this, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Vui lòng thêm khách hàng và tour trước khi thêm phiếu!", Toast.LENGTH_SHORT).show();
        }
        else  Toast.makeText(this, "Vui lòng nhập số phiếu và số người là một số!", Toast.LENGTH_SHORT).show();
    }

    public void deleteKH() {
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            sql.delete(Database.TABLE_PHIEUDANGKY, Database.COL_PHIEUDANGKY_SOPHIEU + "=?", new String[]{String.valueOf(data.get(index).getSoPhieu())});
            sql.delete(Database.TABLE_CT_PHIEUDK, Database.COL_CT_PHIEUDK_SOPHIEU + "=? and " + Database.COL_CT_PHIEUDK_MATOUR + "=?", new String[]{String.valueOf(data.get(index).getSoPhieu()), String.valueOf(data.get(index).getMaTour_phieu())});
            db.close();
            sql.close();
            data.remove(index);
            adapterphieu.notifyDataSetChanged();
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            clear();
        }
        else {Toast.makeText(this, "Xóa không thành công", Toast.LENGTH_SHORT).show();}
    }

    public void updatePhieu(){
        if(index >= 0){
            Database db = new Database(getApplicationContext());
            SQLiteDatabase sql = db.getWritableDatabase();
            String ngaydk = ngayDK.getText().toString();
            String matour_phieu = Integer.toString(maKHPhieu.getSelectedItemPosition());
            String makh_phieu = Integer.toString(maKHPhieu.getSelectedItemPosition());
            int sophieu = -1;
            int songuoi = -1;
            boolean check = true;
            try {
                sophieu = Integer.parseInt(soPhieu.getText().toString());
                songuoi = Integer.parseInt(soNguoi.getText().toString());
            } catch (NumberFormatException e) {
                check = false;
            }
            if(check){
                if(!ngaydk.equals("") && !soPhieu.getText().toString().equals("")){
                    ContentValues ct = new ContentValues();
                    ct.put(Database.COL_PHIEUDANGKY_NGAY, ngaydk);
                    ct.put(Database.COL_PHIEUDANGKY_MAKH, makh_phieu);
                    sql.update(Database.TABLE_PHIEUDANGKY, ct,Database.COL_PHIEUDANGKY_SOPHIEU + "=?", new String[]{String.valueOf(sophieu)});
                    ContentValues ct1 = new ContentValues();
                    ct1.put(Database.COL_CT_PHIEUDK_SONGUOI, songuoi);
                    sql.update(Database.TABLE_CT_PHIEUDK, ct1,Database.COL_CT_PHIEUDK_SOPHIEU + "=? and " + Database.COL_CT_PHIEUDK_MATOUR + "=?", new String[]{String.valueOf(sophieu), String.valueOf(matour_phieu)});
                    db.close();
                    sql.close();
                    data.get(index).setNgayDk(ngaydk);
                    data.get(index).setSoNguoi(songuoi);
                    data.get(index).setMaKH_phieu(makh_phieu);
                    Toast.makeText(this, "Update Thành Công!", Toast.LENGTH_SHORT).show();
                    adapterphieu.notifyDataSetChanged();
                    clear();
                    soPhieu.setEnabled(true);
                    maTourPhieu.setEnabled(true);
                    btnInsert.setEnabled(true);
                    btnUpdate.setEnabled(false);
                }
                else Toast.makeText(this, "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
            }
            else {Toast.makeText(this, "Vui lòng nhập giá số phiếu và số người là một số!", Toast.LENGTH_SHORT).show();}
        }
        else {Toast.makeText(this, "Update không thành công!", Toast.LENGTH_SHORT).show();}
    }

    public void getAllsPhieu(){
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        String query = "SELECT PHIEUDANGKY.SOPHIEU, PHIEUDANGKY.NGAYDANGKY, PHIEUDANGKY.MAKH, CT_PHIEUDK.MATOUR, CT_PHIEUDK.SONGUOI FROM 'CT_PHIEUDK' LEFT JOIN 'PHIEUDANGKY' on CT_PHIEUDK.SOPHIEU = PHIEUDANGKY.SOPHIEU";
        Cursor cursor;
        cursor = sql.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Phieu phieu = new Phieu();
            phieu.setSoPhieu(cursor.getInt(0));
            phieu.setNgayDk(cursor.getString(1));
            phieu.setMaKH_phieu(cursor.getString(2));
            phieu.setMaTour_phieu(cursor.getString(3));
            phieu.setSoNguoi(cursor.getInt(4));
            data.add(phieu);
            adapterphieu.notifyDataSetChanged();
            cursor.moveToNext();
        }
        db.close();
        sql.close();
    }

    private void spinnerKH(){
        adapter_maKh = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listKH);
        adapter_maKh.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        maKHPhieu.setAdapter(adapter_maKh);
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.query(Database.TABLE_DMKHACHHANG, new String[]{String.valueOf(Database.COL_DMKHACHHANG_MA)}, null, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            listKH.add(cursor.getString(0));
            adapter_maKh.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }

    private void spinnerTour(){
        adapter_tour = new ArrayAdapter(this, android.R.layout.simple_spinner_item,listTour);
        adapter_tour.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        maTourPhieu.setAdapter(adapter_tour);
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.query(Database.TABLE_TOUR, new String[]{String.valueOf(Database.COL_TOUR_MA)}, null, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            listTour.add(cursor.getString(0));
            adapter_tour.notifyDataSetChanged();
            cursor.moveToNext();
        }
    }

    public void clear(){
        soPhieu.getText().clear();
        maKHPhieu.setSelection(0);
        soNguoi.getText().clear();
        maTourPhieu.setSelection(0);
        ngayDK.setText("");
    }
}
