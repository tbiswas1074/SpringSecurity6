package com.test.security.service.userService;

import com.test.security.config.AuthenticationRequest;
import com.test.security.config.JwtTokenUtil;
import com.test.security.config.MyUserDetailsService;
import com.test.security.model.Role;
import com.test.security.model.SequenceGenerator;
import com.test.security.model.User;
import com.test.security.repository.userRepo.IUserRepo;
import com.test.security.service.databaseService.ISequenceGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private ISequenceGenerator iSequenceGenerator;


    public ResponseEntity<?> registerUser(User user) {
        user.setId(iSequenceGenerator.generateSequence(User.SEQUENCE_NAME));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(Role.USER));
        User savedUser = userRepo.save(user);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(savedUser.getUsername());
        String jwt = jwtTokenUtil.generateToken(userDetails);

        // Create and return the custom response
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("token", jwt);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> loginUser(AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Incorrect username or password", e);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authRequest.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully logged in");
        response.put("userName", userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
