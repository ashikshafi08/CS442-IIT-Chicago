package com.sonalgaikwad.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private final List<Note> noteList;
    private final MainActivity mainAct;

    NoteAdapter(List<Note> empList, MainActivity ma) {
        this.noteList = empList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note oNote = noteList.get(position);

        holder.name.setText(oNote.getTitle());
        holder.content.setText(oNote.getContent());
        holder.date.setText(new Date(oNote.getTimestamp()).toString()); //new Date().toString() oNote.getTimestamp()

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
