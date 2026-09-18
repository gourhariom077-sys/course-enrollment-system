package com.example.enrollment.service;

import com.example.enrollment.dto.request.CourseRequest;
import com.example.enrollment.dto.response.CourseResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.enums.CourseStatus;
import com.example.enrollment.exception.ConflictException;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.CourseRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepo courseRepo;

    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    public CourseResponse create(CourseRequest request) {
        if (courseRepo.existsByCode(request.getCode())) {
            throw new ConflictException("Course code already exists: " + request.getCode());
        }

        Course course = new Course();
        course.setCode(request.getCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCapacity(request.getCapacity());
        course.setCredits(request.getCredits());
        course.setCourseStatus(CourseStatus.OPEN);
        course.setCreatedAt(LocalDateTime.now());

        return toResponse(courseRepo.save(course));
    }

    public List<CourseResponse> search(Long id, String code, CourseStatus status) {
        Specification<Course> spec = buildSpec(id, code, status);
        return courseRepo.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    private Specification<Course> buildSpec(Long id, String code, CourseStatus status) {
        return (root, query, cb) -> cb.and(
                id == null ? cb.conjunction() : cb.equal(root.get("id"), id),
                code == null ? cb.conjunction() : cb.equal(root.get("code"), code),
                status == null ? cb.conjunction() : cb.equal(root.get("courseStatus"), status)
        );
    }

    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findOrThrow(id);
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setCapacity(request.getCapacity());
        course.setCredits(request.getCredits());
        course.setUpdatedAt(LocalDateTime.now());
        return toResponse(courseRepo.save(course));
    }

    private Course findOrThrow(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private CourseResponse toResponse(Course course) {
        return CourseResponse.builder().id(course.getId()).code(course.getCode())
                .title(course.getTitle()).description(course.getDescription())
                .capacity(course.getCapacity()).credits(course.getCredits())
                .status(course.getCourseStatus()).createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt()).build();
    }
}