package com.example.todolist.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.models.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    // Task list and context
    private List<Task> taskList;
    private Context context;
    private final OnTaskClickListener taskClickListener;

    // Interface for handling task click events
    public interface OnTaskClickListener {
        void onTaskUpdate(Task task);
        void onTaskDelete(Task task);
        void onTaskStatusChanged(int taskId, boolean isCompleted);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener taskClickListener) {
        this.context = context;
        this.taskList = taskList;
        this.taskClickListener = taskClickListener;
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Set task details
        holder.taskName.setText(task.getName());
        holder.taskDescription.setText(task.getDescription());
        // Remove listener temporarily
        holder.taskCompleted.setOnCheckedChangeListener(null);
        holder.taskCompleted.setChecked(task.isCompleted());

        // Set task appearance based on completion status
        updateTaskAppearance(holder, task.isCompleted());

        // Update task completion status
        holder.taskCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            taskClickListener.onTaskStatusChanged(task.getId(), isChecked);
            // Set task appearance based on the new completion status
            updateTaskAppearance(holder, isChecked);
        });

        // Set click listeners for edit and delete buttons
        holder.ivEditTask.setOnClickListener(v -> {
            taskClickListener.onTaskUpdate(task);
        });
        holder.ivDeleteTask.setOnClickListener(v -> {
            taskClickListener.onTaskDelete(task);
        });
    }

    // Method to update task appearance based on completion status
    private void updateTaskAppearance(TaskViewHolder holder, boolean isCompleted) {
        holder.ivDeleteTask.setVisibility(isCompleted ? View.VISIBLE : View.GONE);
        holder.ivEditTask.setVisibility(isCompleted ? View.GONE : View.VISIBLE);

        int color = holder.itemView.getContext().getColor(
                isCompleted ? R.color.gray : R.color.black);
        holder.taskName.setTextColor(color);
        holder.taskDescription.setTextColor(color);

        int paintFlags = holder.taskName.getPaintFlags();
        holder.taskName.setPaintFlags(isCompleted ?
                (paintFlags | Paint.STRIKE_THRU_TEXT_FLAG) :
                (paintFlags & ~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskDescription;
        CheckBox taskCompleted;
        ImageView ivEditTask, ivDeleteTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.tvTaskName);
            taskDescription = itemView.findViewById(R.id.tvTaskDescription);
            taskCompleted = itemView.findViewById(R.id.cbTaskCompleted);
            ivEditTask = itemView.findViewById(R.id.ivEditTask);
            ivDeleteTask = itemView.findViewById(R.id.ivDeleteTask);
        }
    }
}
