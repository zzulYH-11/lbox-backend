package com.lbox.lbox_backend.domain.judgment.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;


 // 목록과 페이지 정보를 묶어서 응답함
public record JudgmentSearchPageResponse(
        List<JudgmentSearchResponse> content,
        PageInfo page
) {

    public static JudgmentSearchPageResponse from(
            Page<JudgmentSearchResponse> result
    ) {
        return new JudgmentSearchPageResponse(
                result.getContent(),
                new PageInfo(
                        result.getNumber(),
                        result.getSize(),
                        result.getTotalElements(),
                        result.getTotalPages()
                )
        );
    }

    // 이 응답 안에서만 사용하는 페이지 정보 DTO
    public record PageInfo(
            int number,
            int size,
            long totalElements,
            int totalPages
    ) {
    }
}