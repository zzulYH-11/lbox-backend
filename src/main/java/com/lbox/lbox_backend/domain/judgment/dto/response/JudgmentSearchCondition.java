package com.lbox.lbox_backend.domain.judgment.dto.response;

import com.lbox.lbox_backend.domain.judgment.enums.CaseType;

import java.time.LocalDate;

public record JudgmentSearchCondition(
        String keyword,
        CaseType caseType,
        String court,
        LocalDate dateFrom,
        LocalDate dateTo
) {
}