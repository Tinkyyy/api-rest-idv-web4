package com.quest.etna.model.data;

import com.quest.etna.dto.LessonDto;
import com.quest.etna.model.BaseModel;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
@DynamicUpdate()
public class Lesson extends BaseModel {

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String thumbnail;

    @OneToOne
    @JoinColumn(nullable = false)
    private User author;

    @OneToOne
    @JoinColumn(nullable = false)
    private Category category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public Lesson() {}

    public Lesson(String name, String title, String thumbnail, String content) {
        this.name = name;
        this.title = title;
        this.thumbnail = thumbnail;
        this.content = content;
    }

    public Lesson(LessonDto lessonDto) {
        this(lessonDto.getName(), lessonDto.getTitle(), lessonDto.getThumbnail(), lessonDto.getContent());
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
