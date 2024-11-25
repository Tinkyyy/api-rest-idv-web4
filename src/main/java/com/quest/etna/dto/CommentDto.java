package com.quest.etna.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quest.etna.model.data.Lesson;
import com.quest.etna.model.data.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    @NotEmpty
    @NotNull
    private String content;

    private User user;
    @NotNull
    @Min(1)
    private int lessonId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int id) {
        this.lessonId = id;
    }
}
