package com.example.enrollment.service;

import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Student;
import com.example.enrollment.enums.EnrollmentStatus;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.StudentRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;

    public StudentService(StudentRepo studentRepo, CourseRepo courseRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public StudentResponse create(StudentRequest request) {
        if (studentRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists: " + request.getEmail());
        }
        if (studentRepo.existsByRollNo(request.getRollNo())) {
            throw new RuntimeException("Roll number already exists: " + request.getRollNo());
        }

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + request.getCourseId()));

        Student student = new Student();
        student.setRollNo(request.getRollNo());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setCourse(course);
        student.setSemester(request.getSemester());
        student.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
        student.setCreatedAt(LocalDateTime.now());

        return toResponse(studentRepo.save(student));
    }

    public List<StudentResponse> search(Long id, String name, String phone, String email) {
        List<Student> students;

        if (id != null) {
            students = new ArrayList<>();
            studentRepo.findById(id).ifPresent(students::add);
        } else if (email != null) {
            students = new ArrayList<>();
            studentRepo.findByEmail(email).ifPresent(students::add);
        } else if (phone != null) {
            students = studentRepo.findByPhone(phone);
        } else if (name != null) {
            students = studentRepo.findByNameContainingIgnoreCase(name);
        } else {
            students = studentRepo.findAll();
        }

        List<StudentResponse> result = new ArrayList<>();
        for (Student student : students) {
            result.add(toResponse(student));
        }
        return result;
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findOrThrow(id);
        student.setName(request.getName());
        student.setPhone(request.getPhone());
        student.setSemester(request.getSemester());
        student.setUpdatedAt(LocalDateTime.now());
        return toResponse(studentRepo.save(student));
    }

    public void delete(Long id) {
        Student student = findOrThrow(id);
        studentRepo.delete(student);
    }

    public StudentResponse changeEnrollmentStatus(Long id, EnrollmentStatus newStatus) {
        Student student = findOrThrow(id);
        if (!student.getEnrollmentStatus().canMoveTo(newStatus)) {
            throw new RuntimeException("Cannot change status from " + student.getEnrollmentStatus() + " to " + newStatus);
        }
        student.setEnrollmentStatus(newStatus);
        student.setUpdatedAt(LocalDateTime.now());
        return toResponse(studentRepo.save(student));
    }

    private Student findOrThrow(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setRollNo(student.getRollNo());
        response.setName(student.getName());
        response.setEmail(student.getEmail());
        response.setPhone(student.getPhone());
        response.setCourseId(student.getCourse().getId());
        response.setSemester(student.getSemester());
        response.setEnrollmentStatus(student.getEnrollmentStatus());
        response.setCreatedAt(student.getCreatedAt());
        response.setUpdatedAt(student.getUpdatedAt());
        return response;
    }
}