package com.example.enrollment.controller;

import com.example.enrollment.dto.request.InstructorRequest;
import com.example.enrollment.dto.response.InstructorResponse;
import com.example.enrollment.service.InstructorService;
import jakarta.validation.Valid;
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
    public InstructorResponse create(@Valid @RequestBody InstructorRequest request) {
        return instructorService.create(request);
    }

    @GetMapping("/{id}")
    public InstructorResponse getById(@PathVariable Long id) {
        return instructorService.getById(id);
    }

    @GetMapping
    public List<InstructorResponse> getAll() {
        return instructorService.getAll();
    }

    @PutMapping("/{id}")
    public InstructorResponse update(@PathVariable Long id, @Valid @RequestBody InstructorRequest request) {
        return instructorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        instructorService.delete(id);
    }
}
