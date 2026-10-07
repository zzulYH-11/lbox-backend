package com.lbox.lbox_backend.domain.citation.entity;


import com.lbox.lbox_backend.domain.judgment.entity.Judgment;
import com.lbox.lbox_backend.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "citations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_citation_judgment",
                        columnNames = {"judgment_id", "cited_judgment_id"}
                )
        }
)
public class Citation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 인용하는 판례
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "judgment_id", nullable = false)
    private Judgment judgment;

    /**
     * 인용되는 판례
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cited_judgment_id", nullable = false)
    private Judgment citedJudgment;


    @Builder
    private Citation(
            Judgment judgment,
            Judgment citedJudgment
    ) {
        this.judgment = judgment;
        this.citedJudgment = citedJudgment;
    }
}