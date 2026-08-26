package com.example.enrollment.service;

import com.example.enrollment.dto.request.CourseRequest;
import com.example.enrollment.dto.response.CourseResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.enums.CourseStatus;
import com.example.enrollment.exception.DuplicateResourceException;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @Transactional
    public CourseResponse create(CourseRequest request) {

        if (courseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException(
                    "Course already exists with code: " + request.getCode());
        }

        Course course = Course.builder()
                .code(request.getCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .instructorName(request.getInstructorName())
                .capacity(request.getCapacity())
                .credits(request.getCredits())
                .status(CourseStatus.OPEN)
                .build();

        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAll(CourseStatus status, Pageable pageable) {

        Page<Course> courses = (status == null)
                ? courseRepository.findAll(pageable)
                : courseRepository.findByStatus(status, pageable);

        return courses.map(CourseResponse::from);
    }

    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return CourseResponse.from(findOrThrow(id));
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {

        Course course = findOrThrow(id);

        if (courseRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new DuplicateResourceException(
                    "Another course already uses code: " + request.getCode());
        }

        course.setCode(request.getCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructorName(request.getInstructorName());
        course.setCapacity(request.getCapacity());
        course.setCredits(request.getCredits());

        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse close(Long id) {
        Course course = findOrThrow(id);
        course.setStatus(CourseStatus.CLOSED);
        return CourseResponse.from(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.delete(findOrThrow(id));
    }

    private Course findOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
}