package com.sonalgaikwad.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder{

    TextView name;
    TextView date;
    TextView content;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.note_title);
        date = itemView.findViewById(R.id.dateTime);
        content = itemView.findViewById(R.id.note_content);
    }
}
