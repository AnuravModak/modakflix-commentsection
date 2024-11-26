package com.modakdev.modakflix_commentsection.Service;


import com.modakdev.modakflix_commentsection.Collections.Comment;
import com.modakdev.modakflix_commentsection.Collections.Reply;
import com.modakdev.modakflix_commentsection.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }
    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(String Id){
        return commentRepository.findById(Id);
    }

    public List<Comment> getCommentsByPostId(String postId){
        List<Comment> parentComment=commentRepository.findByPostId(postId);

        if (parentComment == null){
            return new ArrayList<>();
        }

        if (parentComment.isEmpty()){
            return parentComment;
        }


        return parentComment.stream().sorted((r1,r2)->r2.getCreatedAt().compareTo(r1.getCreatedAt())).toList();
    }

    public List<Comment> getCommentsByUserId(String userId){
        return commentRepository.findByUserId(userId);
    }

    public Optional<List<Reply>> getRepliesByCommentId(String commentId) {
        // Fetch the comment by ID
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        // Check if replies are null, and return an empty list instead
        if (parentComment.getReplies() == null) {
            return Optional.of(new ArrayList<>()); // Return an empty list wrapped in an Optional
        }

        // Otherwise, return the replies wrapped in an Optional
        List<Reply> sortedReplies = parentComment.getReplies().stream()
                .sorted((r1,r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt())).toList();
        return Optional.of(sortedReplies);
    }


    public Comment addComment(Comment comment){
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        return commentRepository.save(comment);

    }

    public Comment addReply(String parentId, Reply reply) {
        // Find the parent comment or throw an exception if not found
        Comment parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        // Set createdAt if it's missing or null
        if (reply.getCreatedAt() == null) {
            reply.setCreatedAt(LocalDateTime.now());
        }

        // Set updatedAt if it's missing or null
        if (reply.getUpdatedAt() == null) {
            reply.setUpdatedAt(LocalDateTime.now());
        }

        // Initialize the replies list if it is null
        if (parentComment.getReplies() == null) {
            parentComment.setReplies(new ArrayList<>());
        }

        // Add the reply to the parent's list of replies
        parentComment.getReplies().add(reply);

        // Update the parent's updatedAt field
        parentComment.setUpdatedAt(LocalDateTime.now());

        // Save and return the updated parent comment
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
