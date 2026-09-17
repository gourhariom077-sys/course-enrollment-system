package com.example.enrollment.service;

import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Student;
import com.example.enrollment.enums.EnrollmentStatus;
import com.example.enrollment.exception.ConflictException;
import com.example.enrollment.exception.ResourceNotFoundException;
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
            throw new ConflictException("Email already exists: " + request.getEmail());
        }
        if (studentRepo.existsByRollNo(request.getRollNo())) {
            throw new ConflictException("Roll number already exists: " + request.getRollNo());
        }

        Course course = courseRepo.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        Student student = new Student();
        student.setRollNo(request.getRollNo());
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setCourse(course);
        student.setSemester(request.getSemester());

        long enrolledCount = studentRepo.countByCourseIdAndEnrollmentStatus(course.getId(), EnrollmentStatus.ENROLLED);
        if (enrolledCount < course.getCapacity()) {
            student.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
        } else {
            student.setEnrollmentStatus(EnrollmentStatus.WAITLISTED);
        }
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

    public StudentResponse changeEnrollmentStatus(Long id, EnrollmentStatus newStatus) {
        Student student = findOrThrow(id);
        if (!student.getEnrollmentStatus().canMoveTo(newStatus)) {
            throw new ConflictException("Cannot change status from " + student.getEnrollmentStatus() + " to " + newStatus);
        }

        student.setEnrollmentStatus(newStatus);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepo.save(student);

        if (newStatus == EnrollmentStatus.DROPPED || newStatus == EnrollmentStatus.COMPLETED) {
            List<Student> waitlist = studentRepo.findByCourseIdAndEnrollmentStatusOrderByCreatedAtAsc(
                    student.getCourse().getId(), EnrollmentStatus.WAITLISTED);
            if (!waitlist.isEmpty()) {
                Student nextStudent = waitlist.get(0);
                nextStudent.setEnrollmentStatus(EnrollmentStatus.ENROLLED);
                nextStudent.setUpdatedAt(LocalDateTime.now());
                studentRepo.save(nextStudent);
            }
        }

        return toResponse(student);
    }

    private Student findOrThrow(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .rollNo(student.getRollNo())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .courseId(student.getCourse().getId())
                .semester(student.getSemester())
                .enrollmentStatus(student.getEnrollmentStatus())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}









