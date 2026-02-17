package com.example.jtrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 사용자 리뷰 정보를 저장하는 JPA 엔티티 클래스
 * ユーザーレビュー情報を保存するJPAエンティティクラス
 *
 * <p>
 * 하나의 관광지(Place)에 여러 리뷰(Review)가 달릴 수 있으므로
 * 다대일(N:1) 연관관계를 맺고 있습니다.
 * 一つの観光地(Place)に複数のレビュー(Review)が投稿されるため、
 * 多対一(N:1)の関連関係を持っています。
 * </p>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "place") // 순환 참조 방지 (循環参照防止)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * N:1 연관관계 매핑 (어떤 관광지에 대한 리뷰인지)
     * N:1 関連マッピング (どの観光地に対するレビューか)
     *
     * @JoinColumn: 외래 키(FK) 이름을 'place_id'로 지정
     * @JoinColumn: 外部キー(FK)名を 'place_id' に指定
     */
    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위해 지연 로딩 권장 (性能最適化のため遅延ローディング推奨)
    @JoinColumn(name = "place_id")
    private Place place;

    /**
     * 리뷰 내용 (긴 글도 저장 가능하도록 TEXT 타입 지정)
     * レビュー内容 (長文も保存できるようTEXTタイプを指定)
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * AI(Gemini)가 분석한 감정 점수 (0.0 ~ 100.0)
     * AI(Gemini)が分析した感情スコア (0.0 ~ 100.0)
     */
    @Column
    private Double sentimentScore;

    /**
     * 리뷰 작성 시간
     * レビュー作成時間
     */
    @Column
    private LocalDateTime createdDate;

    /**
     * 엔티티가 영속성 컨텍스트에 저장되기 전(Persist) 실행되는 메서드
     * エンティティが永続性コンテキストに保存される前(Persist)に実行されるメソッド
     *
     * 별도의 설정 없이 자동으로 현재 시간을 기록하여 데이터 무결성을 보장합니다.
     * 別途の設定なしに自動で現在時刻を記録し、データ整合性を保証します。
     */
    @PrePersist
    public void prePersist() {
        this.createdDate = LocalDateTime.now();
    }
}