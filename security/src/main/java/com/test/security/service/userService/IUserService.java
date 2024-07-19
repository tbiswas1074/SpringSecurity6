package com.test.security.service.userService;

import com.test.security.config.AuthenticationRequest;
import com.test.security.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IUserService {

    public ResponseEntity<?> registerUser(User user);

    ResponseEntity<?> loginUser(AuthenticationRequest authRequest);
}
