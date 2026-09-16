package com.example.enrollment.service;

import com.example.enrollment.dto.request.InstructorRequest;
import com.example.enrollment.dto.response.InstructorResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Instructor;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.InstructorRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorService {

    private final InstructorRepo instructorRepo;
    private final CourseRepo courseRepo;

    public InstructorService(InstructorRepo instructorRepo, CourseRepo courseRepo) {
        this.instructorRepo = instructorRepo;
        this.courseRepo = courseRepo;
    }

    public InstructorResponse create(InstructorRequest request) {
        if (instructorRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        Instructor instructor = new Instructor();
        instructor.setName(request.getName());
        instructor.setEmail(request.getEmail());
        instructor.setPhone(request.getPhone());
        instructor.setSpecialization(request.getSpecialization());
        instructor.setCourse(course);
        instructor.setCreatedAt(LocalDateTime.now());

        return toResponse(instructorRepo.save(instructor));
    }

    public InstructorResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<InstructorResponse> getAll() {
        List<Instructor> instructors = instructorRepo.findAll();
        List<InstructorResponse> result = new ArrayList<>();
        for (Instructor instructor : instructors) {
            result.add(toResponse(instructor));
        }
        return result;
    }

    public InstructorResponse update(Long id, InstructorRequest request) {
        Instructor instructor = findOrThrow(id);
        instructor.setName(request.getName());
        instructor.setPhone(request.getPhone());
        instructor.setSpecialization(request.getSpecialization());
        instructor.setUpdatedAt(LocalDateTime.now());
        return toResponse(instructorRepo.save(instructor));
    }

    private Instructor findOrThrow(Long id) {
        return instructorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));
    }

    private InstructorResponse toResponse(Instructor instructor) {
        InstructorResponse response = new InstructorResponse();
        response.setId(instructor.getId());
        response.setName(instructor.getName());
        response.setEmail(instructor.getEmail());
        response.setPhone(instructor.getPhone());
        response.setSpecialization(instructor.getSpecialization());
        response.setCourseId(instructor.getCourse().getId());
        response.setCreatedAt(instructor.getCreatedAt());
        response.setUpdatedAt(instructor.getUpdatedAt());
        return response;
    }
}
