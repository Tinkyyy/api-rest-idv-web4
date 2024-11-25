package com.quest.etna.controller;

import com.quest.etna.dto.UserDto;
import com.quest.etna.exception.UnauthorizedAccessException;
import com.quest.etna.model.Role;
import com.quest.etna.model.data.User;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.utils.PersistenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping()
    public ResponseEntity<?> getUsers() {
        LinkedList<User> users = this.userRepository.findAll();

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") int id) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return new ResponseEntity<>(user, HttpStatus.FORBIDDEN);
        }

        responseHashMap.put("message", "The user id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putUser(@PathVariable(name = "id") int id, @RequestBody UserDto userDto, Principal principal) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User targetUser = optionalUser.get();
            User currentUser = this.userRepository.findByUsername(principal.getName());

            if (currentUser.isAdmin()) {
                if (userDto.getPassword() != null) {
                    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
                }

                PersistenceUtils.myCopyProperties(userDto, targetUser);
                this.userRepository.save(targetUser);

                return new ResponseEntity<>(targetUser, HttpStatus.OK);
            }

            if (currentUser.getId() == targetUser.getId()) {
                targetUser.setUsername(userDto.getUsername());
                this.userRepository.save(targetUser);

                return new ResponseEntity<>(targetUser, HttpStatus.OK);
            }

            throw new UnauthorizedAccessException();
        }

        responseHashMap.put("message", "The address id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") int id, Principal principal) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();
        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User targetUser = optionalUser.get();
            User currentUser = this.userRepository.findByUsername(principal.getName());

            if (currentUser.isAdmin() || currentUser.getId() == targetUser.getId()) {
                this.userRepository.delete(targetUser);

                return new ResponseEntity<>(targetUser, HttpStatus.OK);
            }

            throw new UnauthorizedAccessException();
        }

        responseHashMap.put("message", "The address id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}/admin")
    public ResponseEntity<?> setAdminRole(@PathVariable(name = "id") int id) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();

        Optional<User> optionalUser = this.userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User targetUser = optionalUser.get();

            targetUser.setRole(Role.ADMIN);
            userRepository.save(targetUser);

            return new ResponseEntity<>(targetUser, HttpStatus.OK);
        }

        responseHashMap.put("message", "The user id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

}
