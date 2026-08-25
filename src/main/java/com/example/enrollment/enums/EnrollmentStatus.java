package com.example.enrollment.enums;

public enum EnrollmentStatus {

    ENROLLED,
    WAITLISTED,
    DROPPED,
    COMPLETED;

    public boolean canMoveTo(EnrollmentStatus next) {
        return switch (this) {
            case WAITLISTED -> next == ENROLLED  || next == DROPPED;
            case ENROLLED   -> next == COMPLETED || next == DROPPED;
            case DROPPED, COMPLETED -> false;
        };
    }
}
