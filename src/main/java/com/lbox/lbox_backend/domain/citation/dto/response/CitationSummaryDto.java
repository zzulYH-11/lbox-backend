package com.lbox.lbox_backend.domain.citation.dto.response;

import com.lbox.lbox_backend.domain.judgement.entity.Judgment;
import java.time.LocalDate;

public record CitationSummaryDto(
        Long id,
        String caseNumber,
        String caseName,
        String court,
        LocalDate judgmentDate,
        String headnote
) {
    public static CitationSummaryDto from(Judgment judgment) {
        return new CitationSummaryDto(
                judgment.getId(),
                judgment.getCaseNumber(),
                judgment.getCaseName(),
                judgment.getCourt(),
                judgment.getJudgmentDate(),
                judgment.getHeadnote()
        );
    }
}
