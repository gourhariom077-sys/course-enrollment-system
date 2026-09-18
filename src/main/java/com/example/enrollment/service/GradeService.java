package com.example.enrollment.service;

import com.example.enrollment.dto.request.GradeRequest;
import com.example.enrollment.dto.response.GradeResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Grade;
import com.example.enrollment.entity.Student;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.GradeRepo;
import com.example.enrollment.repository.StudentRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GradeService {

    private final GradeRepo gradeRepo;
    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    public GradeService(GradeRepo gradeRepo, StudentRepo studentRepo, CourseRepo courseRepo) {
        this.gradeRepo = gradeRepo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public GradeResponse create(GradeRequest request) {
        Student student = studentRepo.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Grade grade = new Grade();
        grade.setStudent(student);
        grade.setCourse(course);
        grade.setMarks(request.getMarks());
        grade.setGradeLetter(request.getGradeLetter());
        grade.setRemarks(request.getRemarks());
        grade.setGradedAt(LocalDateTime.now());
        grade.setCreatedAt(LocalDateTime.now());

        return toResponse(gradeRepo.save(grade));
    }

    public List<GradeResponse> search(Long id, Long studentId, Long courseId) {
        Specification<Grade> spec = buildSpec(id, studentId, courseId);
        return gradeRepo.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    private Specification<Grade> buildSpec(Long id, Long studentId, Long courseId) {
        return (root, query, cb) -> cb.and(
                id == null ? cb.conjunction() : cb.equal(root.get("id"), id),
                studentId == null ? cb.conjunction() : cb.equal(root.get("student").get("id"), studentId),
                courseId == null ? cb.conjunction() : cb.equal(root.get("course").get("id"), courseId)
        );
    }

    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = findOrThrow(id);
        grade.setMarks(request.getMarks());
        grade.setGradeLetter(request.getGradeLetter());
        grade.setRemarks(request.getRemarks());
        grade.setUpdatedAt(LocalDateTime.now());
        return toResponse(gradeRepo.save(grade));
    }

    private Grade findOrThrow(Long id) {
        return gradeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));
    }

    private GradeResponse toResponse(Grade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .studentId(grade.getStudent().getId())
                .courseId(grade.getCourse().getId())
                .marks(grade.getMarks())
                .gradeLetter(grade.getGradeLetter())
                .remarks(grade.getRemarks())
                .gradedAt(grade.getGradedAt())
                .createdAt(grade.getCreatedAt())
                .updatedAt(grade.getUpdatedAt())
                .build();
    }
}