package com.quest.etna.controller;

import com.quest.etna.dto.LessonDto;
import com.quest.etna.exception.UnauthorizedAccessException;
import com.quest.etna.model.data.Category;
import com.quest.etna.model.data.Lesson;
import com.quest.etna.model.data.User;
import com.quest.etna.repositories.CategoryRepository;
import com.quest.etna.repositories.LessonRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.utils.PersistenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

@RestController
@RequestMapping("/lesson")
public class LessonController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping()
    public ResponseEntity<?> getLessons() {
        LinkedList<Lesson> allLessons = lessonRepository.findAll();

        return new ResponseEntity<>(allLessons, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postLesson(@RequestBody @Validated LessonDto lessonDto, Principal principal) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();

        User user = this.userRepository.findByUsername(principal.getName());

        if (!user.isTeacher() && !user.isAdmin()) {
            throw new UnauthorizedAccessException();
        }

        Optional<Category> optionalCategory = this.categoryRepository.findById(lessonDto.getCategoryId());

        if(optionalCategory.isEmpty()) {
            responseHashMap.put("message", "The category id " + lessonDto.getCategoryId() + " doesn't exist.");

            return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
        }

        Category category = optionalCategory.get();
        Lesson lesson = new Lesson(lessonDto);

        lesson.setAuthor(user);
        lesson.setCategory(category);

        this.lessonRepository.save(lesson);

        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLesson(@PathVariable(name = "id") int id) {
        LinkedHashMap<String, String> responseHashMap = new LinkedHashMap<>();
        Optional<Lesson> optionalLesson = this.lessonRepository.findById(id);

        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();

            return new ResponseEntity<>(lesson, HttpStatus.OK);
        }

        responseHashMap.put("message", "The lesson id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> putLesson(@PathVariable(name = "id") int id, @RequestBody LessonDto lessonDto, Principal principal) {
        LinkedHashMap<String, String> responseHashMap = new LinkedHashMap<>();
        Optional<Lesson> optionalLesson = this.lessonRepository.findById(id);
        Optional<Category> optionalCategory = this.categoryRepository.findById(lessonDto.getCategoryId());

        User user = this.userRepository.findByUsername(principal.getName());

        if (optionalCategory.isEmpty()) {
            responseHashMap.put("message", "The category id " + lessonDto.getCategoryId() + " doesn't exist.");

            return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
        }

        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            if (user.getId() == lesson.getAuthor().getId() || user.isAdmin()) {
                PersistenceUtils.myCopyProperties(lessonDto, lesson);

                this.lessonRepository.save(lesson);

                return new ResponseEntity<>(lesson, HttpStatus.OK);
            }

            throw new UnauthorizedAccessException();
        }

        responseHashMap.put("message", "The lesson id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable(name = "id") int id, Principal principal) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();
        Optional<Lesson> optionalLesson = this.lessonRepository.findById(id);
        User user = this.userRepository.findByUsername(principal.getName());

        if (optionalLesson.isPresent()) {
            Lesson lesson = optionalLesson.get();
            if (user.getId() == lesson.getAuthor().getId() || user.isAdmin()) {
                this.lessonRepository.delete(lesson);

                return new ResponseEntity<>(lesson, HttpStatus.OK);
            }

            throw new UnauthorizedAccessException();

        }

        responseHashMap.put("message", "The lesson id " + id + " doesn't exist.");

        return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
    }

}

