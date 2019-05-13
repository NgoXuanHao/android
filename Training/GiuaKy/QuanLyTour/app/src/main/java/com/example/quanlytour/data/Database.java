package com.example.quanlytour.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.quanlytour.tour.Tour;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static String DB_NAME = "quanlytour";
    private static int DB_VERSION = 1;
    private static final String TAG = "SQlite";
    //table DMKH
    public static final String TABLE_DMKHACHHANG = "DMKHACHHANG";
    public static final String COL_DMKHACHHANG_MA = "MAKH";
    public static final String COL_DMKHACHHANG_TEN = "TENKH";
    public static final String COL_DMKHACHHANG_DIACHI = "DIACHI";
//    //table PHIEUDANGKY
    public static final String TABLE_PHIEUDANGKY = "PHIEUDANGKY";
    public static final String COL_PHIEUDANGKY_SOPHIEU = "SOPHIEU";
    public static final String COL_PHIEUDANGKY_NGAY = "NGAYDANGKY";
    public static final String COL_PHIEUDANGKY_MAKH = "MAKH";
//    //table CT_PHIEUDK
    public static final String TABLE_CT_PHIEUDK = "CT_PHIEUDK";
    public static final String COL_CT_PHIEUDK_SOPHIEU = "SOPHIEU";
    public static final String COL_CT_PHIEUDK_MATOUR = "MATOUR";
    public static final String COL_CT_PHIEUDK_SONGUOI = "SONGUOI";
//    //table DMTOUR
    public static final String TABLE_TOUR = "DMTOUR";
    public static final String COL_TOUR_MA = "MATOUR";
    public static final String COL_TOUR_LOTRINH = "LOTRINH";
    public static final String COL_TOUR_HANHTRINH = "HANHTRINH";
    public static final String COL_TOUR_GIATOUR = "GIATOUR";

    public Database(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase m_Database) {
        String sqlQueryCreate_DMKHACHHANG = "CREATE TABLE " + TABLE_DMKHACHHANG + " (" +
                COL_DMKHACHHANG_MA + " VARCHAR(10) NOT NULL PRIMARY KEY, " +
                COL_DMKHACHHANG_TEN + " VARCHAR(40), "+
                COL_DMKHACHHANG_DIACHI + " VARCHAR(30) )";
        String sqlQueryCreate_PHIEUDANGKY = "CREATE TABLE " + TABLE_PHIEUDANGKY  + " (" +
                COL_PHIEUDANGKY_SOPHIEU  + " INTEGER NOT NULL PRIMARY KEY, " +
                COL_PHIEUDANGKY_NGAY + " VARCHAR(11), " +
                COL_PHIEUDANGKY_MAKH + " VARCHAR(10), " +
                "FOREIGN KEY (MAKH) REFERENCES DMKHACHHANG(MAKH) )";
        String sqlQueryCreate_CT_PHIEUDK =  "CREATE TABLE " + TABLE_CT_PHIEUDK + " (" +
                COL_CT_PHIEUDK_SOPHIEU + " INT NOT NULL, "+
                COL_CT_PHIEUDK_MATOUR + " VARCHAR(10) NOT NULL, " +
                COL_CT_PHIEUDK_SONGUOI + " INT, " +
                " PRIMARY KEY (SOPHIEU, MATOUR))";
        String sqlQueryCreate_DMTOUR =  "CREATE TABLE " + TABLE_TOUR +" (" +
                COL_TOUR_MA + " VARCHAR(10) NOT NULL PRIMARY KEY, " +
                COL_TOUR_LOTRINH + " VARCHAR(100), " +
                COL_TOUR_HANHTRINH + " INT, " +
                COL_TOUR_GIATOUR + " INT)";
        m_Database.execSQL(sqlQueryCreate_DMTOUR);
        m_Database.execSQL(sqlQueryCreate_CT_PHIEUDK);
        m_Database.execSQL(sqlQueryCreate_PHIEUDANGKY);
        m_Database.execSQL(sqlQueryCreate_DMKHACHHANG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase m_Database, int i, int i1) {
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_DMKHACHHANG );
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_PHIEUDANGKY );
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_CT_PHIEUDK );
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_TOUR );
        // Và tạo lại.
        onCreate(m_Database);
    }
}