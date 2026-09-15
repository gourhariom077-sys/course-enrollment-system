package com.example.enrollment.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
