package com.example.cuoiki.note;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cuoiki.R;
import com.example.cuoiki.database.Database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ViewNote extends AppCompatActivity {
    private TextView viewTitle, viewContent;
    public static final String NOTE_EXTRA_Key = "note_id";
    private Notes temp;
    private NotesAdapter adapter;
    Database db = new Database(this);

    private static final int REQUEST_ID_WRITE_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);


        setControl();
        loadInfo();

    }




    public void loadInfo(){
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = db.getNoteById(id);
            viewTitle.setText(temp.getNoteTitle());
            viewContent.setText(temp.getNoteContent());
        } else {
            viewTitle.setFocusable(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public void setControl() {
        viewTitle = findViewById(R.id.view_noteTitle);
        viewContent = findViewById(R.id.view_noteContent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_note)
            onEditNote();
        if (id == R.id.download_note)
            askPermissionAndWriteFile();
        if (id == R.id.share_note)
            onShareNote();
        return super.onOptionsItemSelected(item);
    }
    private void onSaveNote(){
        String title = viewTitle.getText().toString();
        String content = viewContent.getText().toString();
        try {
            File path = new File(Environment.getExternalStorageState());
            File myFile = new File(path, title + ".txt");
            FileOutputStream fOut = new FileOutputStream(myFile,true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(this,"Text file Saved !",Toast.LENGTH_LONG).show();
        }
        catch (java.io.IOException e) {

            //do something if an IOException occurs.
            Toast.makeText(this,"ERROR - Text could't beadded",Toast.LENGTH_LONG).show();
        }
    }
    private void onShareNote() {
        int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
        temp = db.getNoteById(id);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext =temp.getNoteTitle().toUpperCase() + "\n\n   "+ temp.getNoteContent() + "\n\n\nCreate on : " +
                NotesAdapter.NoteUtils.dateFromLong(temp.getNoteDate()) + "\nBy: " +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }


    private void onEditNote(){
        int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
        temp = db.getNoteById(id);
        Intent edit = new Intent(this, AddOrEditNote.class);
        edit.putExtra(NOTE_EXTRA_Key, temp.getId());
        startActivity(edit);

    }

    private void askPermissionAndWriteFile() {
        boolean canWrite = this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canWrite) {
            this.writeFile();
        }
    }

    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Kiểm tra quyền
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {

                // Nếu không có quyền, cần nhắc người dùng cho phép.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }


    private void writeFile() {
        String title = viewTitle.getText().toString();
        String content = viewContent.getText().toString();

        String fileName = title + ".txt";
        // Thư mục gốc của SD Card.
        File extStore = Environment.getExternalStorageDirectory();
        // ==> /storage/emulated/0/note.txt
        String path = extStore.getAbsolutePath() + "/" + fileName;
        Log.i("ExternalStorageDemo", "Save to: " + path);



        try {
            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);
            myOutWriter.close();
            fOut.close();

            Toast.makeText(getApplicationContext(), "Saved to " + path , Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
