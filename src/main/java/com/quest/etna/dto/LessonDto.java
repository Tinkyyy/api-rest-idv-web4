package com.quest.etna.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quest.etna.model.data.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDto {

    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String title;
    @NotEmpty
    @NotNull
    private String thumbnail;

    private User author;
    @Min(1)
    @NotNull
    private int categoryId;
    @NotEmpty
    @NotNull
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
