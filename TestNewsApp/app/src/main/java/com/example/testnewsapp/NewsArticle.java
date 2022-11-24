package com.example.testnewsapp;

public class NewsArticle {

    private String articleAuthor;
    private String articleTitle;
    private String articleDescription;
    private String articleUrl;
    private String articleImageUrl;
    private String articlePublishedTime;

    public NewsArticle(String articleAuthor, String articleTitle, String articleDescription,
                       String articleUrl, String articleImageUrl, String articlePublishedTime) {
        this.articleAuthor = articleAuthor;
        this.articleTitle = articleTitle;
        this.articleDescription = articleDescription;
        this.articleUrl = articleUrl;
        this.articleImageUrl = articleImageUrl;
        this.articlePublishedTime = articlePublishedTime;
    }

    public NewsArticle() {

    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public void setArticleDescription(String articleDescription) {
        this.articleDescription = articleDescription;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getArticleImageUrl() {
        return articleImageUrl;
    }

    public void setArticleImageUrl(String articleImageUrl) {
        this.articleImageUrl = articleImageUrl;
    }

    public String getArticlePublishedTime() {
        return articlePublishedTime;
    }

    public void setArticlePublishedTime(String articlePublishedTime) {
        this.articlePublishedTime = articlePublishedTime;
    }
}
