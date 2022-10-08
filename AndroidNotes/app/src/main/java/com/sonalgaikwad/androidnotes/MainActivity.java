package com.sonalgaikwad.androidnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener , View.OnLongClickListener{

    private static final String TAG = "MainActivity";
    private final ArrayList<Note> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Note note;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(String.format("Android Notes (%d)",notesList.size()));
        recyclerView = findViewById(R.id.recycler);

        mAdapter = new NoteAdapter(notesList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        loadFile();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleResult);
    }

    public void handleResult(ActivityResult result) {

        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();

            if (data == null) {
                Toast.makeText(this, "Null text value returned", Toast.LENGTH_SHORT).show();
                return;
            }
            if(data.hasExtra("NOTE_OBJECT_NEW"))
            {
                Note oNote = (Note) data.getSerializableExtra("NOTE_OBJECT_NEW");
                Log.d(TAG, "onActivityResult: NOTE_OBJECT_NEW " );
                notesList.add(0, oNote);
                mAdapter.notifyItemInserted(0);
                linearLayoutManager.scrollToPosition(0); //list.size()-1 for end position
                setTitle(String.format("Android Notes (%d)",notesList.size()));

            }
            if(data.hasExtra("NOTE_OBJECT_UPDATED"))
            {
                Note oNote = (Note) data.getSerializableExtra("NOTE_OBJECT_UPDATED");
                int posUpdate = data.getIntExtra("UPDATE_POS",-1);
                Log.d(TAG, "onActivityResult:NOTE_OBJECT_UPDATED " );
                Note nUpdate = notesList.get(posUpdate);
                notesList.remove(posUpdate);
                mAdapter.notifyItemRemoved(posUpdate);
                notesList.add(0,oNote);
                mAdapter.notifyItemInserted(0);
                setTitle(String.format("Android Notes (%d)",notesList.size()));
                linearLayoutManager.scrollToPosition(0);
            }

        } else {
            Log.d(TAG, "onActivityResult: result Code: " + result.getResultCode());
        }
    }


    public void openEditActivity(View view)
    {
        Intent intent = new Intent(this, EditActivity.class);
        activityResultLauncher.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_about) {
            openAboutActivity(findViewById(R.id.menu_about));
            return true;
        } else if (item.getItemId() == R.id.menu_edit) {
            openEditActivity(findViewById(R.id.menu_edit));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void openAboutActivity(View viewById) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() { // After Pause or Stop

        setTitle(String.format("Android Notes (%d)",notesList.size()));

        super.onResume();
    }

    private void loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for(int iIterator = 0; iIterator < jsonArray.length(); iIterator++)
            {
                JSONObject jsonObject =  jsonArray.getJSONObject(iIterator);
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                long timestamp = jsonObject.getLong("timestamp");
                notesList.add(new Note(title, content, timestamp));
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() { // Going to be partially or fully hidden
        Log.d(TAG, "onPause:");
        saveNote();

        super.onPause();
    }

    private void saveNote() {

        Log.d(TAG, "saveProduct: Saving JSON File");
        String output = toJSON(notesList);
        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(output);
            printWriter.close();
            fos.close();

            Log.d(TAG, "saveNote:\n" + output);
          //  Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @NonNull
    public String toJSON(ArrayList<Note> notesList) {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");

            jsonWriter.beginArray();
                for (Note n : notesList) {
                    jsonWriter.beginObject();

                    jsonWriter.name("title").value(n.getTitle());
                    jsonWriter.name("content").value(n.getContent());
                    jsonWriter.name("timestamp").value(n.getTimestamp());

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


    @Override
    public void onClick(View view) {

        pos = recyclerView.getChildLayoutPosition(view);
        Note n = notesList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE_OBJECT", n);
        intent.putExtra("EDIT_POS",pos);
        activityResultLauncher.launch(intent);
        Log.d(TAG, "onClick: "+n.getTitle());
    }

    @Override
    public boolean onLongClick(View view) {
        pos = recyclerView.getChildLayoutPosition(view);
        Note n = notesList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                notesList.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                setTitle(String.format("Android Notes (%d)",notesList.size()));
                Log.d(TAG, "onLongClick: removed Note "+n.getTitle());
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        builder.setTitle("Delete Note");
        builder.setMessage("Do you want to delete Note "+ n.getTitle());

        AlertDialog dialog = builder.create();
        dialog.show();

        return true; //if false then longClick also calls onClick function
    }


}