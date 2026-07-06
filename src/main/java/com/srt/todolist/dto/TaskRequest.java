package com.srt.todolist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "Tên công việc không được để trống") @Size(max = 100) String title,

        Boolean completed) {
}
