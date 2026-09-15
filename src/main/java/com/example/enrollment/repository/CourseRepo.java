package com.example.enrollment.repository;

import com.example.enrollment.entity.Course;
import com.example.enrollment.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);
    Optional<Course> findByCode(String code);
    List<Course> findByCourseStatus(CourseStatus courseStatus);
}