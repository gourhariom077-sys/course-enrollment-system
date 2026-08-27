package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Student created", studentService.create(request)));
    }

    @GetMapping
    public ApiResponse<Page<StudentResponse>> getAll(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ApiResponse.ok(studentService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(studentService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<StudentResponse> update(
            @PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return ApiResponse.ok("Student updated", studentService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ApiResponse.ok("Student deleted", null);
    }
}
