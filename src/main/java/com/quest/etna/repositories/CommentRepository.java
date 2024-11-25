package com.quest.etna.repositories;

import com.quest.etna.model.data.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer> {

    LinkedList<Comment> findAll();

    @Query("SELECT comment FROM Comment comment WHERE comment.user.id = :userId")
    LinkedList<Comment> findByUserId(@Param("userId") int id);
}
