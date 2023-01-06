package com.example.buycation.comment.repository;

import com.example.buycation.comment.entity.Comment;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostingOrderByCreatedAtDesc(Posting posting);
}
