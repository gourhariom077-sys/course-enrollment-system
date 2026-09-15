package com.example.enrollment.controller;

import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.enums.EnrollmentStatus;
import com.example.enrollment.service.StudentService;
import jakarta.validation.Valid;
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
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping("/search")
    public List<StudentResponse> search(@RequestParam(required = false) Long id,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String phone,
                                        @RequestParam(required = false) String email) {
        return studentService.search(id, name, phone, email);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public StudentResponse changeStatus(@PathVariable Long id, @RequestParam EnrollmentStatus status) {
        return studentService.changeEnrollmentStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
