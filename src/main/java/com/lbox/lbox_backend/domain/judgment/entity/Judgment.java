package com.lbox.lbox_backend.domain.judgment.entity;




import com.lbox.lbox_backend.domain.judgment.enums.CaseType;
import com.lbox.lbox_backend.domain.judgment.enums.InstanceType;
import com.lbox.lbox_backend.domain.judgment.enums.ResultType;
import com.lbox.lbox_backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "judgments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_judgment_case_number",
                        columnNames = "case_number"
                )
        }
)
public class Judgment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "judgment_id")
    private Long id;

    @Column(name = "case_number", nullable = false, length = 50)
    private String caseNumber;

    @Column(name = "case_name", nullable = false, length = 255)
    private String caseName;

    @Column(name = "court", nullable = false, length = 100)
    private String court;

    @Column(name = "judgment_date", nullable = false)
    private LocalDate judgmentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_type", nullable = false, length = 20)
    private CaseType caseType;

    @Enumerated(EnumType.STRING)
    @Column(name = "instance", nullable = false, length = 20)
    private InstanceType instance;

    @Column(name = "headnote", columnDefinition = "TEXT")
    private String headnote;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "full_text", nullable = false, columnDefinition = "LONGTEXT")
    private String fullText;

    @Column(name = "referenced_statutes", columnDefinition = "TEXT")
    private String referencedStatutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false, length = 30)
    private ResultType resultType;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "claim_purpose", columnDefinition = "TEXT")
    private String claimPurpose;


    @Builder
    private Judgment(
            String caseNumber,
            String caseName,
            String court,
            LocalDate judgmentDate,
            CaseType caseType,
            InstanceType instance,
            String headnote,
            String summary,
            String fullText,
            String referencedStatutes,
            ResultType resultType,
            String claimPurpose
    ) {
        this.caseNumber = caseNumber;
        this.caseName = caseName;
        this.court = court;
        this.judgmentDate = judgmentDate;
        this.caseType = caseType;
        this.instance = instance;
        this.headnote = headnote;
        this.summary = summary;
        this.fullText = fullText;
        this.referencedStatutes = referencedStatutes;
        this.resultType = resultType;
        this.claimPurpose = claimPurpose;
        this.viewCount = 0L;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void update(
            String caseName,
            String court,
            LocalDate judgmentDate,
            CaseType caseType,
            InstanceType instance,
            String headnote,
            String summary,
            String fullText,
            String referencedStatutes,
            ResultType resultType,
            String claimPurpose
    ) {
        this.caseName = caseName;
        this.court = court;
        this.judgmentDate = judgmentDate;
        this.caseType = caseType;
        this.instance = instance;
        this.headnote = headnote;
        this.summary = summary;
        this.fullText = fullText;
        this.referencedStatutes = referencedStatutes;
        this.resultType = resultType;
        this.claimPurpose = claimPurpose;
    }
}