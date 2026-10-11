package com.example.classly.data.models;

import android.annotation.SuppressLint;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa la información de un examen, parcial o evaluación académica.
 * Hereda de BaseEntity aplicando los principios de POO (Herencia y Encapsulamiento).
 */
public class ExamInfo extends BaseEntity {
    private String subject;
    private String date;
    private String type;
    private String topic;
    private Double grade;

    public ExamInfo(String subject, String date, String type, String topic) {
        super();
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.grade = null;
    }

    public ExamInfo(String subject, String date, String type, String topic, Double grade) {
        super();
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.grade = grade;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    @SuppressLint("NewApi")
    public int getDaysRemaining() {
        try {
            LocalDate examDate = LocalDate.parse(this.date);
            return (int) ChronoUnit.DAYS.between(LocalDate.now(), examDate);
        } catch (Exception e) {
            return 0;
        }
    }
}
