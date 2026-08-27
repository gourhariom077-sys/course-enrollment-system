package com.example.enrollment.dto.response;

import com.example.enrollment.entity.Course;
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
    private String instructorName;
    private Integer capacity;
    private Integer credits;
    private CourseStatus status;
    private LocalDateTime createdAt;

    public static CourseResponse from(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .instructorName(course.getInstructorName())
                .capacity(course.getCapacity())
                .credits(course.getCredits())
                .status(course.getStatus())
                .createdAt(course.getCreatedAt())
                .build();
    }
}