package com.example.enrollment.service;

import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.entity.Course;
import com.example.enrollment.entity.Student;
import com.example.enrollment.entity.User;
import com.example.enrollment.enums.EnrollmentStatus;
import com.example.enrollment.enums.Role;
import com.example.enrollment.exception.ConflictException;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.CourseRepo;
import com.example.enrollment.repository.StudentRepo;
import com.example.enrollment.repository.UserRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepo studentRepo;
    private final CourseRepo courseRepo;
    private final UserRepo userRepo;

    public StudentService(StudentRepo studentRepo, CourseRepo courseRepo, UserRepo userRepo) {
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
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
        Specification<Student> spec = buildSpec(id, name, phone, email);
        return studentRepo.findAll(spec).stream()
                .map(this::toResponse)
                .toList();
    }

    private Specification<Student> buildSpec(Long id, String name, String phone, String email) {
        return (root, query, cb) -> cb.and(
                id == null ? cb.conjunction() : cb.equal(root.get("id"), id),
                name == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"),
                phone == null ? cb.conjunction() : cb.equal(root.get("phone"), phone),
                email == null ? cb.conjunction() : cb.equal(root.get("email"), email)
        );
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findOrThrow(id);
        student.setName(request.getName());
        student.setPhone(request.getPhone());
        student.setSemester(request.getSemester());
        student.setUpdatedAt(LocalDateTime.now());
        return toResponse(studentRepo.save(student));
    }
@Transactional
    public StudentResponse changeEnrollmentStatus(Long id, EnrollmentStatus newStatus) {
        Student student = findOrThrow(id);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        if (currentUser.getRole() == Role.STUDENT && !id.equals(currentUser.getStudentId())) {
            throw new AccessDeniedException("You can only change your own enrollment status");
        }

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