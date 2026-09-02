package com.example.classly.data;

import java.util.UUID;

public class ExamInfo {
    private String id;
    private String subject;
    private String date;
    private String type;
    private String topic;
    private int daysRemaining;

    public ExamInfo(String subject, String date, String type, String topic, int daysRemaining) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.daysRemaining = daysRemaining;
    }

    public String getId() { return id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public int getDaysRemaining() { return daysRemaining; }
    public void setDaysRemaining(int daysRemaining) { this.daysRemaining = daysRemaining; }
}
