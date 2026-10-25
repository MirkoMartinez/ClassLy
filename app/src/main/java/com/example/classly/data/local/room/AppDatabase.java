package com.example.classly.data.local.room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.classly.data.local.room.daos.AssignmentDao;
import com.example.classly.data.local.room.daos.ClassDao;
import com.example.classly.data.local.room.daos.ExamDao;
import com.example.classly.data.local.room.daos.SubjectDao;
import com.example.classly.data.local.room.entities.AssignmentEntity;
import com.example.classly.data.local.room.entities.ClassEntity;
import com.example.classly.data.local.room.entities.ExamEntity;
import com.example.classly.data.local.room.entities.SubjectEntity;

@Database(entities = {SubjectEntity.class, ExamEntity.class, AssignmentEntity.class, ClassEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "classly_db";

    public abstract SubjectDao subjectDao();
    public abstract ExamDao examDao();
    public abstract AssignmentDao assignmentDao();
    public abstract ClassDao classDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
