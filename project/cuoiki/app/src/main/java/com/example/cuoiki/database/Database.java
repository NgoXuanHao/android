package com.example.cuoiki.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cuoiki.note.Notes;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static String DB_NAME = "todolist.db";
    private static int DB_VERSION = 1;
    private static final String TAG = "SQlite";

    private static final String TABLE_NOTE = "NOTE";
    private static final String COL_NOTE_ID = "ID";
    private static final String COL_NOTE_TITLE = "TITLE";
    private static final String COL_NOTE_CONTENT = "CONTENT";
    private static final String COL_NOTE_DATE = "DATE";

    public static final String TABLE_TASK = "TASK";
    public static final String COL_TASK_ID = "ID";
    public static final String COL_TASK_TITLE = "TITLE";
    public static final String COL_TASK_DATE_S = "DATE_S";
    public static final String COL_TASK_DATE_E = "DATE_E";
    public static final String COL_TASK_CREAT = "CREAT";
    public static final String COL_TASK_DONE = "DONE";
    public static final String COL_TASK_NOTIFY = "NOTIFY";

    public static final String TABLE_ITEM = "ITEM";
    public static final String COL_ITEM_ID = "ID";
    public static final String COL_ITEM_ID_TASk = "ID_TASK";
    public static final String COL_ITEM_TITLE = "TITLE";
    public static final String COL_ITEM_CHECKED = "CHECKED";

    public Database(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase m_Database) {
        String sqlQueryCreate_Note = "CREATE TABLE " + TABLE_NOTE + " (" +
                COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOTE_TITLE + " TEXT, "+
                COL_NOTE_CONTENT + " TEXT, " +
                COL_NOTE_DATE + " INTEGER )";
        String sqlQueryCreate_TASK = "CREATE TABLE " + TABLE_TASK  + " (" +
                COL_TASK_ID  + " INTEGER , " +
                COL_TASK_TITLE + " TEXT, " +
                COL_TASK_DATE_S + " INTEGER , " +
                COL_TASK_DATE_E + " INTEGER , " +
                COL_TASK_CREAT + " INTERGER , " +
                COL_TASK_DONE + " BOOLEAN , " +
                COL_TASK_NOTIFY + " BOOLEAN )";
        String sqlQueryCreate_TASK_CONTENNT =  "CREATE TABLE " + TABLE_ITEM + " (" +
                COL_ITEM_ID + " INTEGER , "+
                COL_ITEM_ID_TASk + " INTEGER, "+
                COL_ITEM_TITLE + " TEXT, " +
                COL_ITEM_CHECKED + " BOOLEAN)";
        m_Database.execSQL(sqlQueryCreate_Note);
        m_Database.execSQL(sqlQueryCreate_TASK);
        m_Database.execSQL(sqlQueryCreate_TASK_CONTENNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase m_Database, int i, int i1) {
        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE );
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK );
        m_Database.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM );
        // Và tạo lại.
        onCreate(m_Database);
    }

    public List<Notes> getAllNotes() {
        Log.i(TAG, "Database.getAllTours ... " );

        ArrayList<Notes> data = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Notes note = new Notes();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNoteTitle(cursor.getString(1));
                note.setNoteContent(cursor.getString(2));
                note.setNoteDate(Long.parseLong(cursor.getString(3)));

                // Thêm vào danh sách.
                data.add(note);
            } while (cursor.moveToNext());
        }

        // return note list
        return data;
    }

    public Notes getNoteById(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNoteById ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[] { COL_NOTE_ID,
                        COL_NOTE_TITLE, COL_NOTE_CONTENT, COL_NOTE_DATE }, COL_NOTE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Notes note = new Notes(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2),Long.parseLong(cursor.getString(3)));
        // return note
        return note;
    }


    public void addNote(Notes note) {
        Log.i(TAG, "Database.addNote ... " +  note.getNoteTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NOTE_TITLE, note.getNoteTitle());
        values.put(COL_NOTE_CONTENT, note.getNoteContent());
        values.put(COL_NOTE_DATE, note.getNoteDate());

        // Trèn một dòng dữ liệu vào bảng.
        db.insert(TABLE_NOTE, null, values);

        // Đóng kết nối database.
        db.close();
    }


    public int updateNote(Notes note) {
        Log.i(TAG, "Database.updateNote ... "  + "Mã Tour: " + note.getId());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COL_NOTE_TITLE, note.getNoteTitle());
        values.put(COL_NOTE_CONTENT, note.getNoteContent());
        values.put(COL_NOTE_DATE, note.getNoteDate());



        // updating row
        return db.update(TABLE_NOTE, values, COL_NOTE_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Notes note) {
        Log.i(TAG, "Database.deleteNote ... " + "Mã Note: " + note.getId());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COL_NOTE_ID + " = ?",
                new String[] { String.valueOf(note.getId()) });
        db.close();
    }

}