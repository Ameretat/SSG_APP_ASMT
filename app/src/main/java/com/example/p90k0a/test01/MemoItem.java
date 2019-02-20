package com.example.p90k0a.test01;

public class MemoItem {
    long ID;
    String title;
    String text;
    String date;

    public MemoItem(long ID, String title, String text, String date) {
        this.ID = ID;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public String getTitle(String title) {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText(String text) {
        return text;
    }

    public void setContent(String text) {
        this.text = text;
    }

    public String getDate(String date) {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}