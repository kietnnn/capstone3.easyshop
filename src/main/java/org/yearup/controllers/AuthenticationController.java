package org.yearup.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.authentication.LoginDto;
import org.yearup.models.authentication.LoginResponseDto;
import org.yearup.models.authentication.RegisterUserDto;
import org.yearup.models.User;
import org.yearup.models.requests.LoginRequest;
import org.yearup.models.requests.RegisterRequest;
import org.yearup.models.responses.LoginResponse;
import org.yearup.security.JwtUtil;
import org.yearup.security.jwt.JWTFilter;
import org.yearup.security.jwt.TokenProvider;

@RestController
@RequestMapping("/auth")
public class AuthenticationController
{
    private final UserDao userDao;
    private final ProfileDao profileDao;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(
            UserDao userDao,
            ProfileDao profileDao,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager)
    {
        this.userDao = userDao;
        this.profileDao = profileDao;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request)
    {
        User user = userDao.create(request.toUser());
        profileDao.create(request.toProfile(user.getId()));
        return user;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request)
    {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userDao.getByUserName(request.getUsername());
        String token = jwtUtil.generateToken(user);

        return new LoginResponse(token);
    }
}


