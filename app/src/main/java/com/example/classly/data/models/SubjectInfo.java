package com.example.classly.data.models;

import java.util.UUID;

/**
 * Representa la información detallada de una asignatura o materia cursada,
 * incluyendo su carga horaria, promedio de calificaciones y estado/criterio de promoción.
 */
public class SubjectInfo {
    private String id;
    private String name;
    private int hours;
    private double averageGrade;
    private boolean isPromotional;
    private double promotionGrade;
    private int partialsCount;

    /**
     * Constructor para inicializar una nueva asignatura.
     *
     * @param name           Nombre de la materia.
     * @param hours          Carga horaria total de la materia.
     * @param averageGrade   Calificación promedio actual.
     * @param isPromotional  Indica si la materia es promocionable (sin examen final).
     * @param promotionGrade Nota mínima requerida para lograr la promoción.
     */
    public SubjectInfo(String name, int hours, double averageGrade, boolean isPromotional, double promotionGrade) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
        this.partialsCount = 2; // Valor por defecto
    }

    /**
     * Constructor completo incluyendo la cantidad de parciales.
     */
    public SubjectInfo(String name, int hours, double averageGrade, boolean isPromotional, double promotionGrade, int partialsCount) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
        this.partialsCount = partialsCount;
    }

    // --- Getters y Setters ---

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }
    public double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }
    public boolean isPromotional() { return isPromotional; }
    public void setPromotional(boolean promotional) { isPromotional = promotional; }
    public double getPromotionGrade() { return promotionGrade; }
    public void setPromotionGrade(double promotionGrade) { this.promotionGrade = promotionGrade; }
    public int getPartialsCount() { return partialsCount; }
    public void setPartialsCount(int partialsCount) { this.partialsCount = partialsCount; }
}
