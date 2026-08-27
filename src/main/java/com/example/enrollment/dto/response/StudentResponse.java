package com.example.enrollment.dto.response;

import com.example.enrollment.entity.Student;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private String rollNo;
    private Integer semester;
    private LocalDateTime createdAt;

    public static StudentResponse from(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .rollNo(student.getRollNo())
                .semester(student.getSemester())
                .createdAt(student.getCreatedAt())
                .build();
    }
}