package com.lbox.lbox_backend.domain.judgement.repository;

import com.lbox.lbox_backend.domain.judgement.entity.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgmentRepository extends JpaRepository<Judgment, Long> {
}
