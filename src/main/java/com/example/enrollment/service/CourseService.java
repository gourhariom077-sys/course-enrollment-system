package com.example.enrollment.service;

import com.example.enrollment.dto.request.CourseRequest;
import com.example.enrollment.dto.response.CourseResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.enums.CourseStatus;
import com.example.enrollment.repository.CourseRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepo courseRepo;

    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    public CourseResponse create(CourseRequest request) {
        if (courseRepo.existsByCode(request.getCode())) {
            throw new RuntimeException("Course code already exists: " + request.getCode());
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

    public CourseResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<CourseResponse> getAll() {
        List<Course> courses = courseRepo.findAll();
        List<CourseResponse> result = new ArrayList<>();
        for (Course course : courses) {
            result.add(toResponse(course));
        }
        return result;
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
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    private CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setCode(course.getCode());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setCapacity(course.getCapacity());
        response.setCredits(course.getCredits());
        response.setStatus(course.getCourseStatus());
        response.setCreatedAt(course.getCreatedAt());
        response.setUpdatedAt(course.getUpdatedAt());
        return response;
    }
}