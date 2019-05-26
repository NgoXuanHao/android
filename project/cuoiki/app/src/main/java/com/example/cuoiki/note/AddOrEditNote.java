package com.example.cuoiki.note;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cuoiki.R;
import com.example.cuoiki.database.Database;

import java.util.Date;

public class AddOrEditNote extends AppCompatActivity {
    public Button btnCancel;
    private EditText inputTitle, inputContent;
    TextView action;
    public static final String NOTE_EXTRA_Key = "note_id";

    private Notes temp;
    Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_note);

        setEvents();

        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(NOTE_EXTRA_Key, 0);
            temp = db.getNoteById(id);
            inputTitle.setText(temp.getNoteTitle());
            inputContent.setText(temp.getNoteContent());
            action.setText(R.string.edit_note);
        } else {
            action.setText(R.string.add_new_note);
            inputTitle.setFocusable(true);
            inputContent.setFocusable(true);
        }

    }

    private void setEvents(){
        btnCancel = findViewById(R.id.btnCancel);
        inputTitle = findViewById(R.id.input_noteTitle);
        inputContent = findViewById(R.id.input_noteContent);
        action = findViewById(R.id.action);

        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edite_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_note)
            onSaveNote();
        return super.onOptionsItemSelected(item);
    }

    private void onSaveNote() {
        // TODO:  Save Note

        String title = this.inputTitle.getText().toString();
        String content = this.inputContent.getText().toString();
        long date = new Date().getTime(); // get  system time
        if(  title.equals("") || content.equals("")) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Oops!!");
            b.setMessage(R.string.mess_note);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            b.create().show();

            return;
        }

        // if  exist update els crete new
        if (temp == null) {
            this.temp = new Notes(title, content, date);
            db.addNote(temp); // create new note and inserted to database
        } else {
            this.temp.setNoteTitle(title);
            this.temp.setNoteContent(content);
            this.temp.setNoteDate(date);
            db.updateNote(temp); // change text and date and update note on database
        }

        this.onBackPressed();

    }
}