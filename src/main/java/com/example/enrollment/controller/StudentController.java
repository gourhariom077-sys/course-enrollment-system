package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.enums.EnrollmentStatus;
import com.example.enrollment.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student created successfully", response));
    }

    // No params -> returns all students. Any param given -> filters by that field.
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll(@RequestParam(required = false) Long id,
                                                                     @RequestParam(required = false) String name,
                                                                     @RequestParam(required = false) String phone,
                                                                     @RequestParam(required = false) String email) {
        List<StudentResponse> response = studentService.search(id, name, phone, email);
        return ResponseEntity.ok(ApiResponse.success("Students fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        StudentResponse response = studentService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student updated successfully", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<StudentResponse>> changeStatus(@PathVariable Long id, @RequestParam EnrollmentStatus status) {
        StudentResponse response = studentService.changeEnrollmentStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Student status updated successfully", response));
    }
}
