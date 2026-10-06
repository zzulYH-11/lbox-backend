package com.lbox.lbox_backend.domain.judgement.service;

import com.lbox.lbox_backend.domain.judgement.dto.response.JudgmentDetailResponseDto;
import com.lbox.lbox_backend.domain.judgement.entity.Judgment;
import com.lbox.lbox_backend.domain.judgement.repository.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JudgmentService {

    private final JudgmentRepository judgmentRepository;

    @Transactional
    public JudgmentDetailResponseDto getJudgmentDetail(Long judgmentId) {
        // TODO: 추후 공통 예외 처리 도입 시 커스텀 예외로 변경 예정
        Judgment judgment = judgmentRepository.findById(judgmentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 판례를 찾을 수 없습니다."));

        // 조회수 증가 (Dirty Checking을 통해 DB 반영)
        judgment.increaseViewCount();

        return JudgmentDetailResponseDto.from(judgment);
    }
}
