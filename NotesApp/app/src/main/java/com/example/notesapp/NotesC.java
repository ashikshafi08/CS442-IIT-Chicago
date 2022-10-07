package com.example.notesapp;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.io.IOException;
import java.io.StringWriter;



public class NotesC implements Serializable {

    private String noteTitle;
    private String noteText;
    private String dateTime;
    private static int ctr;

    public NotesC(String title, String content, String dateTime) {
        this.noteTitle = title;
        this.noteText = content;
        this.dateTime = dateTime;


    }

    public NotesC() {
        this.noteTitle = "";
        this.noteText = "";
        this.dateTime = "";
    }

        public String getNoteTitle() {
            return noteTitle;
        }

    public String getNoteText(){
        return noteText;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setDateTime(String dateTime){
        this.dateTime = dateTime;
    }

    @NonNull
    public String toJSON() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();

            jsonWriter.name("noteTitle").value(getNoteTitle());
            jsonWriter.name("noteContent").value(getNoteText());
            jsonWriter.name("noteDateTime").value(getDateTime());

            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}

