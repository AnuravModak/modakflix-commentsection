package com.modakdev.modakflix_commentsection.Controller;

import com.modakdev.modakflix_commentsection.Collections.Comment;
import com.modakdev.modakflix_commentsection.Collections.Reply;
import com.modakdev.modakflix_commentsection.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService=commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Comment>> getCommentById(@PathVariable String id){
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentByPostId(@PathVariable String postId){
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Comment>> getCommentByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.addComment(comment));
    }

    // Add a reply to a comment
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Comment> addReply(@PathVariable String parentId, @RequestBody Reply reply) {
        return ResponseEntity.ok(commentService.addReply(parentId, reply));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
