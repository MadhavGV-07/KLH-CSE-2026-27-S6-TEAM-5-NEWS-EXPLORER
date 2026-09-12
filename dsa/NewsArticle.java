package com.newsexplorer.dsa;

public class NewsArticle {
    private String id;
    private String title;
    private String summary;
    private String content;
    private String category;
    private String author;
    private String date;

    public NewsArticle(String id, String title, String summary, String content, String category, String author, String date) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.category = category;
        this.author = author;
        this.date = date;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public String getContent() { return content; }
    public String getCategory() { return category; }
    public String getAuthor() { return author; }
    public String getDate() { return date; }

    public String getFullSearchableText() {
        return title + " " + summary + " " + content;
    }
}
