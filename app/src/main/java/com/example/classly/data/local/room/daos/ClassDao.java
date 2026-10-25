package com.example.classly.data.local.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.classly.data.local.room.entities.ClassEntity;
import java.util.List;

@Dao
public interface ClassDao {
    @Query("SELECT * FROM classes")
    List<ClassEntity> getAllClasses();

    @Query("SELECT * FROM classes WHERE dayIndex = :dayIndex")
    List<ClassEntity> getClassesByDay(int dayIndex);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClass(ClassEntity classEntity);

    @Update
    void updateClass(ClassEntity classEntity);

    @Query("DELETE FROM classes WHERE id = :id")
    void deleteClassById(String id);
}
