package com.quest.etna.controller;

import com.quest.etna.dto.CommentDto;
import com.quest.etna.dto.LessonDto;
import com.quest.etna.exception.UnauthorizedAccessException;
import com.quest.etna.model.data.Category;
import com.quest.etna.model.data.Comment;
import com.quest.etna.model.data.Lesson;
import com.quest.etna.model.data.User;
import com.quest.etna.repositories.CategoryRepository;
import com.quest.etna.repositories.CommentRepository;
import com.quest.etna.repositories.LessonRepository;
import com.quest.etna.repositories.UserRepository;
import com.quest.etna.utils.PersistenceUtils;
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
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping()
    public ResponseEntity<?> getComments(Principal principal) {
        User user = this.userRepository.findByUsername(principal.getName());

        LinkedList<Comment> allComments = commentRepository.findByUserId(user.getId());

        return new ResponseEntity<>(allComments, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> postComment(@RequestBody @Validated CommentDto commentDto, Principal principal) {
        LinkedHashMap<String, Object> responseHashMap = new LinkedHashMap<>();

        User user = this.userRepository.findByUsername(principal.getName());

        if (!user.isTeacher() && !user.isAdmin()) {
            throw new UnauthorizedAccessException();
        }

        Optional<Lesson> optionalLesson = this.lessonRepository.findById(commentDto.getLessonId());

        if(optionalLesson.isEmpty()) {
            responseHashMap.put("message", "The lesson id " + commentDto.getLessonId() + " doesn't exist.");

            return new ResponseEntity<>(responseHashMap, HttpStatus.NOT_FOUND);
        }

        Lesson lesson = optionalLesson.get();
        Comment comment = new Comment(commentDto);
        comment.setUser(user);
        comment.setLesson(lesson);

        this.commentRepository.save(comment);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
}

