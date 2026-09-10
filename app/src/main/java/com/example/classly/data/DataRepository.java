package com.example.classly.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Esta clase usa el patrón Singleton: solo existe una instancia en toda la app para centralizar los datos
public class DataRepository {
    private static DataRepository instance;
    private final List<ClassInfo> classes;
    private final List<ExamInfo> exams;
    private final List<SubjectInfo> subjects;

    private DataRepository() {
        classes = new ArrayList<>();
        exams = new ArrayList<>();
        subjects = new ArrayList<>();
        // Cargamos unos datos de ejemplo al iniciar para que la app no se vea vacía
        initDummyData();
    }

    // Método para obtener la única instancia del repositorio desde cualquier parte del código
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

        exams.add(new ExamInfo("Física I", "15 Sep", "Parcial", "Termodinámica", 5));
        exams.add(new ExamInfo("Matemáticas II", "22 Sep", "Parcial", "Integrales Multiples", 12));
        exams.add(new ExamInfo("Programación", "30 Sep", "Final", "Estructuras de Datos", 20));

        subjects.add(new SubjectInfo("Matemáticas II", 5, 80, 8.5, true, 8.0));
        subjects.add(new SubjectInfo("Física I", 4, 64, 7.2, false, 0.0));
        subjects.add(new SubjectInfo("Programación", 6, 96, 9.0, true, 7.0));
        subjects.add(new SubjectInfo("Sistemas", 4, 64, 8.0, false, 0.0));
    }

    public List<ClassInfo> getClassesByDay(int dayIndex) {
        return classes.stream()
                .filter(c -> c.getDayIndex() == dayIndex)
                .collect(Collectors.toList());
    }

    public void addClass(ClassInfo classInfo) {
        classes.add(classInfo);
    }

    public void updateClass(ClassInfo updatedClass) {
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).getId().equals(updatedClass.getId())) {
                classes.set(i, updatedClass);
                return;
            }
        }
    }

    public void deleteClass(String id) {
        classes.removeIf(c -> c.getId().equals(id));
    }

    public List<ExamInfo> getExams() {
        return new ArrayList<>(exams);
    }

    public void addExam(ExamInfo examInfo) {
        exams.add(examInfo);
    }

    public void updateExam(ExamInfo updatedExam) {
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getId().equals(updatedExam.getId())) {
                exams.set(i, updatedExam);
                return;
            }
        }
    }

    public void deleteExam(String id) {
        exams.removeIf(e -> e.getId().equals(id));
    }

    public List<SubjectInfo> getSubjects() {
        return new ArrayList<>(subjects);
    }

    public void addSubject(SubjectInfo subjectInfo) {
        subjects.add(subjectInfo);
    }

    public void updateSubject(SubjectInfo updatedSubject) {
        for (int i = 0; i < subjects.size(); i++) {
            if (subjects.get(i).getId().equals(updatedSubject.getId())) {
                subjects.set(i, updatedSubject);
                return;
            }
        }
    }

    public void deleteSubject(String id) {
        subjects.removeIf(s -> s.getId().equals(id));
    }
}
