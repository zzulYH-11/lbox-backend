package com.lbox.lbox_backend.domain.citation.service;

import com.lbox.lbox_backend.domain.citation.dto.response.CitationSummaryDto;
import com.lbox.lbox_backend.domain.citation.dto.response.JudgmentCitationsResponseDto;
import com.lbox.lbox_backend.domain.citation.repository.CitationRepository;
import com.lbox.lbox_backend.domain.judgment.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitationService {

    private final CitationRepository citationRepository;
    private final JudgmentRepository judgmentRepository; // 존재 여부 검증용

    @Transactional(readOnly = true)
    public JudgmentCitationsResponseDto getCitations(Long judgmentId) {
        // 1. 대상 판례가 존재하는지 확인 (없으면 예외 처리)
        if (!judgmentRepository.existsById(judgmentId)) {
            throw new IllegalArgumentException("해당 판례를 찾을 수 없습니다.");
        }

        // 2. 대상 판례(A)가 인용하고 있는 판례들 (A cites B) -> citing
        List<CitationSummaryDto> citing = citationRepository.findByJudgmentId(judgmentId).stream()
                .map(citation -> CitationSummaryDto.from(citation.getCitedJudgment()))
                .toList();

        return new JudgmentCitationsResponseDto(citing);
    }
}
