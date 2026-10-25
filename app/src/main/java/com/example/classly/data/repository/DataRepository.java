package com.example.classly.data.repository;

import android.content.Context;
import android.util.Log;
import com.example.classly.data.local.room.AppDatabase;
import com.example.classly.data.local.room.entities.AssignmentEntity;
import com.example.classly.data.local.room.entities.ClassEntity;
import com.example.classly.data.local.room.entities.ExamEntity;
import com.example.classly.data.local.room.entities.SubjectEntity;
import com.example.classly.data.models.AssignmentInfo;
import com.example.classly.data.models.ClassInfo;
import com.example.classly.data.models.ExamInfo;
import com.example.classly.data.models.SubjectInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositorio central de datos que implementa el patrón Singleton.
 * Integra la base de datos local con Room (ORM & SQLite) junto con un respaldo en memoria,
 * incorporando manejo robusto de excepciones (try/catch) y auditoría con Logcat.
 */
public class DataRepository {
    private static final String TAG = "ClassLyDebug";
    private static DataRepository instance;
    private AppDatabase database;
    private boolean useRoom = false;

    private final List<ClassInfo> memoryClasses = new ArrayList<>();
    private final List<ExamInfo> memoryExams = new ArrayList<>();
    private final List<SubjectInfo> memorySubjects = new ArrayList<>();
    private final List<AssignmentInfo> memoryAssignments = new ArrayList<>();

    private DataRepository() {
        initDummyData();
    }

    public static synchronized DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
        }
        return instance;
    }

    public static void init(Context context) {
        getInstance();
        if (!instance.useRoom && context != null) {
            try {
                instance.database = AppDatabase.getInstance(context);
                instance.useRoom = true;
                instance.seedInitialDataIfNeeded();
                Log.i(TAG, "DataRepository: Base de datos Room (SQLite ORM) inicializada correctamente.");
            } catch (Exception e) {
                Log.e(TAG, "DataRepository: Error al inicializar Room Database", e);
            }
        }
    }

    private void initDummyData() {
        memoryClasses.add(new ClassInfo("Matemáticas II", "08:00 - 10:00", "Aula 302", "Dr. García", 0));
        memoryClasses.add(new ClassInfo("Programación", "10:00 - 12:00", "Lab 1", "Ing. Pérez", 0));
        memoryClasses.add(new ClassInfo("Física I", "09:00 - 11:00", "Aula 201", "Lic. Blanco", 1));
        memoryClasses.add(new ClassInfo("Matemáticas II", "08:00 - 10:00", "Aula 302", "Dr. García", 2));
        memoryClasses.add(new ClassInfo("Sistemas", "14:00 - 16:00", "Aula 105", "Dra. Torres", 2));
        memoryClasses.add(new ClassInfo("Programación", "10:00 - 12:00", "Lab 1", "Ing. Pérez", 3));
        memoryClasses.add(new ClassInfo("Física I", "09:00 - 11:00", "Aula 201", "Lic. Blanco", 4));
        memoryClasses.add(new ClassInfo("Sistemas", "11:00 - 13:00", "Aula 105", "Dra. Torres", 4));

        memoryExams.add(new ExamInfo("Física I", "2026-09-25", "Parcial", "Termodinámica"));
        memoryExams.add(new ExamInfo("Matemáticas II", "2026-10-02", "Parcial", "Integrales Multiples"));
        memoryExams.add(new ExamInfo("Programación", "2026-10-10", "Final", "Estructuras de Datos"));

        memorySubjects.add(new SubjectInfo("Matemáticas II", 80, 8.5, true, 8.0));
        memorySubjects.add(new SubjectInfo("Física I", 64, 7.2, false, 0.0));
        memorySubjects.add(new SubjectInfo("Programación", 96, 9.0, true, 7.0));
        memorySubjects.add(new SubjectInfo("Sistemas", 64, 8.0, false, 0.0));

        memoryAssignments.add(new AssignmentInfo("Programación", "2026-09-28", "TP 1: Estructuras", "Implementar árboles binarios"));
        memoryAssignments.add(new AssignmentInfo("Física I", "2026-10-05", "Informe de Laboratorio", "Práctica de péndulo"));
    }

    private void seedInitialDataIfNeeded() {
        if (!useRoom || database == null) return;
        new Thread(() -> {
            try {
                if (database.subjectDao().getAllSubjects().isEmpty()) {
                    for (SubjectInfo s : memorySubjects) {
                        database.subjectDao().insertSubject(new SubjectEntity(s.getId(), s.getName(), s.getHours(), s.getAverageGrade(), s.isPromotional(), s.getPromotionGrade(), s.getPartialsCount()));
                    }
                    for (ExamInfo e : memoryExams) {
                        database.examDao().insertExam(new ExamEntity(e.getId(), e.getSubject(), e.getDate(), e.getType(), e.getTopic(), e.getGrade()));
                    }
                    for (AssignmentInfo a : memoryAssignments) {
                        database.assignmentDao().insertAssignment(new AssignmentEntity(a.getId(), a.getSubject(), a.getDate(), a.getTitle(), a.getDescription(), a.isSubmitted(), a.getGrade()));
                    }
                    for (ClassInfo c : memoryClasses) {
                        database.classDao().insertClass(new ClassEntity(c.getId(), c.getSubject(), c.getTime(), c.getRoom(), c.getProfessor(), c.getDayIndex()));
                    }
                    Log.i(TAG, "DataRepository: Datos iniciales sembrados en SQLite con Room.");
                }
            } catch (Exception e) {
                Log.e(TAG, "DataRepository: Error al sembrar datos iniciales en Room", e);
            }
        }).start();
    }

    public List<ClassInfo> getClassesByDay(int dayIndex) {
        try {
            if (useRoom && database != null) {
                List<ClassEntity> entities = database.classDao().getClassesByDay(dayIndex);
                List<ClassInfo> list = new ArrayList<>();
                for (ClassEntity ce : entities) {
                    list.add(new ClassInfo(ce.getSubject(), ce.getTime(), ce.getRoom(), ce.getProfessor(), ce.getDayIndex()));
                }
                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener clases de Room por día", e);
        }
        return memoryClasses.stream().filter(c -> c.getDayIndex() == dayIndex).collect(Collectors.toList());
    }

    public void addClass(ClassInfo classInfo) {
        try {
            memoryClasses.add(classInfo);
            if (useRoom && database != null) {
                database.classDao().insertClass(new ClassEntity(classInfo.getId(), classInfo.getSubject(), classInfo.getTime(), classInfo.getRoom(), classInfo.getProfessor(), classInfo.getDayIndex()));
            }
            Log.d(TAG, "DataRepository: Clase añadida -> " + classInfo.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir clase", e);
        }
    }

    public void updateClass(ClassInfo updatedClass) {
        try {
            for (int i = 0; i < memoryClasses.size(); i++) {
                if (memoryClasses.get(i).getId().equals(updatedClass.getId())) {
                    memoryClasses.set(i, updatedClass);
                    break;
                }
            }
            if (useRoom && database != null) {
                database.classDao().updateClass(new ClassEntity(updatedClass.getId(), updatedClass.getSubject(), updatedClass.getTime(), updatedClass.getRoom(), updatedClass.getProfessor(), updatedClass.getDayIndex()));
            }
            Log.d(TAG, "DataRepository: Clase actualizada -> " + updatedClass.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar clase", e);
        }
    }

    public void deleteClass(String id) {
        try {
            memoryClasses.removeIf(c -> c.getId().equals(id));
            if (useRoom && database != null) {
                database.classDao().deleteClassById(id);
            }
            Log.d(TAG, "DataRepository: Clase eliminada ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar clase", e);
        }
    }

    public List<ExamInfo> getExams() {
        try {
            if (useRoom && database != null) {
                List<ExamEntity> entities = database.examDao().getAllExams();
                List<ExamInfo> list = new ArrayList<>();
                for (ExamEntity ee : entities) {
                    list.add(new ExamInfo(ee.getSubject(), ee.getDate(), ee.getType(), ee.getTopic(), ee.getGrade()));
                }
                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener exámenes de Room", e);
        }
        return new ArrayList<>(memoryExams);
    }

    public void addExam(ExamInfo examInfo) {
        try {
            memoryExams.add(examInfo);
            if (useRoom && database != null) {
                database.examDao().insertExam(new ExamEntity(examInfo.getId(), examInfo.getSubject(), examInfo.getDate(), examInfo.getType(), examInfo.getTopic(), examInfo.getGrade()));
            }
            Log.d(TAG, "DataRepository: Examen añadido -> " + examInfo.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir examen", e);
        }
    }

    public void updateExam(ExamInfo updatedExam) {
        try {
            for (int i = 0; i < memoryExams.size(); i++) {
                if (memoryExams.get(i).getId().equals(updatedExam.getId())) {
                    memoryExams.set(i, updatedExam);
                    break;
                }
            }
            if (useRoom && database != null) {
                database.examDao().updateExam(new ExamEntity(updatedExam.getId(), updatedExam.getSubject(), updatedExam.getDate(), updatedExam.getType(), updatedExam.getTopic(), updatedExam.getGrade()));
            }
            Log.d(TAG, "DataRepository: Examen actualizado -> " + updatedExam.getSubject());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar examen", e);
        }
    }

    public void deleteExam(String id) {
        try {
            memoryExams.removeIf(e -> e.getId().equals(id));
            if (useRoom && database != null) {
                database.examDao().deleteExamById(id);
            }
            Log.d(TAG, "DataRepository: Examen eliminado ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar examen", e);
        }
    }

    public List<SubjectInfo> getSubjects() {
        try {
            if (useRoom && database != null) {
                List<SubjectEntity> entities = database.subjectDao().getAllSubjects();
                List<SubjectInfo> list = new ArrayList<>();
                for (SubjectEntity se : entities) {
                    list.add(new SubjectInfo(se.getName(), se.getHours(), se.getAverageGrade(), se.isPromotional(), se.getPromotionGrade(), se.getPartialsCount()));
                }
                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener asignaturas de Room", e);
        }
        return new ArrayList<>(memorySubjects);
    }

    public void addSubject(SubjectInfo subjectInfo) {
        try {
            memorySubjects.add(subjectInfo);
            if (useRoom && database != null) {
                database.subjectDao().insertSubject(new SubjectEntity(subjectInfo.getId(), subjectInfo.getName(), subjectInfo.getHours(), subjectInfo.getAverageGrade(), subjectInfo.isPromotional(), subjectInfo.getPromotionGrade(), subjectInfo.getPartialsCount()));
            }
            Log.d(TAG, "DataRepository: Asignatura añadida -> " + subjectInfo.getName());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir asignatura", e);
        }
    }

    public void updateSubject(SubjectInfo updatedSubject) {
        try {
            for (int i = 0; i < memorySubjects.size(); i++) {
                if (memorySubjects.get(i).getId().equals(updatedSubject.getId())) {
                    memorySubjects.set(i, updatedSubject);
                    break;
                }
            }
            if (useRoom && database != null) {
                database.subjectDao().updateSubject(new SubjectEntity(updatedSubject.getId(), updatedSubject.getName(), updatedSubject.getHours(), updatedSubject.getAverageGrade(), updatedSubject.isPromotional(), updatedSubject.getPromotionGrade(), updatedSubject.getPartialsCount()));
            }
            Log.d(TAG, "DataRepository: Asignatura actualizada -> " + updatedSubject.getName());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar asignatura", e);
        }
    }

    public void deleteSubject(String id) {
        try {
            memorySubjects.removeIf(s -> s.getId().equals(id));
            if (useRoom && database != null) {
                database.subjectDao().deleteSubjectById(id);
            }
            Log.d(TAG, "DataRepository: Asignatura eliminada ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar asignatura", e);
        }
    }

    public List<AssignmentInfo> getAssignments() {
        try {
            if (useRoom && database != null) {
                List<AssignmentEntity> entities = database.assignmentDao().getAllAssignments();
                List<AssignmentInfo> list = new ArrayList<>();
                for (AssignmentEntity ae : entities) {
                    list.add(new AssignmentInfo(ae.getSubject(), ae.getDate(), ae.getTitle(), ae.getDescription()));
                }
                return list;
            }
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al obtener TPs de Room", e);
        }
        return new ArrayList<>(memoryAssignments);
    }

    public void addAssignment(AssignmentInfo assignmentInfo) {
        try {
            memoryAssignments.add(assignmentInfo);
            if (useRoom && database != null) {
                database.assignmentDao().insertAssignment(new AssignmentEntity(assignmentInfo.getId(), assignmentInfo.getSubject(), assignmentInfo.getDate(), assignmentInfo.getTitle(), assignmentInfo.getDescription(), assignmentInfo.isSubmitted(), assignmentInfo.getGrade()));
            }
            Log.d(TAG, "DataRepository: TP añadido -> " + assignmentInfo.getTitle());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al añadir TP", e);
        }
    }

    public void updateAssignment(AssignmentInfo updatedAssignment) {
        try {
            for (int i = 0; i < memoryAssignments.size(); i++) {
                if (memoryAssignments.get(i).getId().equals(updatedAssignment.getId())) {
                    memoryAssignments.set(i, updatedAssignment);
                    break;
                }
            }
            if (useRoom && database != null) {
                database.assignmentDao().updateAssignment(new AssignmentEntity(updatedAssignment.getId(), updatedAssignment.getSubject(), updatedAssignment.getDate(), updatedAssignment.getTitle(), updatedAssignment.getDescription(), updatedAssignment.isSubmitted(), updatedAssignment.getGrade()));
            }
            Log.d(TAG, "DataRepository: TP actualizado -> " + updatedAssignment.getTitle());
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al actualizar TP", e);
        }
    }

    public void deleteAssignment(String id) {
        try {
            memoryAssignments.removeIf(a -> a.getId().equals(id));
            if (useRoom && database != null) {
                database.assignmentDao().deleteAssignmentById(id);
            }
            Log.d(TAG, "DataRepository: TP eliminado ID -> " + id);
        } catch (Exception e) {
            Log.e(TAG, "DataRepository: Error al eliminar TP", e);
        }
    }
}
