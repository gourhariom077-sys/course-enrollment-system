package com.example.enrollment.service;

import com.example.enrollment.dto.request.LoginRequest;
import com.example.enrollment.dto.request.RegisterRequest;
import com.example.enrollment.dto.response.AuthResponse;
import com.example.enrollment.entity.User;
import com.example.enrollment.enums.Role;
import com.example.enrollment.exception.ConflictException;
import com.example.enrollment.repository.StudentRepo;
import com.example.enrollment.repository.UserRepo;
import com.example.enrollment.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final StudentRepo studentRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepo userRepo, StudentRepo studentRepo, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.studentRepo = studentRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username already exists: " + request.getUsername());
        }

        Long studentId = null;
        if (request.getRole() == Role.STUDENT) {
            if (request.getStudentId() == null) {
                throw new ConflictException("studentId is required for STUDENT role");
            }
            if (!studentRepo.existsById(request.getStudentId())) {
                throw new ConflictException("No student found with id: " + request.getStudentId());
            }
            if (userRepo.existsByStudentId(request.getStudentId())) {
                throw new ConflictException("This studentId is already linked to another user");
            }
            studentId = request.getStudentId();
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStudentId(studentId);
        userRepo.save(user);


        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return AuthResponse.builder()
                .token(token).username(user.getUsername()).role(user.getRole()).build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return AuthResponse.builder()
                .token(token).username(user.getUsername()).role(user.getRole()).build();
    }
}