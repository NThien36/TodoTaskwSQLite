package com.example.todolist.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolist.database.TaskDatabase;
import com.example.todolist.models.Task;
import com.example.todolist.utils.DateUtils;

import java.util.Collections;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    // Database instance and LiveData for tasks
    private final TaskDatabase database;
    // LiveData for tasks
    private final MutableLiveData<List<Task>> tasks;
    // Current selected date
    private String currentSelectedDate;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        database = TaskDatabase.getInstance(application);
        tasks = new MutableLiveData<>();
        currentSelectedDate = DateUtils.getCurrentDate();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    // Method to set the current selected date
    public void setSelectedDate(String date) {
        this.currentSelectedDate = date;
        loadTasksByDate();
    }

    // Method to load tasks for the current selected date
    private void loadTasksByDate() {
        if (currentSelectedDate != null) {
            List<Task> taskList = database.taskDao().getTasksByDate(currentSelectedDate);
            sortTasksByStatus(taskList);
            tasks.setValue(taskList);
        }
    }

    // Method to sort tasks by completion status
    private void sortTasksByStatus(List<Task> tasks) {
        Collections.sort(tasks, (task1, task2) -> {
            if (task1.isCompleted() == task2.isCompleted()) {
                return 0;
            } else if (task1.isCompleted()) {
                return 1; // Completed tasks go to the bottom
            } else {
                return -1; // Incomplete tasks go to the top
            }
        });
    }

    // Other methods for CRUD operations
    public void insertTask(Task task) {
        database.taskDao().insert(task);
        loadTasksByDate();
    }
    public void updateTask(Task task) {
        database.taskDao().update(task);
        loadTasksByDate();
    }
    public void deleteTask(Task task) {
        database.taskDao().delete(task);
        loadTasksByDate();
    }
    public void updateTaskStatus(int taskId, boolean isCompleted) {
        database.taskDao().updateTaskStatus(taskId, isCompleted);
        loadTasksByDate();
    }

}
