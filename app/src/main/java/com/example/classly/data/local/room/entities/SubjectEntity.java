package com.example.classly.data.local.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subjects")
public class SubjectEntity {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private int hours;
    private double averageGrade;
    private boolean isPromotional;
    private double promotionGrade;
    private int partialsCount;

    public SubjectEntity(@NonNull String id, String name, int hours, double averageGrade, boolean isPromotional, double promotionGrade, int partialsCount) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.averageGrade = averageGrade;
        this.isPromotional = isPromotional;
        this.promotionGrade = promotionGrade;
        this.partialsCount = partialsCount;
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getName() { return name; }
    public int getHours() { return hours; }
    public double getAverageGrade() { return averageGrade; }
    public boolean isPromotional() { return isPromotional; }
    public double getPromotionGrade() { return promotionGrade; }
    public int getPartialsCount() { return partialsCount; }
}
