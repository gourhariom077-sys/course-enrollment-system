package com.example.enrollment.dto.response;

import com.example.enrollment.enums.CourseStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
