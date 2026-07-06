package com.srt.todolist.dto;

public record TaskResponse(
                Long id,
                String title,
                boolean completed) {

}
