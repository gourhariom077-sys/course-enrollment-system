package com.example.enrollment.repository;

import com.example.enrollment.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepo extends JpaRepository<Grade, Long> {

    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByCourseId(Long courseId);

    }
