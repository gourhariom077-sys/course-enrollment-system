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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponse>> getById(@PathVariable Long id) {
        InstructorResponse response = instructorService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Instructor fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InstructorResponse>>> getAll() {
        List<InstructorResponse> response = instructorService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Instructors fetched successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InstructorResponse>> update(@PathVariable Long id, @Valid @RequestBody InstructorRequest request) {
        InstructorResponse response = instructorService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Instructor updated successfully", response));
    }
}
