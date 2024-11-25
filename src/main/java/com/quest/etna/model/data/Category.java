package com.quest.etna.model.data;

import com.quest.etna.dto.CategoryDto;
import com.quest.etna.model.BaseModel;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "category")
@DynamicUpdate()
public class Category extends BaseModel {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Category(CategoryDto categoryDto) {
        this(categoryDto.getName(), categoryDto.getDescription());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
