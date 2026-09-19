package com.example.enrollment.dto.response;

import com.example.enrollment.enums.EnrollmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentResponse {
    private Long id;
    private String rollNo;
    private String name;
    private String email;
    private String phone;
    private Long courseId;
    private Integer semester;
    private EnrollmentStatus enrollmentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
