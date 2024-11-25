package com.quest.etna.controller;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.dto.AuthenticationDto;
import com.quest.etna.dto.UserDto;
import com.quest.etna.model.JwtUserDetails;
import com.quest.etna.model.UserDetails;
import com.quest.etna.model.data.User;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
public class AuthenticationController {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register")
    private ResponseEntity<?> register(@RequestBody @Validated UserDto userDto) {
        LinkedHashMap<String, String> responseHashMap = new LinkedHashMap<>();

        if (this.userRepository.findByUsername(userDto.getUsername()) == null) {

            User user = new User(userDto);
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            Optional<User> optionalUser = Optional.of(user);

            this.userRepository.save(optionalUser.get());

            UserDetails userDetails = new UserDetails(user.getUsername(), user.getRole());

            return new ResponseEntity<>(userDetails, HttpStatus.CREATED);
        }

        responseHashMap.put("message", "This username already exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.CONFLICT);
    }

    @PostMapping(value = "/authenticate")
    private @ResponseBody ResponseEntity<?> authenticate(@RequestBody @Validated AuthenticationDto authenticationDto) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();

        String username = authenticationDto.getUsername();
        String password = authenticationDto.getPassword();

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException ex) {
            responseHashMap.put("message", "The username or the password are incorrect.");

            return new ResponseEntity<>(responseHashMap, HttpStatus.UNAUTHORIZED);
        }

        if (authentication.isAuthenticated()) {
            JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);
            String token = jwtTokenUtil.generateToken(jwtUserDetails);

            responseHashMap.put("token", token);

            return new ResponseEntity<>(responseHashMap, HttpStatus.OK);
        }

        responseHashMap.put("message", "The username or the password are incorrect.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/me")
    public ResponseEntity<?> me(Principal principal) {
        String username = principal.getName();
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(username);

        UserDetails userDetails = new UserDetails(jwtUserDetails.getUsername(), jwtUserDetails.getUser().getRole());

        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }
}