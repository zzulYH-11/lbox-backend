package com.lbox.lbox_backend.domain.judgment.controller;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentDetailResponseDto;
import com.lbox.lbox_backend.domain.judgment.service.JudgmentService;
import com.lbox.lbox_backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/judgments")
@RequiredArgsConstructor
public class JudgmentController {

    private final JudgmentService judgmentService;

    // TODO: PathVariable 타입 미스매치 (400) 및 존재하지 않는 ID 조회 (404) 예외 처리는 추후 추가
    @GetMapping("/{judgementId}")
    public ApiResponse<JudgmentDetailResponseDto> getJudgmentDetail(@PathVariable("judgementId") Long judgementId) {
        return ApiResponse.onSuccess("판례 상세 조회 성공", judgmentService.getJudgmentDetail(judgementId));
    }
}
