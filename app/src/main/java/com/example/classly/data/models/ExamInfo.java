package com.example.classly.data.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Representa la información de un examen, parcial o evaluación académica.
 * Incluye la materia, la fecha programada, el tipo de evaluación y el tema o unidad.
 */
public class ExamInfo {
    private String id;
    private String subject;
    private String date; // Formato esperado: YYYY-MM-DD
    private String type; // Ej: Parcial, Final, Práctica, etc.
    private String topic;
    private Double grade; // Nota obtenida, null si aún no está calificado

    /**
     * Constructor para inicializar una nueva evaluación con un ID único.
     *
     * @param subject Materia asociada al examen.
     * @param date    Fecha del examen en formato YYYY-MM-DD.
     * @param type    Tipo de evaluación (Parcial, Final, etc.).
     * @param topic   Temas específicos a evaluar.
     */
    public ExamInfo(String subject, String date, String type, String topic) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.grade = null;
    }

    /**
     * Constructor completo incluyendo la calificación obtenida.
     */
    public ExamInfo(String subject, String date, String type, String topic, Double grade) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.date = date;
        this.type = type;
        this.topic = topic;
        this.grade = grade;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
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

    /**
     * Calcula los días restantes desde la fecha actual hasta la fecha del examen.
     *
     * @return Número de días restantes (0 o positivo si es en el futuro).
     */
    public int getDaysRemaining() {
        try {
            LocalDate examDate = LocalDate.parse(this.date);
            return (int) ChronoUnit.DAYS.between(LocalDate.now(), examDate);
        } catch (Exception e) {
            return 0;
        }
    }
}
