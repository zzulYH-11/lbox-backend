package com.lbox.lbox_backend.domain.judgment.repository;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchCondition;
import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.lbox.lbox_backend.domain.judgment.entity.QJudgment.judgment;

@Repository
@RequiredArgsConstructor
public class JudgmentSearchRepositoryImpl
        implements JudgmentSearchRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Judgment> search(
            JudgmentSearchCondition condition,
            Pageable pageable
    ) {
        // 목록 조회와 전체 건수 조회에 동일한 검색 조건을 사용한다.
        BooleanBuilder predicate = createPredicate(condition);

        /*
         * 1. 현재 페이지의 데이터만 조회하고,
         * 검색·정렬·페이징은 모두 DB에서 수행한다.
         */
        List<Judgment> content = queryFactory
                .selectFrom(judgment)
                .where(predicate)
                .orderBy(createOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /*
         * 2. 전체 검색 결과 수 조회
         *
         * 현재 페이지의 개수가 아닌, 조건에 일치하는 전체 개수다.
         * COUNT에는 정렬·offset·limit을 적용하지 않는다.
         */
        Long total = queryFactory
                .select(judgment.id.count())
                .from(judgment)
                .where(predicate)
                .fetchOne();

        // 조회한 목록과 전체 건수를 Spring의 Page로 묶는다.
        return new PageImpl<>(
                content,
                pageable,
                total == null ? 0L : total
        );
    }

    private BooleanBuilder createPredicate(
            JudgmentSearchCondition condition
    ) {
        BooleanBuilder builder = new BooleanBuilder();


        String pattern = "%" + escapeLike(condition.keyword()) + "%";

        // 괄호 안에서 제목 OR 본문을 먼저 묶는다.
        builder.and(
                judgment.caseName.like(pattern, '!')
                        .or(judgment.fullText.like(pattern, '!'))
        );

        // 선택 필터는 전달됐을 때만 AND로 추가한다.
        if (condition.caseType() != null) {
            builder.and(
                    judgment.caseType.eq(condition.caseType())
            );
        }

        if (condition.court() != null) {
            builder.and(
                    judgment.court.eq(condition.court())
            );
        }

        // goe: greater or equal → 시작일 이상
        if (condition.dateFrom() != null) {
            builder.and(
                    judgment.judgmentDate.goe(condition.dateFrom())
            );
        }

        // loe: less or equal → 종료일 이하
        if (condition.dateTo() != null) {
            builder.and(
                    judgment.judgmentDate.loe(condition.dateTo())
            );
        }

        return builder;
    }

    private String escapeLike(String value) {
        // escape 문자 자체를 먼저 처리해야 한다.
        return value
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }

    private OrderSpecifier<?>[] createOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        /*
         * Service에서 검증하고 변환한 정렬 조건을
         * QueryDSL의 정렬 표현식으로 바꾼다.
         */
        for (Sort.Order order : sort) {
            boolean ascending = order.isAscending();

            switch (order.getProperty()) {
                case "judgmentDate" -> orders.add(
                        ascending
                                ? judgment.judgmentDate.asc()
                                : judgment.judgmentDate.desc()
                );

                case "viewCount" -> orders.add(
                        ascending
                                ? judgment.viewCount.asc()
                                : judgment.viewCount.desc()
                );

                case "id" -> orders.add(
                        ascending
                                ? judgment.id.asc()
                                : judgment.id.desc()
                );

                // 내부 코드에서 허용되지 않은 정렬이 전달된 경우
                default -> throw new IllegalArgumentException(
                        "지원하지 않는 정렬 필드: " + order.getProperty()
                );
            }
        }

        return orders.toArray(new OrderSpecifier<?>[0]);
    }
}