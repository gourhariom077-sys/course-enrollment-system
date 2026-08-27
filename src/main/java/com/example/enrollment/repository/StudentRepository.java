package com.example.enrollment.repository;

import com.example.enrollment.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByRollNo(String rollNo);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRollNoAndIdNot(String rollNo, Long id);
}