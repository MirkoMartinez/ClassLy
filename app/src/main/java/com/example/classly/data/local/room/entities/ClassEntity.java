package com.example.classly.data.local.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classes")
public class ClassEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String subject;
    private String time;
    private String room;
    private String professor;
    private int dayIndex;

    public ClassEntity(@NonNull String id, String subject, String time, String room, String professor, int dayIndex) {
        this.id = id;
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.professor = professor;
        this.dayIndex = dayIndex;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getSubject() { return subject; }
    public String getTime() { return time; }
    public String getRoom() { return room; }
    public String getProfessor() { return professor; }
    public int getDayIndex() { return dayIndex; }
}
