package com.lbox.lbox_backend.domain.judgment.repository;

import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgmentRepository extends JpaRepository<Judgment, Long> ,JudgmentSearchRepository{
}
