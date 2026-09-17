package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.GradeRequest;
import com.example.enrollment.dto.response.GradeResponse;
import com.example.enrollment.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GradeResponse>> create(@Valid @RequestBody GradeRequest request) {
        GradeResponse response = gradeService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Grade recorded successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeResponse>>> getAll(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long courseId) {

        List<GradeResponse> response = gradeService.search(id, studentId, courseId);
        return ResponseEntity.ok(ApiResponse.success("Grades fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeResponse>> update(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        GradeResponse response = gradeService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Grade updated successfully", response));
    }


}
