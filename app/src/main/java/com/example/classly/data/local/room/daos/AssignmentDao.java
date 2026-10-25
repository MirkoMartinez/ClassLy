package com.example.classly.data.local.room.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.classly.data.local.room.entities.AssignmentEntity;
import java.util.List;

@Dao
public interface AssignmentDao {
    @Query("SELECT * FROM assignments")
    List<AssignmentEntity> getAllAssignments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssignment(AssignmentEntity assignment);

    @Update
    void updateAssignment(AssignmentEntity assignment);

    @Query("DELETE FROM assignments WHERE id = :id")
    void deleteAssignmentById(String id);
}
