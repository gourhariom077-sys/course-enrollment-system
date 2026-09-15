package com.example.enrollment.controller;

import com.example.enrollment.dto.request.GradeRequest;
import com.example.enrollment.dto.response.GradeResponse;
import com.example.enrollment.service.GradeService;
import jakarta.validation.Valid;
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
    public GradeResponse create(@Valid @RequestBody GradeRequest request) {
        return gradeService.create(request);
    }

    @GetMapping("/{id}")
    public GradeResponse getById(@PathVariable Long id) {
        return gradeService.getById(id);
    }

    @GetMapping
    public List<GradeResponse> getAll() {
        return gradeService.getAll();
    }

    @GetMapping("/student/{studentId}")
    public List<GradeResponse> getByStudentId(@PathVariable Long studentId) {
        return gradeService.getByStudentId(studentId);
    }

    @PutMapping("/{id}")
    public GradeResponse update(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        return gradeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        gradeService.delete(id);
    }
}
