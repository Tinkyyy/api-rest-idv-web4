package com.quest.etna.model.data;

import com.quest.etna.dto.CommentDto;
import com.quest.etna.model.BaseModel;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "comment")
@DynamicUpdate()
public class Comment extends BaseModel {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Lesson lesson;

    public Comment() {

    }

    public Comment(String content) {
        this.content = content;
    }

    public Comment(CommentDto commentDto) {
        this(commentDto.getContent());
    }

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

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }
}
