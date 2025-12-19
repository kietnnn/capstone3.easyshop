package org.yearup.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;



import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;

import org.yearup.models.User;
import org.yearup.models.requests.LoginRequest;
import org.yearup.models.requests.RegisterRequest;
import org.yearup.models.responses.LoginResponse;
import org.yearup.security.JwtUtil;
import org.springframework.transaction.annotation.Transactional;


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

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request)
    {
        if (userDao.exists(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        User user = userDao.create(request.toUser());
        profileDao.create(request.toProfile(user.getId()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
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


