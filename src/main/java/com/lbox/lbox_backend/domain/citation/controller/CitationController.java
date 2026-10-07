package com.lbox.lbox_backend.domain.citation.controller;

import com.lbox.lbox_backend.domain.citation.dto.response.JudgmentCitationsResponseDto;
import com.lbox.lbox_backend.domain.citation.service.CitationService;
import com.lbox.lbox_backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/judgments")
@RequiredArgsConstructor
public class CitationController {

    private final CitationService citationService;

    // TODO: 예외 처리는 추후 공통 예외 처리 도입 시 추가
    @GetMapping("/{judgmentId}/citations")
    public ApiResponse<JudgmentCitationsResponseDto> getCitations(@PathVariable("judgmentId") Long judgmentId) {
        return ApiResponse.onSuccess("인용 판례 목록 조회 성공", citationService.getCitations(judgmentId));
    }
}
