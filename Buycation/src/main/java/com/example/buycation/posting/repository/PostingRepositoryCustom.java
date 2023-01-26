package com.example.buycation.posting.repository;

import com.example.buycation.posting.entity.Posting;

import java.util.List;

public interface PostingRepositoryCustom {

    List<Posting> findAllByQuerydsl(String category, String search, String sort);
}
