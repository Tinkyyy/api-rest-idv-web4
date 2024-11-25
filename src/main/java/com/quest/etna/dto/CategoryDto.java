package com.quest.etna.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategoryDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String description;


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
