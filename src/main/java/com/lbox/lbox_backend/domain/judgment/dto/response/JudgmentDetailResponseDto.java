package com.lbox.lbox_backend.domain.judgment.dto.response;

import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import java.time.LocalDate;

public record JudgmentDetailResponseDto(
        Long id,
        String caseNumber,
        String caseName,
        String court,
        LocalDate judgmentDate,
        String caseType,
        String instance,
        String resultType,
        String headnote,
        String summary,
        String referencedStatutes,
        String fullText,
        Long viewCount
) {
    public static JudgmentDetailResponseDto from(Judgment judgment) {
        return new JudgmentDetailResponseDto(
                judgment.getId(),
                judgment.getCaseNumber(),
                judgment.getCaseName(),
                judgment.getCourt(),
                judgment.getJudgmentDate(),
                judgment.getCaseType().name(),
                judgment.getInstance().name(),
                judgment.getResultType().getDescription(), // 한글 설명으로 반환
                judgment.getHeadnote(),
                judgment.getSummary(),
                judgment.getReferencedStatutes(),
                judgment.getFullText(),
                judgment.getViewCount()
        );
    }
}
