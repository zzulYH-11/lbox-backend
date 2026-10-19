package com.lbox.lbox_backend.domain.judgment.repository;

import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JudgmentRepository extends JpaRepository<Judgment, Long> {
}
