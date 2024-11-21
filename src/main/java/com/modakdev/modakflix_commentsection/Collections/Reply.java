package com.modakdev.modakflix_commentsection.Collections;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Reply {
    private String id;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private List<String> mentions;

    public Reply() {
        this.id= UUID.randomUUID().toString();
    }

    public Reply(String id, String userId, String content, LocalDateTime createdAt, List<String> mentions) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
        this.mentions = mentions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
