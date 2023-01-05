package com.example.buycation.posting.repository;

import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
}
