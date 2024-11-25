package com.quest.etna.repositories;

import com.quest.etna.model.data.Lesson;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Integer> {

    LinkedList<Lesson> findAll();

    @Query("SELECT lesson FROM Lesson lesson WHERE lesson.author.id = :userId")
    LinkedList<Lesson> findByUserId(@Param("userId") int id);
}
