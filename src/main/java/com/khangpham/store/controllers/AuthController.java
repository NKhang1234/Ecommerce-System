package com.khangpham.store.controllers;

import com.khangpham.store.config.JwtConfig;
import com.khangpham.store.dtos.JwtResponse;
import com.khangpham.store.dtos.LoginRequest;
import com.khangpham.store.dtos.UserDto;
import com.khangpham.store.mappers.UserMapper;
import com.khangpham.store.repositories.UserRepository;
import com.khangpham.store.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("RefreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);

        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PostMapping("/validate")
    public boolean validate(@RequestHeader(name = "Authorization") String authHeader) {
        System.out.println("Validate Called");
        var token = authHeader.replace("Bearer ", "");
        var jwt = jwtService.parseToken(token);
        if (jwt == null || jwt.isExpired()) {
            return false;
        }
        return true;
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "RefreshToken") String refreshToken
    ) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        var newAccessToken = jwtService.generateAccessToken(user);

        return ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        // Get userId from authentication context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        // Fetch user from database by userId
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
