package com.example.enrollment.service;

import com.example.enrollment.dto.request.InstructorRequest;
import com.example.enrollment.dto.response.InstructorResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Instructor;
import com.example.enrollment.exception.ConflictException;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.InstructorRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
            throw new ConflictException("Email already exists: " + request.getEmail());
        }

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Instructor instructor = new Instructor();
        instructor.setName(request.getName());
        instructor.setEmail(request.getEmail());
        instructor.setPhone(request.getPhone());
        instructor.setSpecialization(request.getSpecialization());
        instructor.setCourse(course);
        instructor.setCreatedAt(LocalDateTime.now());

        return toResponse(instructorRepo.save(instructor));
    }

    public List<InstructorResponse> search(Long id, String specialization, Long courseId) {
        Specification<Instructor> spec = buildSpec(id, specialization, courseId);
        return instructorRepo.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    private Specification<Instructor> buildSpec(Long id, String specialization, Long courseId) {
        return (root, query, cb) -> cb.and(
                id == null ? cb.conjunction() : cb.equal(root.get("id"), id),
                specialization == null ? cb.conjunction() : cb.equal(cb.lower(root.get("specialization")), specialization.toLowerCase()),
                courseId == null ? cb.conjunction() : cb.equal(root.get("course").get("id"), courseId)
        );
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
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
    }

    private InstructorResponse toResponse(Instructor instructor) {
        return InstructorResponse.builder()
                .id(instructor.getId())
                .name(instructor.getName())
                .email(instructor.getEmail())
                .phone(instructor.getPhone())
                .specialization(instructor.getSpecialization())
                .courseId(instructor.getCourse().getId())
                .createdAt(instructor.getCreatedAt())
                .updatedAt(instructor.getUpdatedAt())
                .build();
    }
}