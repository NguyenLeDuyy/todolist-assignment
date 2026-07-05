package com.srt.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
    @NotBlank(message = "Tên công việc không được để trống")
    String title,

    boolean completed
) {}
