package com.lbox.lbox_backend.domain.judgment.controller;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentDetailResponseDto;
import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchCondition;
import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchPageResponse;
import com.lbox.lbox_backend.domain.judgment.enums.CaseType;
import com.lbox.lbox_backend.domain.judgment.service.JudgmentSearchService;
import com.lbox.lbox_backend.domain.judgment.service.JudgmentService;
import com.lbox.lbox_backend.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "Judgment", description = "판례 검색 및 상세 조회 API")
@RestController
@RequestMapping("/api/v1/judgments")
@RequiredArgsConstructor
public class JudgmentController {

    private final JudgmentService judgmentService;
    private final JudgmentSearchService judgmentSearchService;


    @Operation(
            summary = "판례 상세 조회",
            description = "판례 ID로 판례의 상세 정보를 조회합니다."
    )
    // TODO: PathVariable 타입 미스매치 (400) 및 존재하지 않는 ID 조회 (404) 예외 처리는 추후 추가
    @GetMapping("/{judgementId}")
    public ApiResponse<JudgmentDetailResponseDto> getJudgmentDetail(
            @PathVariable("judgementId") Long judgementId
    ) {
        return ApiResponse.onSuccess(
                "판례 상세 조회 성공",
                judgmentService.getJudgmentDetail(judgementId)
        );
    }


    @Operation(
            summary = "판례 검색",
            description = """
                제목 또는 본문에 검색어가 포함된 판례를 검색합니다.
                사건 유형, 법원, 선고일 범위로 필터링할 수 있습니다.
                날짜순 또는 조회수순 정렬과 페이징을 지원합니다.
                검색어는 필수이며 페이지 번호는 0부터 시작합니다.
                """
    )
    @GetMapping("/search")
    public ApiResponse<JudgmentSearchPageResponse> searchJudgments(
            // 누락·공백 검증은 Service에서 처리
            @RequestParam(name = "keyword", required = false)
            String keyword,

            @RequestParam(name = "caseType", required = false)
            CaseType caseType,

            @RequestParam(name = "court", required = false)
            String court,

            @RequestParam(name = "dateFrom", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateFrom,

            @RequestParam(name = "dateTo", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dateTo,

            @RequestParam(name = "sort", defaultValue = "date,desc")
            String sort,

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "20")
            int size
    ) {
        // 전달받은 검색 조건을 하나의 DTO로 묶는다.
        JudgmentSearchCondition condition = new JudgmentSearchCondition(
                keyword,
                caseType,
                court,
                dateFrom,
                dateTo
        );

        // 검색 결과와 페이지 정보를 가져온다.
        JudgmentSearchPageResponse response = judgmentSearchService.search(
                condition,
                page,
                size,
                sort
        );

        // 프로젝트의 공통 성공 응답 형식으로 감싼다.
        return ApiResponse.onSuccess(
                "판례 검색 성공",
                response
        );
    }


}
