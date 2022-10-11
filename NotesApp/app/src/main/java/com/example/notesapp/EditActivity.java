package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class EditActivity extends AppCompatActivity {

    public static final String TAG = "EditActivity";
    private EditText noteTitleId;
    private EditText noteTextId;
    private NotesC notes;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitleId = findViewById(R.id.noteTitleId);
        noteTextId = findViewById(R.id.notesTextId);

        Intent intent = getIntent();
        // Note Title == Title
        if (intent.hasExtra("NOTE_TITLE")){
            String noteTitle = intent.getStringExtra("NOTE_TITLE");
            noteTitleId.setText(String.format("%s" , noteTitle));
        }
        // Note content == title
        if (intent.hasExtra("NOTE_CONTENT")){
            String noteText = intent.getStringExtra("NOTE_CONTENT");
            noteTextId.setText(String.format("%s" , noteText));
        }

        // Note Object == Note OBJ
        if (intent.hasExtra("NOTE_OBJ")){
            NotesC note = (NotesC) intent.getSerializableExtra("NOTE_OBJ");
            noteTitleId.setText(note.getNoteTitle());
            noteTextId.setText(note.getNoteText());


        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Setting up the builder
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteSave();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditActivity.super.onBackPressed();
            }
        });

        // Setting up the message and title
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save the note before the exit?");

        // Initializing the dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Menu saveNote = findViewById(R.id.save_note_id);

        if (item.getItemId()== R.id.save_note_id){
            noteSave();
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }


    // Function executes after hte note is saved
    public void noteSave() {
        String noteTitle = noteTitleId.getText().toString();
        String noteContent = noteTextId.getText().toString();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd',' YYYY hh:mm:ss a ");
        String dateTime = dateFormat.format(date);

        // If the title is empty or null do the following..
        if (noteTitle.isEmpty() || noteTitle == null) {

            // Bring a alert for the user
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Activate the onBackPressed
                    EditActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Make the user stay in the Edit Text for adding the title
                    return;
                }
            });

            builder.setTitle("The Note has no title");
            builder.setMessage("Continue without the title?");

            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            String newKey = "NEW_NOTE_OBJ";
            // Getting the intent
            Intent intent = getIntent();

            if (intent.hasExtra("NOTE_OBJ")) {
                newKey = "UPDATED_NOTE_OBJ";
            }

            NotesC note = new NotesC(noteTitle, noteContent, dateTime);
            Intent data = new Intent();
            data.putExtra(newKey, note);

            if (intent.hasExtra("EDIT_POS")) {
                pos = intent.getIntExtra("EDIT_POS", 0);
                data.putExtra("UPDATED_POS", pos);
            }
            setResult(RESULT_OK, data);
            finish();
        }

    }


}

