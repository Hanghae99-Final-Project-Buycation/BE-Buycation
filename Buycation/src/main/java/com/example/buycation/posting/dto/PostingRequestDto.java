package com.example.buycation.posting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostingRequestDto {
    private String title;
    private String category;
    private int totalMembers;
    private String address;
    private String dueDate;
    private long budget;
    private String image;
    private String content;
    private double coordsX;
    private double coordsY;
}
