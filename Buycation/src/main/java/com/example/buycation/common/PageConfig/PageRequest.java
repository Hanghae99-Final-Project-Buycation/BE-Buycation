package com.example.buycation.common.PageConfig;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageRequest {

    Long key; //마지막에 읽었던 키 값, 기준 컬럼에 중복값이 없어야 함

    public static final Long NONE_KEY = -1L; //마지막까지 간다면 -1을 반환하여 더이상 데이터가 없음을 알림

    public PageRequest(Long key) {
        this.key = key;
    }

    public Boolean hasKey(Long key){
        return key != null;
    }

    public PageRequest next(Long key){
        return new PageRequest(key);
    }
}
