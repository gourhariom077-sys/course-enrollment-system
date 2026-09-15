package com.example.enrollment.service;

import com.example.enrollment.dto.request.GradeRequest;
import com.example.enrollment.dto.response.GradeResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Grade;
import com.example.enrollment.entity.Student;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.GradeRepo;
import com.example.enrollment.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + request.getStudentId()));

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

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

    public GradeResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<GradeResponse> getAll() {
        List<Grade> grades = gradeRepo.findAll();
        List<GradeResponse> result = new ArrayList<>();
        for (Grade grade : grades) {
            result.add(toResponse(grade));
        }
        return result;
    }

    public List<GradeResponse> getByStudentId(Long studentId) {
        List<Grade> grades = gradeRepo.findByStudentId(studentId);
        List<GradeResponse> result = new ArrayList<>();
        for (Grade grade : grades) {
            result.add(toResponse(grade));
        }
        return result;
    }

    public GradeResponse update(Long id, GradeRequest request) {
        Grade grade = findOrThrow(id);
        grade.setMarks(request.getMarks());
        grade.setGradeLetter(request.getGradeLetter());
        grade.setRemarks(request.getRemarks());
        grade.setUpdatedAt(LocalDateTime.now());
        return toResponse(gradeRepo.save(grade));
    }

    public void delete(Long id) {
        Grade grade = findOrThrow(id);
        gradeRepo.delete(grade);
    }

    private Grade findOrThrow(Long id) {
        return gradeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
    }

    private GradeResponse toResponse(Grade grade) {
        GradeResponse response = new GradeResponse();
        response.setId(grade.getId());
        response.setStudentId(grade.getStudent().getId());
        response.setCourseId(grade.getCourse().getId());
        response.setMarks(grade.getMarks());
        response.setGradeLetter(grade.getGradeLetter());
        response.setRemarks(grade.getRemarks());
        response.setGradedAt(grade.getGradedAt());
        response.setCreatedAt(grade.getCreatedAt());
        response.setUpdatedAt(grade.getUpdatedAt());
        return response;
    }
}