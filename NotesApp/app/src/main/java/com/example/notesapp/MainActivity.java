package com.example.notesapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private final ArrayList<NotesC> notesCArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private NotesC notes;
    private ActivityResultLauncher<Intent> activityResultLauncher;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleActivity);

        recyclerView = findViewById(R.id.recyclerViewId);
        loadJsonFile();
        loadFile();

//        for (int i = 0; i < 20; i++) {
//            notesCArrayList.add(new NotesC("hello" + i , "dkjdkjdhkdhkddd " + i , "dkddkjndd " + i));
//        }



        notesAdapter = new NotesAdapter(notesCArrayList , this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void handleActivity(ActivityResult activityResult){

        Intent dataIntent = activityResult.getData();
        NotesC noteUpdate = new NotesC();

        if(activityResult.getResultCode() == RESULT_OK){
            int pos = dataIntent.getIntExtra("POSITION" , -1);
            noteUpdate = (NotesC)  dataIntent.getSerializableExtra("NOTE_EDIT");

            if(noteUpdate != null){
                if(pos == -1){
                    notesCArrayList.add(new NotesC(noteUpdate.getNoteTitle() , noteUpdate.getNoteText()  ,
                            noteUpdate.getDateTime()));
                    saveData();


                }
            }else{
                notesCArrayList.set(pos, new NotesC(noteUpdate.getNoteTitle() , noteUpdate.getNoteText() ,
                        noteUpdate.getDateTime()));
                saveData();
            }

        }

    }

    @Override
    public void onClick(View view)
    {
        int pos = recyclerView.getChildLayoutPosition(view);
        NotesC notesC = notesCArrayList.get(pos);
        Intent data = new Intent(this, EditActivity.class);
        data.putExtra("noteData", notesC);
        data.putExtra("position", pos);
        startActivity(data);
    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public void loadJsonFile()
    {
        try
        {
            InputStream is = getApplicationContext().openFileInput("Notes.json");
            JsonReader jReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            jReader.beginArray();
            while (jReader.hasNext())
            {
                NotesC notesC = new NotesC();
                jReader.beginObject();
                while (jReader.hasNext())
                {
                    String name = jReader.nextName();
                    switch (name)
                    {
                        case "title":
                            notesC.setNoteTitle(jReader.nextString());
                            break;
                        case "desc":
                            notesC.setNoteText(jReader.nextString());
                            break;
                        case "date":
                            notesC.setDateTime(jReader.nextString());
                            break;
                        default:
                            jReader.skipValue();
                            break;
                    }
                }
                jReader.endObject();
                notesCArrayList.add(notesC);
            }
            jReader.endArray();
        }
        catch (FileNotFoundException ex)
        {
            Toast.makeText(this, "No Data Saved so far.",Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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

    public void saveData(){
        try{
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("Notes.json" , Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(notesCArrayList);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonFile();
        updateTitle();
    }

    public void updateTitle()
    {
        int totalNotes = notesCArrayList.size();
        if (totalNotes != 0)
            setTitle("Android Notes"+ " [" + totalNotes + "]");
        else
            setTitle("Android Notes");
    }


    public void writeJsonFile()
    {
        try
        {
            FileOutputStream fos = getApplicationContext().openFileOutput("Notes.json", Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            jWriter.setIndent(" ");
            jWriter.beginArray();
            for (NotesC notesC : notesCArrayList)
            {
                jWriter.beginObject();
                jWriter.name("noteTitle").value(notesC.getNoteTitle());
                jWriter.name("noteText").value(notesC.getNoteText());
                jWriter.name("noteDateTime").value(notesC.getDateTime());
                jWriter.endObject();
            }
            jWriter.endArray();
            jWriter.close();
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }



    // JSOn Object is kinda giving me some error, I might be doing something wrong.
    // So I found the below function helping me meantime.

//    public void loadJson(){
//
//    try{
//        InputStream inputStream = getApplicationContext().openFileInput("Notes.json");
//        JsonReader reader = new JsonReader(new InputStreamReader(inputStream , StandardCharsets.UTF_8));
//
//        reader.beginArray();
//        while (reader.hasNext()){
//            NotesC notesC = new NotesC(title, content, dateTime);
//            reader.beginObject();
//            while (reader.hasNext()){
//                String name = reader.nextName();
//                switch (name){
//                    case "noteTitle":
//                        notesC.setNoteTitle(reader.nextString());
//                        break;
//
//                    case "noteText":
//                        notesC.setNoteText(reader.nextString());
//                        break;
//
//                    case "dateTime":
//                        notesC.setDateTime(reader.nextString());
//                        break;
//                }
//            }
//            reader.endObject();
//            notesCArrayList.add(notesC);
//
//        }
//            reader.endArray();
//
//
//    } catch (FileNotFoundException e) {
//        Toast.makeText(this, "Data is not saved yet" , Toast.LENGTH_SHORT).show();
//    }catch(Exception e){
//        e.getStackTrace();
//      }
//    }

//    @Override
//    public void onClick(View view){
//        int posIdx = recyclerView.getChildLayoutPosition(view);
//        NotesC notesC = notesCArrayList.get(posIdx);
//
//        Intent data = new Intent(this, EditActivity.class);
//        data.putExtra("noteClassData" , notesC);
//        data.putExtra("positionIndex" , posIdx);
//
//
//    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.activity_main_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.create_new_note_id){
            Intent editIntent = new Intent(this, EditActivity.class);
            startActivity(editIntent);
            return true; }
        else if(item.getItemId() == R.id.infoid){
            Intent infoIntent = new Intent(this, AboutActivity.class);
            startActivity(infoIntent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }



}