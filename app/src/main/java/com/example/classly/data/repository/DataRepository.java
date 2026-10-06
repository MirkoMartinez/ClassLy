package com.example.classly.data.repository;

import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.models.SubjectInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositorio central de datos que implementa el patrón Singleton.
 * Gestiona en memoria las listas de clases del horario, exámenes y asignaturas,
 * proporcionando métodos CRUD para interactuar con los fragmentos y adaptadores.
 */
public class DataRepository {
    private static DataRepository instance;
    private final List<ClassInfo> classes;
    private final List<ExamInfo> exams;
    private final List<SubjectInfo> subjects;
    private final List<AssignmentInfo> assignments;

    /**
     * Constructor privado del repositorio. Inicializa las listas y carga datos de prueba iniciales.
     */
    private DataRepository() {
        classes = new ArrayList<>();
        exams = new ArrayList<>();
        subjects = new ArrayList<>();
        assignments = new ArrayList<>();
        // Cargamos datos de ejemplo al iniciar para asegurar que la app tenga contenido demostrativo funcional
        initDummyData();
    }

    /**
     * Obtiene la única instancia compartida del repositorio (Singleton thread-safe).
     *
     * @return Instancia de DataRepository.
     */
    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    /**
     * Inicializa datos ficticios (dummy data) para clases, exámenes y materias.
     */
    private void initDummyData() {
        classes.add(new ClassInfo("Matemáticas II", "08:00 - 10:00", "Aula 302", "Dr. García", 0));
        classes.add(new ClassInfo("Programación", "10:00 - 12:00", "Lab 1", "Ing. Pérez", 0));
        classes.add(new ClassInfo("Física I", "09:00 - 11:00", "Aula 201", "Lic. Blanco", 1));
        classes.add(new ClassInfo("Matemáticas II", "08:00 - 10:00", "Aula 302", "Dr. García", 2));
        classes.add(new ClassInfo("Sistemas", "14:00 - 16:00", "Aula 105", "Dra. Torres", 2));
        classes.add(new ClassInfo("Programación", "10:00 - 12:00", "Lab 1", "Ing. Pérez", 3));
        classes.add(new ClassInfo("Física I", "09:00 - 11:00", "Aula 201", "Lic. Blanco", 4));
        classes.add(new ClassInfo("Sistemas", "11:00 - 13:00", "Aula 105", "Dra. Torres", 4));

        exams.add(new ExamInfo("Física I", "2026-09-25", "Parcial", "Termodinámica"));
        exams.add(new ExamInfo("Matemáticas II", "2026-10-02", "Parcial", "Integrales Multiples"));
        exams.add(new ExamInfo("Programación", "2026-10-10", "Final", "Estructuras de Datos"));

        subjects.add(new SubjectInfo("Matemáticas II", 80, 8.5, true, 8.0));
        subjects.add(new SubjectInfo("Física I", 64, 7.2, false, 0.0));
        subjects.add(new SubjectInfo("Programación", 96, 9.0, true, 7.0));
        subjects.add(new SubjectInfo("Sistemas", 64, 8.0, false, 0.0));

        assignments.add(new AssignmentInfo("Programación", "2026-09-28", "TP 1: Estructuras", "Implementar árboles binarios"));
        assignments.add(new AssignmentInfo("Física I", "2026-10-05", "Informe de Laboratorio", "Práctica de péndulo"));
    }

    /**
     * Filtra y retorna la lista de clases correspondientes a un día específico de la semana.
     *
     * @param dayIndex Índice del día (0 = Lunes, 4 = Viernes, etc.).
     * @return Lista de objetos ClassInfo para dicho día.
     */
    public List<ClassInfo> getClassesByDay(int dayIndex) {
        return classes.stream()
                .filter(c -> c.getDayIndex() == dayIndex)
                .collect(Collectors.toList());
    }

    /** Agrega una nueva clase al repositorio. */
    public void addClass(ClassInfo classInfo) {
        classes.add(classInfo);
    }

    /** Actualiza una clase existente buscando por su ID único. */
    public void updateClass(ClassInfo updatedClass) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getId().equals(updatedClass.getId())) {
                classes.set(i, updatedClass);
                return;
            }
        }
    }

    /** Elimina una clase por su ID. */
    public void deleteClass(String id) {
        classes.removeIf(c -> c.getId().equals(id));
    }

    /** Retorna una copia de la lista completa de exámenes. */
    public List<ExamInfo> getExams() {
        return new ArrayList<>(exams);
    }

    /** Agrega un nuevo examen. */
    public void addExam(ExamInfo examInfo) {
        exams.add(examInfo);
    }

    /** Actualiza un examen existente por ID. */
    public void updateExam(ExamInfo updatedExam) {
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getId().equals(updatedExam.getId())) {
                exams.set(i, updatedExam);
                return;
            }
        }
    }

    /** Elimina un examen por su ID. */
    public void deleteExam(String id) {
        exams.removeIf(e -> e.getId().equals(id));
    }

    /** Retorna una copia de la lista de asignaturas. */
    public List<SubjectInfo> getSubjects() {
        return new ArrayList<>(subjects);
    }

    /** Agrega una nueva asignatura. */
    public void addSubject(SubjectInfo subjectInfo) {
        subjects.add(subjectInfo);
    }

    /** Actualiza una asignatura existente por ID. */
    public void updateSubject(SubjectInfo updatedSubject) {
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getId().equals(updatedSubject.getId())) {
                subjects.set(i, updatedSubject);
                return;
            }
        }
    }

    /** Elimina una asignatura por su ID. */
    public void deleteSubject(String id) {
        subjects.removeIf(s -> s.getId().equals(id));
    }

    /** Retorna una copia de la lista de trabajos prácticos. */
    public List<AssignmentInfo> getAssignments() {
        return new ArrayList<>(assignments);
    }

    /** Agrega un nuevo trabajo práctico. */
    public void addAssignment(AssignmentInfo assignmentInfo) {
        assignments.add(assignmentInfo);
    }

    /** Actualiza un trabajo práctico existente. */
    public void updateAssignment(AssignmentInfo updatedAssignment) {
        for (int i = 0; i < assignments.size(); i++) {
            if (assignments.get(i).getId().equals(updatedAssignment.getId())) {
                assignments.set(i, updatedAssignment);
                return;
            }
        }
    }

    /** Elimina un trabajo práctico por ID. */
    public void deleteAssignment(String id) {
        assignments.removeIf(a -> a.getId().equals(id));
    }
}
