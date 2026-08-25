package com.example.enrollment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "grades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private Enrollment enrollment;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal marks;

    @Column(name = "grade_letter", nullable = false, length = 2)
    private String gradeLetter;

    @Column(name = "graded_at", nullable = false, updatable = false)
    private LocalDateTime gradedAt;

    @PrePersist
    void onCreate() {
        this.gradedAt = LocalDateTime.now();
    }
}