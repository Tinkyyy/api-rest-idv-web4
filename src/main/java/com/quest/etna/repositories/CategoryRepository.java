package com.quest.etna.repositories;

import com.quest.etna.model.data.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

    LinkedList<Category> findAll();
}
