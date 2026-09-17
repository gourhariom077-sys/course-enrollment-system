package com.example.enrollment.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class GradeResponse {
    private Long id;
    private Long studentId;
    private Long courseId;
    private BigDecimal marks;
    private String gradeLetter;
    private String remarks;
    private LocalDateTime gradedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}