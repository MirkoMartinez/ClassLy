package com.example.classly.data;

import java.util.UUID;

public class SubjectInfo {
    private String id;
    private String name;
    private int credits;
    private int hours;
    private double averageGrade;
    private boolean isPromotional;
    private double promotionGrade;

    public SubjectInfo(String name, int credits, int hours, double averageGrade, boolean isPromotional, double promotionGrade) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.credits = credits;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public int getHours() { return hours; }
    public void setHours(int hours) { this.hours = hours; }
    public double getAverageGrade() { return averageGrade; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }
    public boolean isPromotional() { return isPromotional; }
    public void setPromotional(boolean promotional) { isPromotional = promotional; }
    public double getPromotionGrade() { return promotionGrade; }
    public void setPromotionGrade(double promotionGrade) { this.promotionGrade = promotionGrade; }
}
