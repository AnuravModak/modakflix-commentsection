package com.modakdev.modakflix_commentsection.Repository;

import com.modakdev.modakflix_commentsection.Collections.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository  extends MongoRepository<Comment, String> {
    // Fetch all comments for a specific post
    List<Comment> findByPostId(String postId);

    // Fetch all comments made by a specific user
    List<Comment> findByUserId(String userId);


}
