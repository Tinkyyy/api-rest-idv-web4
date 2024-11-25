package com.quest.etna.repositories;

import com.quest.etna.model.data.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    LinkedList<User> findAll();
    User findByUsername(String username);
    Optional<User> findById(int id);
}
