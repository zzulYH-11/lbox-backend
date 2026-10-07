package com.lbox.lbox_backend.domain.citation.repository;

import com.lbox.lbox_backend.domain.citation.entity.Citation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitationRepository extends JpaRepository<Citation, Long> {
    
    // 대상 판례(A)가 인용하고 있는 다른 판례들(B) 조회 (A cites B)
    List<Citation> findByJudgmentId(Long judgmentId);
}
