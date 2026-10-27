package com.lbox.lbox_backend.domain.judgment.service;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchCondition;
import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchPageResponse;
import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchResponse;
import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import com.lbox.lbox_backend.domain.judgment.exception.JudgmentSearchException;
import com.lbox.lbox_backend.domain.judgment.repository.JudgmentRepository;
import com.lbox.lbox_backend.domain.judgment.repository.JudgmentSearchRepository;
import com.lbox.lbox_backend.domain.judgment.repository.JudgmentSearchRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JudgmentSearchService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int MAX_KEYWORD_LENGTH = 100;

    private static final int EXCERPT_LENGTH = 200;
    private static final int EXCERPT_BEFORE = 50;

    private final JudgmentRepository judgmentRepository;

    public JudgmentSearchPageResponse search(
            JudgmentSearchCondition condition,
            int page,
            int size,
            String sort
    ) {
        // 1. 검색어 공백 제거 및 검색 조건 검증
        JudgmentSearchCondition normalized = normalize(condition);

        // 2. 페이지 검증 및 API 정렬 이름을 엔티티 필드명으로 변환
        Pageable pageable = createPageable(page, size, sort);

        // 3. 현재 페이지의 판결문과 전체 건수 조회
        Page<Judgment> judgments = judgmentRepository.search(
                normalized,
                pageable
        );


        Page<JudgmentSearchResponse> response = judgments.map(
                judgment -> JudgmentSearchResponse.from(
                        judgment,
                        createExcerpt(judgment, normalized.keyword())
                )
        );

        // 5. 명세의 content + page 구조로 반환
        return JudgmentSearchPageResponse.from(response);
    }

    private JudgmentSearchCondition normalize(
            JudgmentSearchCondition condition
    ) {
        String keyword = condition.keyword();

        if (keyword == null || keyword.isBlank()) {
            throw new JudgmentSearchException(
                    "MISSING_KEYWORD",
                    "검색어(keyword)는 필수입니다."
            );
        }

        keyword = keyword.strip();

        // 초기 정책: 검색어는 최대 100자
        if (keyword.length() > MAX_KEYWORD_LENGTH) {
            throw new JudgmentSearchException(
                    "INVALID_KEYWORD",
                    "검색어는 100자 이하여야 합니다."
            );
        }

        if (condition.dateFrom() != null
                && condition.dateTo() != null
                && condition.dateFrom().isAfter(condition.dateTo())) {
            throw new JudgmentSearchException(
                    "INVALID_DATE_RANGE",
                    "시작일이 종료일보다 늦을 수 없습니다."
            );
        }

        // 법원이 빈 문자열이면 필터를 적용하지 않는다.
        String court = condition.court();

        if (court != null) {
            court = court.strip();

            if (court.isEmpty()) {
                court = null;
            }
        }

        return new JudgmentSearchCondition(
                keyword,
                condition.caseType(),
                court,
                condition.dateFrom(),
                condition.dateTo()
        );
    }

    private Pageable createPageable(int page, int size, String sort) {
        if (page < 0) {
            throw new JudgmentSearchException(
                    "INVALID_PAGE",
                    "page는 0 이상이어야 합니다."
            );
        }

        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new JudgmentSearchException(
                    "INVALID_SIZE",
                    "size는 1 이상 100 이하여야 합니다."
            );
        }

        // 이 API는 "필드,방향" 형태의 정렬 하나만 받는다.
        // 예: date,desc / viewCount,desc
        if (sort == null) {
            sort = "date,desc";
        }

        String[] parts = sort.split(",", -1);

        if (parts.length != 2) {
            throw new JudgmentSearchException(
                    "INVALID_SORT",
                    "정렬은 date,desc 또는 viewCount,desc 형식이어야 합니다."
            );
        }

        String property = switch (parts[0].strip()) {
            case "date", "judgmentDate" -> "judgmentDate";
            case "viewCount" -> "viewCount";
            default -> throw new JudgmentSearchException(
                    "INVALID_SORT",
                    "정렬 필드는 date 또는 viewCount만 가능합니다."
            );
        };

        Sort.Direction direction = switch (
                parts[1].strip().toLowerCase(Locale.ROOT)
                ) {
            case "asc" -> Sort.Direction.ASC;
            case "desc" -> Sort.Direction.DESC;
            default -> throw new JudgmentSearchException(
                    "INVALID_SORT",
                    "정렬 방향은 asc 또는 desc여야 합니다."
            );
        };


        Sort resolvedSort = Sort.by(direction, property)
                .and(Sort.by(Sort.Direction.DESC, "id"));

        return PageRequest.of(page, size, resolvedSort);
    }

    private String createExcerpt(Judgment judgment, String keyword) {
        String fullText = judgment.getFullText();

        if (fullText != null && !fullText.isBlank()) {
            // 본문에서 검색어가 처음 나타나는 위치
            int index = fullText.indexOf(keyword);

            if (index >= 0) {
                // 검색어 앞쪽 문맥도 보여주기 위해 최대 50자 앞부터 발췌
                int start = Math.max(0, index - EXCERPT_BEFORE);

                return cutExcerpt(fullText, start);
            }
        }

        /*
         * 제목만 검색어에 일치 or
         * DB의 문자열 비교 규칙과 Java의 정확한 문자열 비교가
         * 달라 본문 위치를 찾지 못한 경우 요약으로 대체함
         */
        String summary = judgment.getSummary();

        if (summary != null && !summary.isBlank()) {
            return cutExcerpt(summary, 0);
        }

        if (fullText != null && !fullText.isBlank()) {
            return cutExcerpt(fullText, 0);
        }

        return "";
    }

    private String cutExcerpt(String text, int requestedStart) {
        int start = requestedStart;

        // 이모지처럼 UTF-16 두 칸을 쓰는 문자 중간에서 자르지 않도록 보정
        if (start > 0
                && Character.isLowSurrogate(text.charAt(start))
                && Character.isHighSurrogate(text.charAt(start - 1))) {
            start--;
        }

        int end = Math.min(text.length(), start + EXCERPT_LENGTH);

        if (end < text.length()
                && Character.isHighSurrogate(text.charAt(end - 1))
                && Character.isLowSurrogate(text.charAt(end))) {
            end--;
        }

        String excerpt = text.substring(start, end)
                .replaceAll("\\s+", " ")
                .strip();

        // 원문 일부가 생략됐다는 표시
        String prefix = start > 0 ? "…" : "";
        String suffix = end < text.length() ? "…" : "";

        return prefix + excerpt + suffix;
    }
}