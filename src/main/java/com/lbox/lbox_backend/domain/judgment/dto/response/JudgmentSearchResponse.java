package com.lbox.lbox_backend.domain.judgment.dto.response;

import com.lbox.lbox_backend.domain.judgment.entity.Judgment;

import java.time.LocalDate;

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