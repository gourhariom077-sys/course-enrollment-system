package com.example.enrollment.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InstructorResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private Long courseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
