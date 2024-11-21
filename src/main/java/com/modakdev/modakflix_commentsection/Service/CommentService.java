package com.modakdev.modakflix_commentsection.Service;


import com.modakdev.modakflix_commentsection.Collections.Comment;
import com.modakdev.modakflix_commentsection.Collections.Reply;
import com.modakdev.modakflix_commentsection.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }

    public Optional<Comment> getCommentById(String Id){
        return commentRepository.findById(Id);
    }

    public List<Comment> getCommentsByPostId(String postId){
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByUserId(String userId){
        return commentRepository.findByUserId(userId);
    }

    public Comment addComment(Comment comment){
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);

    }

    public Comment addReply (String parentId, Reply reply){
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        reply.setCreatedAt(LocalDateTime.now());
        parentComment.getReplies().add(reply);
        parentComment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(parentComment);

    }
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }
}
