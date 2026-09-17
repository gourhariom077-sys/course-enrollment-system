package com.example.enrollment.repository;

import com.example.enrollment.entity.Student;
import com.example.enrollment.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {

        boolean existsByEmail(String email);
        boolean existsByRollNo(String rollNo);
        List<Student> findByCourseId(Long courseId);
    Optional<Student> findByRollNo(String rollNo);
    Optional<Student> findByEmail(String email);
    List<Student> findByPhone(String phone);
    List<Student> findByNameContainingIgnoreCase(String name);
    long countByCourseIdAndEnrollmentStatus(Long courseId, EnrollmentStatus status);
    List<Student> findByCourseIdAndEnrollmentStatusOrderByCreatedAtAsc(Long courseId, EnrollmentStatus status);



    }

