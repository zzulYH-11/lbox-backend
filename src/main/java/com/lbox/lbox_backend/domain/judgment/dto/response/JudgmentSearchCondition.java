package com.lbox.lbox_backend.domain.judgment.dto.response;

import com.lbox.lbox_backend.domain.judgment.enums.CaseType;

import java.time.LocalDate;

//검색 조건을 묶는 객체

/*
 * 사용자가 전달한 검색 조건을 묶어서 전달하는 DTO.
 *
 * keyword: 필수 검색어
 * 나머지: 선택 필터. null이면 해당 필터를 적용하지 않는다.
 *
 * 페이지 번호와 정렬은 Pageable로 별도 전달한다.
 */
public record JudgmentSearchCondition(
        String keyword,
        CaseType caseType,
        String court,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}