package assignment5.ruparajendran.newsgateway;

import java.io.Serializable;

public class Article implements Serializable {

    private String author;
    private String title;
    private String description;
    private String url;
    private String urltoImage;
    private String publishedAt;

    public Article(String author, String title, String description, String url, String urltoImage, String publishedAt) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urltoImage = urltoImage;
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = removeRegex(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = removeRegex(description);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrltoImage() {
        return urltoImage;
    }

    public void setUrltoImage(String urltoImage) {
        this.urltoImage = urltoImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    private String removeRegex(String s)
    {
        return s.replaceAll("\\<(/?[^\\>]+)\\>", "\\ ").replaceAll("\\s+", " ").trim();
    }
}
