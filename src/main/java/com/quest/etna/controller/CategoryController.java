package com.quest.etna.controller;

import com.quest.etna.dto.CategoryDto;
import com.quest.etna.dto.CommentDto;
import com.quest.etna.exception.UnauthorizedAccessException;
import com.quest.etna.model.data.Category;
import com.quest.etna.model.data.Comment;
import com.quest.etna.model.data.Lesson;
import com.quest.etna.model.data.User;
import com.quest.etna.repositories.CategoryRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.LessonRepository;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<?> getCategories() {

        LinkedList<Category> allCategories = categoryRepository.findAll();

        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postComment(@RequestBody @Validated CategoryDto categoryDto, Principal principal) {
        User user = this.userRepository.findByUsername(principal.getName());

        if (!user.isAdmin()) {
            throw new UnauthorizedAccessException();
        }

        Category category = new Category(categoryDto);
        this.categoryRepository.save(category);

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
}

