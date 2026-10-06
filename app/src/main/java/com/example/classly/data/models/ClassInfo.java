package com.example.classly.data.models;

import java.util.UUID;

/**
 * Representa la información de una clase o turno dentro del horario académico.
 * Contiene los datos fundamentales como la materia, el horario, el aula,
 * el profesor y el índice del día de la semana correspondiente.
 */
public class ClassInfo {
    private String id;
    private String subject;
    private String time;
    private String room;
    private String professor;
    private int dayIndex; // 0 para Lunes, 1 para Martes, 2 para Miércoles, 3 para Jueves, 4 para Viernes, etc.

    /**
     * Constructor para inicializar una nueva instancia de ClassInfo con un ID único generado automáticamente.
     *
     * @param subject   Nombre de la materia.
     * @param time      Rango horario de la clase (ej. "08:00 - 10:00").
     * @param room      Aula o laboratorio donde se imparte.
     * @param professor Nombre del profesor o docente a cargo.
     * @param dayIndex  Índice numérico del día de la semana.
     */
    public ClassInfo(String subject, String time, String room, String professor, int dayIndex) {
        this.id = UUID.randomUUID().toString();
        this.subject = subject;
        this.time = time;
        this.room = room;
        this.professor = professor;
        this.dayIndex = dayIndex;
    }

    // --- Getters y Setters con su respectiva documentación ---

    /** Obtiene el identificador único de la clase. */
    public String getId() { return id; }

    /** Obtiene el nombre de la materia. */
    public String getSubject() { return subject; }

    /** Establece el nombre de la materia. */
    public void setSubject(String subject) { this.subject = subject; }

    /** Obtiene el horario de la clase. */
    public String getTime() { return time; }

    /** Establece el horario de la clase. */
    public void setTime(String time) { this.time = time; }

    /** Obtiene el aula o ubicación. */
    public String getRoom() { return room; }

    /** Establece el aula o ubicación. */
    public void setRoom(String room) { this.room = room; }

    /** Obtiene el nombre del profesor. */
    public String getProfessor() { return professor; }

    /** Establece el nombre del profesor. */
    public void setProfessor(String professor) { this.professor = professor; }

    /** Obtiene el índice del día de la semana. */
    public int getDayIndex() { return dayIndex; }

    /** Establece el índice del día de la semana. */
    public void setDayIndex(int dayIndex) { this.dayIndex = dayIndex; }
}
