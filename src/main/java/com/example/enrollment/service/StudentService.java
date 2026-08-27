package com.example.enrollment.service;

import com.example.enrollment.dto.request.StudentRequest;
import com.example.enrollment.dto.response.StudentResponse;
import com.example.enrollment.entity.Student;
import com.example.enrollment.exception.DuplicateResourceException;
import com.example.enrollment.exception.ResourceNotFoundException;
import com.example.enrollment.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    @Transactional
    public StudentResponse create(StudentRequest request) {

        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Student already exists with email: " + request.getEmail());
        }

        if (studentRepository.existsByRollNo(request.getRollNo())) {
            throw new DuplicateResourceException(
                    "Student already exists with roll number: " + request.getRollNo());
        }

        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .rollNo(request.getRollNo())
                .semester(request.getSemester())
                .build();

        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(StudentResponse::from);
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentResponse.from(findOrThrow(id));
    }

    @Transactional
    public StudentResponse update(Long id, StudentRequest request) {

        Student student = findOrThrow(id);

        if (studentRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException(
                    "Another student already uses email: " + request.getEmail());
        }

        if (studentRepository.existsByRollNoAndIdNot(request.getRollNo(), id)) {
            throw new DuplicateResourceException(
                    "Another student already uses roll number: " + request.getRollNo());
        }

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setRollNo(request.getRollNo());
        student.setSemester(request.getSemester());

        return StudentResponse.from(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        Student student = findOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
