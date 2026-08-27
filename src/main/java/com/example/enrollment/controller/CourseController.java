package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.CourseRequest;
import com.example.enrollment.dto.response.CourseResponse;
import com.example.enrollment.enums.CourseStatus;
import com.example.enrollment.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<ApiResponse<CourseResponse>> create(
            @Valid @RequestBody CourseRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Course created", courseService.create(request)));
    }

    @GetMapping
    public ApiResponse<Page<CourseResponse>> getAll(
            @RequestParam(required = false) CourseStatus status,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        return ApiResponse.ok(courseService.getAll(status, pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getById(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequest request) {

        return ApiResponse.ok("Course updated", courseService.update(id, request));
    }

    @PatchMapping("/{id}/close")
    public ApiResponse<CourseResponse> close(@PathVariable Long id) {
        return ApiResponse.ok("Course closed", courseService.close(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ApiResponse.ok("Course deleted", null);
    }
}