package com.example.classly.data.local.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "assignments")
public class AssignmentEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String subject;
    private String date;
    private String title;
    private String description;
    private boolean isSubmitted;
    private Double grade;

    public AssignmentEntity(@NonNull String id, String subject, String date, String title, String description, boolean isSubmitted, Double grade) {
        this.id = id;
        this.subject = subject;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isSubmitted = isSubmitted;
        this.grade = grade;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getSubject() { return subject; }
    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isSubmitted() { return isSubmitted; }
    public Double getGrade() { return grade; }
}
