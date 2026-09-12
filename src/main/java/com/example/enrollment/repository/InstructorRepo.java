package com.example.enrollment.repository;

import com.example.enrollment.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface InstructorRepo extends JpaRepository<Instructor, Long> {

    }

