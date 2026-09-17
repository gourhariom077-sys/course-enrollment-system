package com.example.enrollment.dto.response;

import com.example.enrollment.enums.CourseStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CourseResponse {
    private Long id;
    private String code;
    private String title;
    private String description;
    private Integer capacity;
    private Integer credits;
    private CourseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}