package com.lbox.lbox_backend.domain.judgment.repository;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchCondition;
import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface JudgmentSearchRepository {

    // 판례 검색 조건과 페이징 정보를 받으면 => 원래 판례 검색 결과 반환 ( 명세 구현 )
    Page<Judgment> search(
            JudgmentSearchCondition condition,
            Pageable pageable
    );
}
