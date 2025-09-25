package com.example.myapplication;

public class Topic {
    private String title;
    private int questionCount;
    private String time;

    public Topic(String title, int questionCount, String time) {
        this.title = title;
        this.questionCount = questionCount;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public String getTime() {
        return time;
    }
}