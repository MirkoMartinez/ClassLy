package com.example.classly.data.models;

import android.annotation.SuppressLint;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Representa un trabajo práctico o tarea pendiente de entrega para una materia,
 * incluyendo su fecha de vencimiento, título, descripción, estado de entrega y nota.
 */
public class AssignmentInfo {
    private String id;
    private String subject;
    private String date; // Formato YYYY-MM-DD
    private String title;
    private String description;
    private boolean isSubmitted;
    private Double grade;

    /**
     * Constructor para inicializar un nuevo trabajo práctico.
     *
     * @param subject     Materia asociada.
     * @param date        Fecha de vencimiento (YYYY-MM-DD).
     * @param title       Título del trabajo práctico.
     * @param description Descripción o consignas.
     */
    public AssignmentInfo(String subject, String date, String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.date = date;
        this.title = title;
        this.description = description;
        this.isSubmitted = false;
        this.grade = null;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
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

    /**
     * Calcula los días restantes hasta la fecha de vencimiento.
     */
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
