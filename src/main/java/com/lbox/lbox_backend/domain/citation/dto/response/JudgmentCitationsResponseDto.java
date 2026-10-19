package com.lbox.lbox_backend.domain.citation.dto.response;

import java.util.List;

public record JudgmentCitationsResponseDto(
        List<CitationSummaryDto> citing
) {
}
