package com.example.todolist.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todolist.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE dueDate = :dueDate")
    List<Task> getTasksByDate(String dueDate);

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    void updateTaskStatus(int taskId, boolean isCompleted);
}

