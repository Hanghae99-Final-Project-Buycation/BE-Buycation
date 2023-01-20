package com.example.buycation.common.PageConfig;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse<T> {
    PageRequest nextPageRequest;
    List<T> dataList;

    public PageResponse(PageRequest nextPageRequest, List<T> dataList) {
        this.nextPageRequest = nextPageRequest;
        this.dataList = dataList;
    }
}
