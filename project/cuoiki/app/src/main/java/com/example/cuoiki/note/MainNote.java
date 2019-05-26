package com.example.cuoiki.note;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cuoiki.R;
import com.example.cuoiki.database.Database;

import java.util.ArrayList;
import java.util.List;

import static com.example.cuoiki.note.AddOrEditNote.NOTE_EXTRA_Key;

public class MainNote extends AppCompatActivity implements NoteEventListener, SearchView.OnQueryTextListener {
    private static final String TAG = "MainNote";
    private RecyclerView recyclerView;
    private ArrayList<Notes> notes;
    private NotesAdapter adapter;
    private FloatingActionButton fab;
    private int checkedCount = 0;
    private MainActionModeCallback actionModeCallback;
    Database db = new Database(this);
    Object[] filesToExport;
    Object[] filesToDelete;
    int fileBeingExported;
    boolean successful = true;


    public static final int IMPORT = 42;
    public static final int EXPORT = 43;
    public static final int EXPORT_TREE = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_note);


        setControl();


    }
    public void setControl(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //init recyclerView
        recyclerView = findViewById(R.id.notes_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        //init fab button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddNewNote();
            }
        });

        //add input search
    }

    private void loadNotes() {
        this.notes = new ArrayList<>();
        List<Notes> list = db.getAllNotes();// get All notes from DataBase
        this.notes.addAll(list);
        this.adapter = new NotesAdapter(this, this.notes);
        // set listener to adapter
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);

        showEmptyView();// show empty view if empty note
        swipeToDeleteHelper.attachToRecyclerView(recyclerView);//
    }

    @Override
    public void onNoteClick(Notes note) {
        Intent edit = new Intent(this, ViewNote.class);
        edit.putExtra(NOTE_EXTRA_Key, note.getId());
        startActivity(edit);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNoteLongClick(Notes note) {
        note.setChecked(true);
        checkedCount = 1;
        adapter.setMultiCheckMode(true);

        // set new listener to adapter intend off MainActivity listener that we have implement
        adapter.setListener(new NoteEventListener() {
            @Override
            public void onNoteClick(Notes note) {
                note.setChecked(!note.isChecked()); // inverse selected

                if (note.isChecked() )
                    checkedCount++;
                else checkedCount--;

                if (checkedCount == 0) {
                    //  finish multi select mode wen checked count =0
                    actionModeCallback.getAction().finish();
                }

                if (checkedCount > 1) {
                    actionModeCallback.changeShareItemVisible(false);
                } else actionModeCallback.changeShareItemVisible(true);

                actionModeCallback.setCount("Xóa "+ "(" + checkedCount + ")");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNoteLongClick(Notes note) {
            }
        });

        actionModeCallback = new MainActionModeCallback() {
            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_delete_notes)
                    onDeleteMultiNotes();
                if (menuItem.getItemId() == R.id.share_note)
                    onShareNote();
                actionMode.finish();
                return false;
            }

        };

        // start action mode
        startActionMode(actionModeCallback);
        // hide fab button
        fab.setVisibility(View.GONE);
        actionModeCallback.setCount("Xóa "+ "(" + checkedCount + ")");
    }


    // delete multi notes
    private void onDeleteMultiNotes() {
        List<Notes> checkedNotes = adapter.getCheckedNotes();
        if (checkedNotes.size() != 0) {
            for (Notes note : checkedNotes) {
                db.deleteNote(note);
            }
            // refresh Notes
            loadNotes();
            Toast.makeText(this, checkedNotes.size() + " Note(s) Delete successfully !", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "No Note(s) selected", Toast.LENGTH_SHORT).show();

    }

    private void onShareNote() {

        Notes note = adapter.getCheckedNotes().get(0);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String notetext = note.getNoteTitle().toUpperCase() + "\n\n   "+ note.getNoteContent() + "\n\n\n Create on: " +
                NotesAdapter.NoteUtils.dateFromLong(note.getNoteDate()) + "\nBy :" +
                getString(R.string.app_name);
        share.putExtra(Intent.EXTRA_TEXT, notetext);
        startActivity(share);
    }




    @SuppressLint("RestrictedApi")
    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);

        adapter.setMultiCheckMode(false); // uncheck the notes
        adapter.setListener(this); // set back the old listener
        fab.setVisibility(View.VISIBLE);
    }

    //Resume Activity
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    public void onAddNewNote(){
        startActivity(new Intent(this, AddOrEditNote.class));
    }


    //when no notes show message No note!
    private void showEmptyView() {
        if (notes.size() == 0) {
            this.recyclerView.setVisibility(View.GONE);
            findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);

        } else {
            this.recyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.empty_notes_view).setVisibility(View.GONE);
        }
    }

    // swipe to right or to left te delete
    private ItemTouchHelper swipeToDeleteHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    //delete note when swipe

                    if (notes != null) {
                        // get swiped note
                        Notes swipedNote = notes.get(viewHolder.getAdapterPosition());
                        if (swipedNote != null) {
                            swipeToDelete(swipedNote, viewHolder);

                        }

                    }
                }
            });

    private void swipeToDelete(final Notes swipedNote, final RecyclerView.ViewHolder viewHolder) {
        new AlertDialog.Builder(MainNote.this)
                .setMessage(R.string.mess_del_note)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete note
                        db.deleteNote(swipedNote);
                        notes.remove(swipedNote);
                        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        showEmptyView();

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Undo swipe and restore swipedNote
                        recyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());


                    }
                })
                .setCancelable(false)
                .create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText.trim());
        return false;
    }
}
