package com.example.notesapp;

import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    TextView noteTitleId;
    TextView noteTextId;
    TextView dateTimeId;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        noteTitleId = itemView.findViewById(R.id.noteTitleId);
        noteTextId = itemView.findViewById(R.id.notesTextId);
        dateTimeId = itemView.findViewById(R.id.dateTimeId);
    }


}
