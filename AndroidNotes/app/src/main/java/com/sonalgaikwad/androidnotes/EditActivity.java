package com.sonalgaikwad.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class EditActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.editTextTitle);
        editContent = findViewById(R.id.editTextContent);

        Intent intent = getIntent();
        if (intent.hasExtra("TITLE")) {
            String sTitle = intent.getStringExtra("TITLE");
            editTitle.setText(String.format("%s", sTitle));
        }
        if (intent.hasExtra("CONTENT")) {
            String sContent = intent.getStringExtra("CONTENT");
            editContent.setText(String.format("%s", sContent));
        }
        if(intent.hasExtra("NOTE_OBJECT"))
        {
            Note oNote = (Note) intent.getSerializableExtra("NOTE_OBJECT");
            editTitle.setText(String.format("%s", oNote.getTitle()));
            editContent.setText(String.format("%s", oNote.getContent()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_save) {
            doSave(findViewById(R.id.menu_save));
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void doSave(View v) {

        String sTitle = editTitle.getText().toString();
        String sContent = editContent.getText().toString();
        Date date = new Date();
        Long lDateTime = date.getTime();//System.currentTimeMillis()/1000;
        if(sTitle == null || sTitle.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   return;
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EditActivity.super.onBackPressed();

                }
            });
            builder.setTitle("Note has no Title");
            builder.setMessage("Go back to Edit the note?");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else {
            String key = "NOTE_OBJECT_NEW";

            Intent intent = getIntent();
            if (intent.hasExtra("NOTE_OBJECT")) {
                key = "NOTE_OBJECT_UPDATED";
            }

            Note oNote = new Note(sTitle, sContent, lDateTime);
            Intent data = new Intent(); // Used to hold results data to be returned to original activity
            data.putExtra(key, oNote);
            if (intent.hasExtra("EDIT_POS")) {
                int pos = intent.getIntExtra("EDIT_POS", 0);
                data.putExtra("UPDATE_POS", pos);
            }
            setResult(RESULT_OK, data);
            finish();// This closes the current activity, returning us to the original activity
        }
    }

    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                doSave(null);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditActivity.super.onBackPressed();
            }
        });
        builder.setTitle("Unsaved Changes");
        builder.setMessage("Do you want to save Note before exiting?");

        AlertDialog dialog = builder.create();
        dialog.show();


    }

}