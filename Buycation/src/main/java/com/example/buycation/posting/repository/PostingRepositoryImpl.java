package com.example.buycation.posting.repository;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.posting.entity.Posting;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.WRONG_CATEGORY_ERROR;
import static com.example.buycation.common.exception.ErrorCode.WRONG_SORT_ERROR;
import static com.example.buycation.posting.entity.QPosting.posting;

@Repository
public class PostingRepositoryImpl implements PostingRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public PostingRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Posting> findAllByQuerydsl(String category, String search, String sort) {
        // "%", "_" 가 SQL에서 LIKE의 속성으로 인식 됨으로 escape 처리를 하기 위한 코드
        if (search.contains("%") || search.contains("_")) {
            search = search.replace("%", "!%");
            search = search.replace("_", "!_");
        }
        OrderSpecifier<?> sortCheck = switch (sort) {
            case "금액 순" -> sortCheck = posting.perBudget.asc();
            case "인원 순" -> sortCheck = posting.totalMembers.asc();
            case "기한 순" -> sortCheck = posting.dueDate.asc();
            case "최신 순", "" -> sortCheck = posting.createdAt.desc();
            default -> throw new CustomException(WRONG_SORT_ERROR);
        };
        String categoryCheck = switch (category) {
            case "전체", "" -> "";
            case "음식" -> "음식";
            case "물건" -> "물건";
            default -> throw new CustomException(WRONG_CATEGORY_ERROR);
        };
        return jpaQueryFactory
                .selectFrom(posting)
                .where(
                        posting.title.contains(search)
                                .or(posting.address.contains(search)),
                        posting.category.contains(categoryCheck),
                        posting.doneStatus.eq(false)
                )
                .orderBy(sortCheck)
                .fetch();
    }
}
