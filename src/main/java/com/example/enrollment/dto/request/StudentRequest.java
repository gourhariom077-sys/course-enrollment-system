package com.example.enrollment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRequest {

    @NotBlank(message = "Roll number is required")
    private String rollNo;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private String phone;

    @NotNull(message = "Course id is required")
    private Long courseId;

    @NotNull(message = "Semester is required")
    @Min(value = 1, message = "Semester must be atleast 1")
    @Max(value = 8, message = "Semester cannot exceed 8")
    private Integer semester;
}