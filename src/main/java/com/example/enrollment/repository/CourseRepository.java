
package com.example.enrollment.repository;

import com.example.enrollment.entity.Course;
import com.example.enrollment.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
}