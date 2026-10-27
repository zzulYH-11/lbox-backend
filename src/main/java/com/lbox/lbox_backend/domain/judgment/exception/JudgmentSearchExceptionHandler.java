package com.lbox.lbox_backend.domain.judgment.exception;

import com.lbox.lbox_backend.domain.judgment.controller.JudgmentController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(assignableTypes = JudgmentController.class)
public class JudgmentSearchExceptionHandler {

    /*
     * Service에서 직접 검증하여 발생시킨 오류.
     * 예: 빈 검색어, 날짜 역전, 잘못된 페이지 크기
     */
    @ExceptionHandler(JudgmentSearchException.class)
    public ResponseEntity<SearchErrorResponse> handleSearchException(
            JudgmentSearchException exception
    ) {
        return ResponseEntity.badRequest().body(
                new SearchErrorResponse(
                        exception.getCode(),
                        exception.getMessage()
                )
        );
    }

    /*
     * Controller의 파라미터 타입으로 변환하다 발생한 오류.
     *
     * 예:
     * caseType=FOO → CaseType으로 변환 불가
     * dateFrom=abc → LocalDate로 변환 불가
     * page=hello   → int로 변환 불가
     *
     * 이런 오류는 Service에 도달하기 전에 발생한다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<SearchErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception
    ) {
        SearchErrorResponse body = switch (exception.getName()) {
            case "caseType" -> new SearchErrorResponse(
                    "INVALID_CASE_TYPE",
                    "유효하지 않은 사건종류입니다."
            );

            case "dateFrom", "dateTo" -> new SearchErrorResponse(
                    "INVALID_DATE_FORMAT",
                    "날짜는 yyyy-MM-dd 형식의 유효한 날짜여야 합니다."
            );

            case "page" -> new SearchErrorResponse(
                    "INVALID_PAGE",
                    "page는 0 이상의 정수여야 합니다."
            );

            case "size" -> new SearchErrorResponse(
                    "INVALID_SIZE",
                    "size는 1 이상 100 이하의 정수여야 합니다."
            );

            default -> new SearchErrorResponse(
                    "INVALID_PARAMETER",
                    "요청 파라미터 형식이 올바르지 않습니다."
            );
        };

        return ResponseEntity.badRequest().body(body);
    }

    // 명세에서 약속한 오류 응답 구조
    public record SearchErrorResponse(
            String code,
            String message
    ) {
    }
}