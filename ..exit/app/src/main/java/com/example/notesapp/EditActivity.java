package com.example.notesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        getData();
    }

    @Override
    public void onBackPressed(){
        if(!noteTitleId.getText().toString().isEmpty() && !noteTextId.getText().toString().isEmpty()){
                 noteSavedOnClick();
        }else if(noteTitleId.getText().toString().isEmpty() && noteTextId.getText().toString().isEmpty()){
            Toast.makeText(this , "You should enter something.." , Toast.LENGTH_SHORT).show();

        }else if(noteTitleId.getText().toString().isEmpty() && !noteTextId.getText().toString().isEmpty()){
            createAlert();
        }
            super.onBackPressed();
    }

    public void getData(){
        Intent intent = getIntent();
        if(intent.hasExtra("NOTE_EDIT")){

            NotesC notesC = (NotesC) intent.getSerializableExtra("NOTE_EDIT");
            pos = intent.getIntExtra("POSITION" , -1);

            if(notesC != null){
                noteTitleId.setText(notesC.getNoteTitle());
                noteTextId.setText(notesC.getNoteText());

            }
        }else{
            noteTitleId.setText("");
            noteTextId.setText((""));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.activity_edit_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note_id){
            if (noteTitleId.getText().toString().isEmpty() && noteTextId.getText().toString().isEmpty())
            {
                Toast.makeText(this,"There exists no Notes..",Toast.LENGTH_LONG).show();
                createAlert();


            }
            else if (noteTitleId.getText().toString().isEmpty())
            {
                Toast.makeText(this,"No Title for the Note",Toast.LENGTH_LONG).show();
                createAlert();

            }
            else if (noteTextId.getText().toString().isEmpty())
            {
                Toast.makeText(this,"No Content is present...",Toast.LENGTH_LONG).show();
            }else{
                noteSavedOnClick();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void returnData(){
        NotesC notesC = new NotesC();
        Intent intent = new Intent();
        intent.putExtra("NOTE_EDIT" , notesC);
        intent.putExtra("POSITION" , pos);

        notesC.setNoteTitle(noteTitleId.getText().toString());
        notesC.setNoteText(noteTextId.getText().toString());

        notesC.setDateTime("" + new Date());
        setResult(RESULT_OK , intent);
        finish();
    }

    // Function executes after hte note is saved
    public void noteSavedOnClick()
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat ("E MMM dd',' YYYY hh:mm:ss a ");

        Intent data = new Intent();
        data.putExtra("title", noteTitleId.getText().toString());
        data.putExtra("desc",noteTextId.getText().toString());
        data.putExtra("date",dateFormat.format(date));
        Log.d(TAG, "NoteTitle: " + noteTitleId.getText().toString());
        Log.d(TAG , "Date it was stored: " + dateFormat.format(date).toString());
//        if(pos!=-1)
//            data.putExtra("position",pos);
        setResult(RESULT_OK,data);
        Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void createAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                noteSavedOnClick();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                finish();
            }
        });

        builder.setTitle("Note hasn't saved");
        builder.setMessage("Enter the title'"+ noteTitleId.getText()+"' ?");
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}


//    private void saveProduct() {
//
//        Log.d(TAG, "saveProduct: Saving JSON File");
//
//        try {
//            FileOutputStream fos = getApplicationContext().
//                    openFileOutput("Notes.json", Context.MODE_PRIVATE);
//
//            PrintWriter printWriter = new PrintWriter(fos);
//            printWriter.print(notes.toJSON());
//            printWriter.close();
//            fos.close();
//
//            Log.d(TAG, "saveProduct:\n" + notes.toJSON());
//            Toast.makeText(this, "Json File has been saved", Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.getStackTrace();
//        }
//    }

