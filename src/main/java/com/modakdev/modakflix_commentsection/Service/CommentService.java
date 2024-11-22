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

    public Comment updateComment(String commentId, String newContent) {
        // Find the comment by its ID
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Update the content of the comment
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());

        // Save the updated comment back to the database
        return commentRepository.save(comment);
    }

    public Comment updateReply(String commentId, String replyId,String newContent){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        List<Reply> replies=comment.getReplies();

        if (!replies.isEmpty()){
            for(Reply reply: replies){
                reply.setContent(newContent);
                reply.setUpdatedAt(LocalDateTime.now());

                if (!reply.isUpdated()){
                    reply.setUpdated(true);
                }

                // Save the updated comment back to the database
                comment.setUpdatedAt(LocalDateTime.now());
                return commentRepository.save(comment);
            }
        }


        // set the new details
        comment.setContent(newContent);
        comment.setUpdatedAt(LocalDateTime.now());

        if (!comment.isUpdated()){
            comment.setUpdated(true);
        }
        return commentRepository.save(comment);
    }

    public String deleteComment(String id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return id;
        }
        return null;
    }

    public Comment deleteReply(String parentId, String replyId){
        // Find the comment by its ID
        Comment comment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        boolean removed = comment.getReplies().removeIf(reply -> reply.getId().equals(replyId));
        if (!removed) {
            throw new RuntimeException("Reply not found in the specified comment");
        }

        // Update the comment in the database
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);

    }
}
