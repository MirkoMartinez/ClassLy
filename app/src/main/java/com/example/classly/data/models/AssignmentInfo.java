package com.example.classly.data.models;

import android.annotation.SuppressLint;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representa un trabajo práctico o tarea pendiente de entrega para una materia.
 * Hereda de BaseEntity aplicando los principios de POO (Herencia y Encapsulamiento).
 */
public class AssignmentInfo extends BaseEntity {
    private String subject;
    private String date;
    private String title;
    private String description;
    private boolean isSubmitted;
    private Double grade;

    public AssignmentInfo(String subject, String date, String title, String description) {
        super();
        this.subject = subject;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isSubmitted = false;
        this.grade = null;
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isSubmitted() { return isSubmitted; }
    public void setSubmitted(boolean submitted) { isSubmitted = submitted; }
    public Double getGrade() { return grade; }
    public void setGrade(Double grade) { this.grade = grade; }

    @SuppressLint("NewApi")
    public int getDaysRemaining() {
        try {
            LocalDate dueDate = LocalDate.parse(this.date);
            return (int) ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
        } catch (Exception e) {
            return 0;
        }
    }
}
