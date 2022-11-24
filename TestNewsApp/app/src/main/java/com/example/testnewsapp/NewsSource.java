package com.example.testnewsapp;

import java.util.ArrayList;
import java.util.List;

public class NewsSource {

    private String sourceId;
    private String sourceName;
    private String sourceCategory;



    public NewsSource(String sourceId, String sourceName, String sourceCategory) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.sourceCategory = sourceCategory;
    }

    public NewsSource() {

    }



    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }
}
