package com.sonalgaikwad.androidnotes;


import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;

public class Note implements Serializable {
    private  String title;
    private  long timestamp;
    private  String content;

    public Note(String title, String content, long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    long getTimestamp() {
        return timestamp;
    }

    String getContent() {
        return content;
    }


    @NonNull
    @Override
    public String toString() {
        return title + " (" + timestamp+ "), " + content;
    }
}
