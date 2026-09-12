package com.example.enrollment.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest {

    @NotBlank(message = "Course code is required")
        private String code;

    @NotBlank(message = "Title is required")
        private String title;

        private String description;

        @NotNull(message = "Capacity is required")
        @Min(value = 1, message = "Capacity must be atleast 1")
        private Integer capacity;

        @NotNull(message = "Credits required")
        @Min(value = 1)
        @Max(value = 10)
        private Integer credits;
    }

