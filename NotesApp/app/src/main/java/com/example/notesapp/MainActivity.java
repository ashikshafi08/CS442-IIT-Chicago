package com.example.notesapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private final ArrayList<NotesC> notesCArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private NotesAdapter notesAdapter;
    private NotesC notes;
    int pos;
    int posUpdated;
    private ActivityResultLauncher<Intent> activityResultLauncher;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleActivity);

        recyclerView = findViewById(R.id.recyclerViewId);
//        for (int i = 0; i < 20; i++) {
//            notesCArrayList.add(new NotesC("hello" + i , "dkjdkjdhkdhkddd " + i , "dkddkjndd " + i));
//        }
        linearLayoutManager = new LinearLayoutManager(this);
        notesAdapter = new NotesAdapter(notesCArrayList , this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load file
        loadFile();

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult() ,
                this::handleActivity);

    }

    public void handleActivity(ActivityResult activityResult){



        if(activityResult.getResultCode() == RESULT_OK) {
            Intent data = activityResult.getData();

            if(data == null){
                Toast.makeText(this, "Null is returned" , Toast.LENGTH_SHORT).show();
                return;
            }

            if (data.hasExtra("NEW_NOTE_OBJ")){
                NotesC notesC = (NotesC) data.getSerializableExtra("NEW_NOTE_OBJ");
                notesCArrayList.add(0 , notesC);
                notesAdapter.notifyItemInserted(0);
                linearLayoutManager.scrollToPosition(0);
                setTitle(String.format("Notes App (%d)",notesCArrayList.size()));
            }
            if (data.hasExtra("UPDATED_NOTE_OBJ")){
                NotesC notesC = (NotesC) data.getSerializableExtra("UPDATED_NOTE_OBJ");
                posUpdated = data.getIntExtra("UPDATED_POS" , -1);

                // Getting the updated note from the list
                NotesC updatedNote = notesCArrayList.get(posUpdated);
                notesCArrayList.remove(posUpdated);
                notesAdapter.notifyItemRemoved(posUpdated);
                notesCArrayList.add(0 , notesC);
                notesAdapter.notifyItemInserted(0);
                setTitle(String.format("Notes App (%d)",notesCArrayList.size()));
                linearLayoutManager.scrollToPosition(0);
            }
        }else{
            Log.d(TAG , "RESULT CODE IS NOT OKAY");
        }

    }

    public void openEditActivity(){
        Intent intent = new Intent(this , EditActivity.class);
        activityResultLauncher.launch(intent);
    }

    public void openAboutActivity(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        setTitle(String.format("Notes App (%d)",notesCArrayList.size()));
        super.onResume();
    }


    @Override
    public void onClick(View view)
    {
        pos = recyclerView.getChildLayoutPosition(view);
        NotesC notesC = notesCArrayList.get(pos);
        Intent data = new Intent(this, EditActivity.class);
        data.putExtra("NOTE_OBJ", notesC);
        data.putExtra("EDIT_POS", pos);
        activityResultLauncher.launch(data);
    }

    @Override
    public boolean onLongClick(View view) {
        pos = recyclerView.getChildLayoutPosition(view);
        NotesC note = notesCArrayList.get(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesCArrayList.remove(pos);
                notesAdapter.notifyItemRemoved(pos);
                setTitle(String.format("Notes App (%d)",notesCArrayList.size()));
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setTitle("Delete Note");
        builder.setMessage("Do you want to delete this Note "+ note.getNoteTitle() + "?");
        AlertDialog dialog = builder.create();
        dialog.show();


        return false;
    }


    public void loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream inputStream = getApplicationContext().openFileInput("Notes.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("noteTitle");
                String content = jsonObject.getString("noteText");
                String timeStamp = jsonObject.getString("noteDateTime");
                NotesC notesC = new NotesC(title , content , timeStamp);
                notesCArrayList.add(notesC);
            }


        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No File Present", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }

    public void saveData(){
        String output = toJSON(notesCArrayList);
        try{
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("Notes.json" , Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(output);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public String toJSON(ArrayList<NotesC> notesList) {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");

            jsonWriter.beginArray();
            for (NotesC note : notesList) {
                jsonWriter.beginObject();

                jsonWriter.name("title").value(note.getNoteTitle());
                jsonWriter.name("content").value(note.getNoteText());
                jsonWriter.name("timestamp").value(note.getDateTime());

                jsonWriter.endObject();
            }
            jsonWriter.endArray();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }





    public void updateTitle()
    {
        int totalNotes = notesCArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes"+ " [" + totalNotes + "]");
        else
            setTitle("Android Notes");
    }





    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.activity_main_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.infoid){
            openAboutActivity();
            return true;
        }else if(item.getItemId() == R.id.create_new_note_id){
            openEditActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}