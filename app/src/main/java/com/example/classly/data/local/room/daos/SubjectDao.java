package com.example.classly.data.local.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.classly.data.local.room.entities.SubjectEntity;
import java.util.List;

@Dao
public interface SubjectDao {
    @Query("SELECT * FROM subjects")
    List<SubjectEntity> getAllSubjects();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubject(SubjectEntity subject);

    @Update
    void updateSubject(SubjectEntity subject);

    @Query("DELETE FROM subjects WHERE id = :id")
    void deleteSubjectById(String id);
}
