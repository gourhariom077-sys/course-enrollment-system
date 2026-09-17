package com.example.enrollment.repository;

import com.example.enrollment.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorRepo extends JpaRepository<Instructor, Long> {

        boolean existsByEmail(String email);
        List<Instructor> findByCourseId(Long courseId);

    List<Instructor> findBySpecializationIgnoreCaseAndCourseId(String specialization, Long courseId);

    List<Instructor> findBySpecializationIgnoreCase(String specialization);
}

