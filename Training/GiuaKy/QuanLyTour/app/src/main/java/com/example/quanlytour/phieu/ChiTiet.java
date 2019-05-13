package com.example.quanlytour.phieu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.quanlytour.R;
import com.example.quanlytour.data.Database;

public class ChiTiet extends AppCompatActivity {
    TextView soPhieu_ct, ngayDK_ct, makh_ct, matour_ct, songuoi_ct;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chitietphieu);
        setControl();
        setEvents();
    }
    public void setControl() {
        soPhieu_ct = findViewById(R.id.tv_sophieu_ct);
        ngayDK_ct = findViewById(R.id.tv_ngaydk_ct);
        makh_ct = findViewById(R.id.tv_maKH_ct);
        matour_ct = findViewById(R.id.tv_maTour_ct);
        songuoi_ct = findViewById(R.id.tv_songuoi_ct);
    }

    public void setEvents() {
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        soPhieu_ct.setText(intent.getStringExtra("sophieu"));
        ngayDK_ct.setText(intent.getStringExtra("ngaydk"));
        String makh = intent.getStringExtra("makh");
        String matour = intent.getStringExtra("matour");
        //makh_ct.setText(intent.getStringExtra("makh"));
        //matour_ct.setText(intent.getStringExtra("matour"));
        Database db = new Database(getApplicationContext());
        SQLiteDatabase sql = db.getReadableDatabase();
        String query = "Select " + Database.COL_DMKHACHHANG_TEN + " from " + Database.TABLE_DMKHACHHANG + " where " + Database.COL_DMKHACHHANG_MA + "='" + makh + "'";
        Cursor cursor = sql.rawQuery(query, null);
        cursor.moveToFirst();
        makh_ct.setText(cursor.getString(0));
        cursor.close();
        String query1 = "Select " + Database.COL_TOUR_LOTRINH + " from " + Database.TABLE_TOUR + " where " + Database.COL_TOUR_MA + "='" + matour + "'";
        cursor = sql.rawQuery(query1, null);
        cursor.moveToFirst();
        matour_ct.setText(cursor.getString(0));
        cursor.close();
        sql.close();
        db.close();
        songuoi_ct.setText(intent.getStringExtra("songuoi"));
    }
}
