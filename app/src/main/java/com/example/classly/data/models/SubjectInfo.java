package com.example.classly.data.models;

/**
 * Representa la información detallada de una asignatura o materia cursada.
 * Hereda de BaseEntity aplicando los principios de POO (Herencia y Encapsulamiento).
 */
public class SubjectInfo extends BaseEntity {
    private String name;
    private int hours;
    private double averageGrade;
    private boolean isPromotional;
    private double promotionGrade;
    private int partialsCount;

    public SubjectInfo(String name, int hours, double averageGrade, boolean isPromotional, double promotionGrade) {
        super();
        this.name = name;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
        this.partialsCount = 2;
    }

    public SubjectInfo(String name, int hours, double averageGrade, boolean isPromotional, double promotionGrade, int partialsCount) {
        super();
        this.name = name;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
        this.partialsCount = partialsCount;
    }

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
