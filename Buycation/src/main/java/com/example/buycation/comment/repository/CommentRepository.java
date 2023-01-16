package com.example.buycation.comment.repository;

import com.example.buycation.comment.entity.Comment;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostingOrderByCreatedAtDesc(Posting posting);

    List<Comment> findAllByPosting(Posting posting);

    @Modifying
    @Query("delete from Comment c where c in :comments")
    void deleteAllByInQuery(@Param("comments") List<Comment> comments);
}
