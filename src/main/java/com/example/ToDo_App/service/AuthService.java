package com.example.ToDo_App.service;

import com.example.ToDo_App.entity.User;
import com.example.ToDo_App.repository.UserRepository;
import com.example.ToDo_App.dto.AuthRequest;
import com.example.ToDo_App.dto.AuthResponse;
import com.example.ToDo_App.config.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return "Đăng ký thành công!";
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Sai username hoặc password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Sai username hoặc password");
        }

        String token = jwtUtils.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}