package com.example.notesapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private final List<NotesC> notesCList;
    private final MainActivity mainActivity;

    public NotesAdapter(List<NotesC> notesCList, MainActivity mainActivity){
        this.notesCList = notesCList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_notes ,
                parent , false);

        // Setting up the OnClick and Long Press Listener, when user performs either one.
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);

        return new NotesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesC notes = notesCList.get(position);
        holder.noteTitleId.setText(notes.getNoteTitle());

        String textDesc = notes.getNoteText();

        // For the Text in the notes, adding the ellipsis for words > 80.
        if (textDesc.length() > 80) {
            String tempText = textDesc.substring(0, 80);
            tempText += "......."; // adding the ellipses
            holder.noteTextId.setText(tempText);
        } else {
            holder.noteTextId.setText(textDesc);
        }

        holder.dateTimeId.setText(notes.getDateTime());
    }


    @Override
    public int getItemCount() {
        return notesCList.size();
    }
}
