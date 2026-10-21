package com.lbox.lbox_backend.domain.judgment.repository;

import com.lbox.lbox_backend.domain.judgment.dto.response.JudgmentSearchCondition;
import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JudgmentSearchRepository {

    Page<Judgment> search(
            JudgmentSearchCondition condition,
            Pageable pageable
    );
}
