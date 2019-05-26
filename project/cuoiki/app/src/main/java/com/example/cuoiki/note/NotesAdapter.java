package com.example.cuoiki.note;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.cuoiki.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {
    private Context context;
    private ArrayList<Notes> notes;
    private ArrayList<Notes> arrayList;
    private NoteEventListener listener;
    private boolean multiCheckMode = false;

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView noteContent, noteTitle, noteDate;
        CheckBox checkBox;
        public NoteHolder(View itemView) {
            super(itemView);
            noteDate = itemView.findViewById(R.id.note_date);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteContent = itemView.findViewById(R.id.note_content);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }


    public NotesAdapter(Context context, ArrayList<Notes> notes) {
        this.context = context;
        this.notes = notes;

        this.arrayList = new ArrayList<Notes>();
        this.arrayList.addAll(notes);
    }



    @Override
    public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteHolder holder, int position) {
        final Notes note = getNote(position);
        //Kiểm tra xem có note trong database để đổ vào all note
        if (note != null) {
            holder.noteTitle.setText(note.getNoteTitle());
            holder.noteDate.setText(NoteUtils.dateFromLong(note.getNoteDate()));

            int start = 0;//cắt bớt chuỗi
            int end  = 100;
            if (note.getNoteContent().length() >= 100) {
                holder.noteContent.setText(note.getNoteContent().substring(start, end) + " ...");
            }else
            {
                holder.noteContent.setText(note.getNoteContent());
            }

            // init note click event
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNoteClick(note);
                }
            });
        }
        // lắng nghe nhấn giữ vào note sẽ chọn checkbox
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onNoteLongClick(note);
                return false;
            }
        });

        // check checkBox if note selected
        if (multiCheckMode) {
            holder.checkBox.setVisibility(View.VISIBLE); // show checkBox if multiMode on
            holder.checkBox.setChecked(note.isChecked());
        } else holder.checkBox.setVisibility(View.GONE); // hide checkBox if multiMode off*/

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private Notes getNote(int position) {
        return notes.get(position);
    }
    /*
     * get All checked notes
     *
     * @return Array
     */

    public List<Notes> getCheckedNotes() {
        List<Notes> checkedNotes = new ArrayList<>();
        for (Notes n : this.notes) {
            if (n.isChecked())
                checkedNotes.add(n);
        }

        return checkedNotes;
    }




    public void setListener(NoteEventListener listener) {
        this.listener = listener;
    }

    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
        if (!multiCheckMode)
            for (Notes note : this.notes) {
                note.setChecked(false);
            }
        notifyDataSetChanged();
    }


    public static class NoteUtils {
        /**
         * @param time that will be convert  and formatted to string
         * @return string
         */
        public static String dateFromLong(long time) {
            DateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy 'at' hh:mm aaa", Locale.US);
            return format.format(new Date(time));
        }
    }

    //filter class
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        notes.clear();
        if(charText.length()==0)
        {
            notes.addAll(arrayList);
        }else
        {
            for(Notes note : arrayList)
            {
                if (note.getNoteTitle().toLowerCase(Locale.getDefault()).contains(charText) || note.getNoteContent().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    notes.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }
}