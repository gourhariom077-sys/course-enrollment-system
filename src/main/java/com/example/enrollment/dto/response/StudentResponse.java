package com.example.enrollment.dto.response;

import com.example.enrollment.enums.EnrollmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
