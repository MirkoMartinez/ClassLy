package com.example.classly.data.repository;

import android.util.Log;
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
 * incorporando manejo robusto de excepciones (try/catch) y auditoría con Logcat.
 */
public class DataRepository {
    private static final String TAG = "ClassLyDebug";
    private static DataRepository instance;
    private final List<ClassInfo> classes;
    private final List<ExamInfo> exams;
    private final List<SubjectInfo> subjects;
    private final List<AssignmentInfo> assignments;

    private DataRepository() {
        classes = new ArrayList<>();
        exams = new ArrayList<>();
        subjects = new ArrayList<>();
        assignments = new ArrayList<>();
        try {
            initDummyData();
            Log.i(TAG, "DataRepository: Datos iniciales cargados correctamente.");
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al inicializar datos dummy", e);
        }
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

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

    public List<ClassInfo> getClassesByDay(int dayIndex) {
        try {
            return classes.stream()
                    .filter(c -> c.getDayIndex() == dayIndex)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al filtrar clases por día", e);
            return new ArrayList<>();
        }
    }

    public void addClass(ClassInfo classInfo) {
        try {
            classes.add(classInfo);
            Log.d(TAG, "DataRepository: Clase añadida -> " + classInfo.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir clase", e);
        }
    }

    public void updateClass(ClassInfo updatedClass) {
        try {
            for (int i = 0; i < classes.size(); i++) {
                if (classes.get(i).getId().equals(updatedClass.getId())) {
                    classes.set(i, updatedClass);
                    Log.d(TAG, "DataRepository: Clase actualizada -> " + updatedClass.getSubject());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar clase", e);
        }
    }

    public void deleteClass(String id) {
        try {
            classes.removeIf(c -> c.getId().equals(id));
            Log.d(TAG, "DataRepository: Clase eliminada ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar clase", e);
        }
    }

    public List<ExamInfo> getExams() {
        try {
            return new ArrayList<>(exams);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener exámenes", e);
            return new ArrayList<>();
        }
    }

    public void addExam(ExamInfo examInfo) {
        try {
            exams.add(examInfo);
            Log.d(TAG, "DataRepository: Examen añadido -> " + examInfo.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir examen", e);
        }
    }

    public void updateExam(ExamInfo updatedExam) {
        try {
            for (int i = 0; i < exams.size(); i++) {
                if (exams.get(i).getId().equals(updatedExam.getId())) {
                    exams.set(i, updatedExam);
                    Log.d(TAG, "DataRepository: Examen actualizado -> " + updatedExam.getSubject());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar examen", e);
        }
    }

    public void deleteExam(String id) {
        try {
            exams.removeIf(e -> e.getId().equals(id));
            Log.d(TAG, "DataRepository: Examen eliminado ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar examen", e);
        }
    }

    public List<SubjectInfo> getSubjects() {
        try {
            return new ArrayList<>(subjects);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener asignaturas", e);
            return new ArrayList<>();
        }
    }

    public void addSubject(SubjectInfo subjectInfo) {
        try {
            subjects.add(subjectInfo);
            Log.d(TAG, "DataRepository: Asignatura añadida -> " + subjectInfo.getName());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir asignatura", e);
        }
    }

    public void updateSubject(SubjectInfo updatedSubject) {
        try {
            for (int i = 0; i < subjects.size(); i++) {
                if (subjects.get(i).getId().equals(updatedSubject.getId())) {
                    subjects.set(i, updatedSubject);
                    Log.d(TAG, "DataRepository: Asignatura actualizada -> " + updatedSubject.getName());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar asignatura", e);
        }
    }

    public void deleteSubject(String id) {
        try {
            subjects.removeIf(s -> s.getId().equals(id));
            Log.d(TAG, "DataRepository: Asignatura eliminada ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar asignatura", e);
        }
    }

    public List<AssignmentInfo> getAssignments() {
        try {
            return new ArrayList<>(assignments);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener TPs", e);
            return new ArrayList<>();
        }
    }

    public void addAssignment(AssignmentInfo assignmentInfo) {
        try {
            assignments.add(assignmentInfo);
            Log.d(TAG, "DataRepository: TP añadido -> " + assignmentInfo.getTitle());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir TP", e);
        }
    }

    public void updateAssignment(AssignmentInfo updatedAssignment) {
        try {
            for (int i = 0; i < assignments.size(); i++) {
                if (assignments.get(i).getId().equals(updatedAssignment.getId())) {
                    assignments.set(i, updatedAssignment);
                    Log.d(TAG, "DataRepository: TP actualizado -> " + updatedAssignment.getTitle());
                    return;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar TP", e);
        }
    }

    public void deleteAssignment(String id) {
        try {
            assignments.removeIf(a -> a.getId().equals(id));
            Log.d(TAG, "DataRepository: TP eliminado ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar TP", e);
        }
    }
}
