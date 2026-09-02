package com.example.classly.data;

import java.util.UUID;

public class SubjectInfo {
    private String id;
    private String name;
    private int credits;
    private int hours;
    private double averageGrade;

    public SubjectInfo(String name, int credits, int hours, double averageGrade) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.credits = credits;
        this.hours = hours;
        this.averageGrade = averageGrade;
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
}
