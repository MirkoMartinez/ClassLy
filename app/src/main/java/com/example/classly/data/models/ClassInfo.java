package com.example.classly.data.models;

/**
 * Representa la información de una clase o turno dentro del horario académico.
 * Hereda de BaseEntity aplicando los principios de POO (Herencia y Encapsulamiento).
 */
public class ClassInfo extends BaseEntity {
    private String subject;
    private String time;
    private String room;
    private String professor;
    private int dayIndex;

    public ClassInfo(String subject, String time, String room, String professor, int dayIndex) {
        super();
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.professor = professor;
        this.dayIndex = dayIndex;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getProfessor() { return professor; }
    public void setProfessor(String professor) { this.professor = professor; }
    public int getDayIndex() { return dayIndex; }
    public void setDayIndex(int dayIndex) { this.dayIndex = dayIndex; }
}
