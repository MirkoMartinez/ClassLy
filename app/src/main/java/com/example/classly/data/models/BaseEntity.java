package com.example.classly.data.models;

import java.util.UUID;

/**
 * Clase base abstracta que representa una entidad de datos en el sistema.
 * Demuestra los principios de POO: Encapsulamiento y Herencia,
 * proveyendo un ID único y métodos comunes para todas las entidades (Clases, Exámenes, Materias, TPs).
 */
public abstract class BaseEntity {
    protected String id;

    public BaseEntity() {
        this.id = UUID.randomUUID().toString();
    }

    public BaseEntity(String id) {
        this.id = id != null ? id : UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
