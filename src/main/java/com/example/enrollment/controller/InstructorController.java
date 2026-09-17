package com.example.enrollment.controller;

import com.example.enrollment.common.ApiResponse;
import com.example.enrollment.dto.request.InstructorRequest;
import com.example.enrollment.dto.response.InstructorResponse;
import com.example.enrollment.service.InstructorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InstructorResponse>> create(@Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Instructor created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstructorResponse>>> getAll(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) Long courseId) {

        List<InstructorResponse> response = instructorService.search(id, specialization, courseId);
        return ResponseEntity.ok(ApiResponse.success("Instructors fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponse>> update(@PathVariable Long id, @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Instructor updated successfully", response));
    }
}
