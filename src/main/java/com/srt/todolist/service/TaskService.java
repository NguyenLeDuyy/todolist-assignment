package com.srt.todolist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.srt.dto.TaskRequest;
import com.srt.dto.TaskResponse;
import com.srt.exception.ResourceNotFoundException;
import com.srt.todolist.entity.Task;
import com.srt.todolist.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.isCompleted()))
                .toList();
    }

    public List<TaskResponse> getTaskByStatus(boolean completed) {
        return taskRepository.findByCompleted(completed)
                .stream()
                .map(task -> new TaskResponse(task.getId(), task.getTitle(), task.isCompleted()))
                .toList();
    }

    public TaskResponse createTask(TaskRequest request) {
        Task newTask = new Task();

        newTask.setCompleted(false);
        newTask.setTitle(request.title());

        Task savedTask = taskRepository.save(newTask);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.isCompleted());
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task updatedTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy task với ID: " + id));

        updatedTask.setCompleted(request.completed());
        updatedTask.setTitle(request.title());

        taskRepository.save(updatedTask);
        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.isCompleted());
    }

    public void deleteTask(Long id) {
        Task deletedTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy task với ID: " + id));
        taskRepository.delete(deletedTask);
    }
}
