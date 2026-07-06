package com.srt.todolist.service;

import com.srt.dto.TaskRequest;
import com.srt.dto.TaskResponse;
import com.srt.todolist.entity.Task;
import com.srt.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task existingTask;

    // Dữ liệu mẫu
    @BeforeEach
    void setUp() {
        existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Học Spring Boot");
        existingTask.setCompleted(false);
    }

    @Test
    void getAllTasks_returnsListOfTaskResponses() {
        when(taskRepository.findAll()).thenReturn(List.of(existingTask));

        List<TaskResponse> responses = taskService.getAllTasks();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).title()).isEqualTo("Học Spring Boot");
    }

    @Test
    void createTask_savesToDbAndReturnsResponse() {
        // Kịch bản
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        // Thực hiện
        TaskRequest request = new TaskRequest("Đi học tiếng anh", false);
        TaskResponse response = taskService.createTask(request);

        // Kiểm chứng
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.title()).isEqualTo("Đi học tiếng anh");
        assertThat(response.completed()).isFalse();
    }

    @Test
    void updateTask_updatesFieldsAndReturnsResponse() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskRequest request = new TaskRequest("Đã làm bài tập", true);
        TaskResponse response = taskService.updateTask(1L, request);

        assertThat(response.title()).isEqualTo("Đã làm bài tập");
        assertThat(response.completed()).isTrue();
    }

    @Test
    void updateTask_taskNotFound_throwsException() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        TaskRequest request = new TaskRequest("Update thử", true);

        // Dùng AssertJ
        assertThatThrownBy(() -> taskService.updateTask(99L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteTask_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskService.deleteTask(1L);

        // Đảm bảo rằng hàm delete của Repository đã thực sự được gọi 1 lần
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    void deleteTask_notFound_throwsException() {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(42L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("42");
    }
}