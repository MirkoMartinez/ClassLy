package com.example.classly.data.local.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exams")
public class ExamEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String subject;
    private String date;
    private String type;
    private String topic;
    private Double grade;

    public ExamEntity(@NonNull String id, String subject, String date, String type, String topic, Double grade) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.grade = grade;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getSubject() { return subject; }
    public String getDate() { return date; }
    public String getType() { return type; }
    public String getTopic() { return topic; }
    public Double getGrade() { return grade; }
}
