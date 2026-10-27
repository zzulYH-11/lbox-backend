package com.lbox.lbox_backend.domain.judgment.dto.response;

import com.lbox.lbox_backend.domain.judgment.entity.Judgment;

import java.time.LocalDate;
/*
 * 검색 결과 한 건을 표현하는 DTO.
 *
 * 전체 본문(fullText)은 응답하지 않고,
 * Service에서 만든 미리보기(excerpt)만 전달한다.
 */
public record JudgmentSearchResponse(
        Long id,
        String caseNumber,
        String caseName,
        String court,
        LocalDate judgmentDate,
        String resultType,
        String claimPurpose,
        String excerpt,
        Long viewCount
) {

    public static JudgmentSearchResponse from(
            Judgment judgment,
            String excerpt
    ) {
        return new JudgmentSearchResponse(
                judgment.getId(),
                judgment.getCaseNumber(),
                judgment.getCaseName(),
                judgment.getCourt(),
                judgment.getJudgmentDate(),
                judgment.getResultType().getDescription(),
                judgment.getClaimPurpose(),
                excerpt,
                judgment.getViewCount()
        );
    }
}