package com.example.jtrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 관광지 정보를 담는 JPA 엔티티 클래스
 * 観光地情報を格納するJPAエンティティクラス
 *
 * <p>
 * 데이터베이스의 'place' 테이블과 매핑되며, 관광지 이름, 설명,
 * 그리고 해당 관광지에 달린 리뷰 목록을 관리합니다.
 * データベースの 'place' テーブルとマッピングされ、観光地名、説明、
 * そして該当観光地に投稿されたレビューリストを管理します。
 * </p>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = "reviews") // 순환 참조 방지를 위해 연관관계 필드는 제외 (循環参照防止のため、関連フィールドは除外)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 긴 텍스트 저장을 위해 컬럼 정의 수정
    // 長文テキスト保存のためにカラム定義を修正
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 1:N 연관관계 매핑 (관광지 하나에 여러 개의 리뷰)
     * 1:N 関連マッピング (一つの観光地に複数のレビュー)
     *
     * - fetch = EAGER: 관광지 조회 시 리뷰도 즉시 로딩 (메인 화면 노출용)
     * (観光地照会時、レビューも即時ローディング - メイン画面表示用)
     * - cascade = REMOVE: 관광지 삭제 시 관련 리뷰도 함께 삭제 (무결성 유지)
     * (観光地削除時、関連レビューも一緒に削除 - 整合性維持)
     * - OrderBy: 최신 리뷰가 먼저 나오도록 ID 역순 정렬
     * (最新レビューが先に出るようにID逆順ソート)
     */
    @OneToMany(mappedBy = "place", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("id DESC")
    private List<Review> reviews = new ArrayList<>();

    /**
     * 해당 관광지의 리뷰 평점을 계산하여 반환하는 도메인 로직
     * 該当観光地のレビュー評点を計算して返却するドメインロジック
     *
     * View(Mustache)에서 {{averageScore}} 형태로 직접 호출하여 사용합니다.
     * View(Mustache)にて {{averageScore}} の形式で直接呼び出して使用します。
     *
     * @return 소수점 한 자리까지 포맷팅된 평균 점수 문자열 (小数点第1位までフォーマットされた平均点文字列)
     */
    public String getAverageScore() {
        if (reviews == null || reviews.isEmpty()) {
            return "0.0"; // 리뷰가 없을 경우 기본값 (レビューがない場合のデフォルト値)
        }

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getSentimentScore(); // 모든 리뷰의 감정 점수 합산 (全レビューの感情スコア合算)
        }

        double avg = sum / reviews.size();
        return String.format("%.1f", avg); // 예: 85.3 (例: 85.3)
    }
}