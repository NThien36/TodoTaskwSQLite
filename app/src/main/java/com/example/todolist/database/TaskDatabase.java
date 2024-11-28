package com.example.todolist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.todolist.DAO.TaskDao;
import com.example.todolist.models.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    // Singleton pattern to ensure only one instance of the database
    private static TaskDatabase instance;

    // Abstract method to provide access to the TaskDao
    public abstract TaskDao taskDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_database")
                    // Allow main thread queries if necessary
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}