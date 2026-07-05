package com.srt.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.srt.todolist.entity.Task;
import com.srt.todolist.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTaskByStatus(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    public Task createTask(Task task) {
        task.setCompleted(false);
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails) {

        Task updatedTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với ID: " + id));

        updatedTask.setCompleted(taskDetails.isCompleted());
        updatedTask.setTitle(taskDetails.getTitle());
        return taskRepository.save(updatedTask);
    }

    public void deleteTask(Long id) {
        Task deletedTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với ID: " + id));
        taskRepository.delete(deletedTask);
    }
}
