package com.srt.dto;

public record TaskResponse(
        Long id,
        String title,
        boolean completed) {

}
