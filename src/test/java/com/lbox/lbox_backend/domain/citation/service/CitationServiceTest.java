package com.lbox.lbox_backend.domain.citation.service;

import com.lbox.lbox_backend.domain.citation.dto.response.JudgmentCitationsResponseDto;
import com.lbox.lbox_backend.domain.citation.entity.Citation;
import com.lbox.lbox_backend.domain.citation.repository.CitationRepository;
import com.lbox.lbox_backend.domain.judgement.entity.Judgment;
import com.lbox.lbox_backend.domain.judgement.repository.JudgmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CitationServiceTest {

    @Mock
    private CitationRepository citationRepository;

    @Mock
    private JudgmentRepository judgmentRepository;

    @InjectMocks
    private CitationService citationService;

    @Test
    @DisplayName("인용한 판례가 있는 경우 올바르게 매핑된 DTO 목록을 반환한다")
    void 인용한_판례가_있는경우_목록을_반환한다() {
        // given
        given(judgmentRepository.existsById(1L)).willReturn(true);

        Judgment judgmentA = Judgment.builder().caseNumber("TargetCase").build();
        Judgment judgmentB = Judgment.builder().caseNumber("CitedCase1").build();
        Judgment judgmentC = Judgment.builder().caseNumber("CitedCase2").build();

        ReflectionTestUtils.setField(judgmentA, "id", 1L);
        ReflectionTestUtils.setField(judgmentB, "id", 2L);
        ReflectionTestUtils.setField(judgmentC, "id", 3L);

        Citation citation1 = Citation.builder().judgment(judgmentA).citedJudgment(judgmentB).build();
        Citation citation2 = Citation.builder().judgment(judgmentA).citedJudgment(judgmentC).build();

        given(citationRepository.findByJudgmentId(1L)).willReturn(List.of(citation1, citation2));

        // when
        JudgmentCitationsResponseDto result = citationService.getCitations(1L);

        // then
        assertThat(result.citing()).hasSize(2);
        assertThat(result.citing().get(0).caseNumber()).isEqualTo("CitedCase1");
        assertThat(result.citing().get(1).caseNumber()).isEqualTo("CitedCase2");
    }

    @Test
    @DisplayName("인용 관계가 없는 판례 조회 시 에러가 터지지 않고 빈 배열을 반환한다")
    void 인용관계가_없는_판례_조회시_빈_배열을_반환한다() {
        // given
        given(judgmentRepository.existsById(1L)).willReturn(true);
        given(citationRepository.findByJudgmentId(1L)).willReturn(Collections.emptyList());

        // when
        JudgmentCitationsResponseDto result = citationService.getCitations(1L);

        // then
        assertThat(result.citing()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 판례 ID로 조회 시 예외가 발생한다")
    void 존재하지_않는_판례ID로_조회시_예외가_발생한다() {
        // given
        given(judgmentRepository.existsById(anyLong())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> citationService.getCitations(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 판례를 찾을 수 없습니다.");
    }
}
