package com.example.buycation.posting.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainPostingResponseDto {
    private Long postingId;
    private String title;
    private String image;
    private String dueDate;
    private long perBudget;
    private int totalMembers;
    private int currentMembers;
    private String address;
    private String category;
    private boolean doneStatus;
}
