package com.example.enrollment.repository;

import com.example.enrollment.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepo extends JpaRepository<Grade, Long> {

    }
