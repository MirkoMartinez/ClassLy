package com.example.classly.data.local.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.classly.data.local.room.entities.ExamEntity;
import java.util.List;

@Dao
public interface ExamDao {
    @Query("SELECT * FROM exams")
    List<ExamEntity> getAllExams();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExam(ExamEntity exam);

    @Update
    void updateExam(ExamEntity exam);

    @Query("DELETE FROM exams WHERE id = :id")
    void deleteExamById(String id);
}
