package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.CourseRequest;
import com.example.enrollment.dto.response.CourseResponse;
import com.example.enrollment.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(@Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Course created successfully", response));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getById(@PathVariable Long id) {
        CourseResponse response = courseService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Course fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAll() {
        List<CourseResponse> response = courseService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Courses fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        CourseResponse response = courseService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Course updated successfully", response));
    }
}
