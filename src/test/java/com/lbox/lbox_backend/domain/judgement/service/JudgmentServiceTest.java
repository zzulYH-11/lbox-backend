package com.lbox.lbox_backend.domain.judgement.service;

import com.lbox.lbox_backend.domain.judgement.dto.response.JudgmentDetailResponseDto;
import com.lbox.lbox_backend.domain.judgement.entity.Judgment;
import com.lbox.lbox_backend.domain.judgement.enums.CaseType;
import com.lbox.lbox_backend.domain.judgement.enums.InstanceType;
import com.lbox.lbox_backend.domain.judgement.enums.ResultType;
import com.lbox.lbox_backend.domain.judgement.repository.JudgmentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JudgmentServiceTest {

    @Mock
    private JudgmentRepository judgmentRepository;

    @InjectMocks
    private JudgmentService judgmentService;

    @Test
    @DisplayName("존재하는 판례 ID로 조회하면 판례 상세 정보를 성공적으로 반환하고 조회수를 1 증가시킨다")
    void 판례_상세_정보_조회에_성공한다() {
        // given
        Judgment judgment = Judgment.builder()
                .caseNumber("2022가단5065289")
                .caseName("층간소음으로인한위자료청구의건")
                .court("서울중앙지방법원")
                .judgmentDate(LocalDate.of(2023, 4, 13))
                .caseType(CaseType.CIVIL)
                .instance(InstanceType.FIRST)
                .headnote("판결요지")
                .summary("요약")
                .fullText("주문\n피고는 원고에게...")
                .referencedStatutes("민법 제750조")
                .resultType(ResultType.PARTIAL_WIN)
                .claimPurpose("청구취지")
                .build();

        // 엔티티 ID 세팅 (Setter가 없으므로 ReflectionTestUtils 활용)
        ReflectionTestUtils.setField(judgment, "id", 1L);

        // Mock 객체 동작 정의
        given(judgmentRepository.findById(1L)).willReturn(Optional.of(judgment));

        // when
        JudgmentDetailResponseDto result = judgmentService.getJudgmentDetail(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.caseNumber()).isEqualTo("2022가단5065289");
        assertThat(result.resultType()).isEqualTo("원고일부승"); // Enum -> 한글 설명 변환 확인
        assertThat(result.viewCount()).isEqualTo(1L); // 조회수 1 증가 확인
        assertThat(judgment.getViewCount()).isEqualTo(1L); // 엔티티 자체의 값도 변경되었는지 확인
    }

    @Test
    @DisplayName("존재하지 않는 판례 ID로 조회하면 예외가 발생한다")
    void 존재하지_않는_판례_조회시_예외가_발생한다() {
        // given
        given(judgmentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> judgmentService.getJudgmentDetail(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 판례를 찾을 수 없습니다.");
    }
}
