package com.example.enrollment.repository;

import com.example.enrollment.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface StudentRepo extends JpaRepository<Student, Long> {

    }

