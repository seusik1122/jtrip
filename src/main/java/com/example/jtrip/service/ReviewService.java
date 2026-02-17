package com.example.jtrip.service;

import com.example.jtrip.dto.ReviewRequestDto;
import com.example.jtrip.entity.Place;
import com.example.jtrip.entity.Review;
import com.example.jtrip.repository.PlaceRepository;
import com.example.jtrip.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * 리뷰 등록 및 조회를 담당하는 비즈니스 로직 서비스
 * レビューの登録および照会を担当するビジネスロジックサービス
 *
 * <p>
 * 주요 기능:
 * 1. 리뷰 데이터 유효성 검사 및 저장
 * 2. Python AI 서버(FastAPI)와 REST API 통신을 통한 감성 분석 수행
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final RestTemplateBuilder restTemplateBuilder;

    /**
     * 리뷰를 저장하고, 외부 AI 서버에 감성 분석을 요청합니다.
     * レビューを保存し、外部AIサーバーへ感情分析をリクエストします。
     *
     * @param dto 리뷰 요청 데이터 (レビューリクエストデータ)
     */
    public void saveReview(ReviewRequestDto dto) {
        // AI 분석 실패 시 사용할 기본 점수 (Fail-safe)
        // AI分析失敗時に使用するデフォルトスコア (Fail-safe)
        Double score = 0.0;

        try {
            // 1. RestTemplate 설정 (타임아웃 30초)
            //    AI 모델의 추론 시간이 길어질 수 있으므로 충분한 대기 시간을 확보합니다.
            // 1. RestTemplate設定 (タイムアウト30秒)
            //    AIモデルの推論時間が長くなる可能性があるため、十分な待機時間を確保します。
            RestTemplate restTemplate = restTemplateBuilder
                    .setConnectTimeout(Duration.ofSeconds(30))
                    .setReadTimeout(Duration.ofSeconds(30))
                    .build();

            // AI 서버 엔드포인트 URL
            // AIサーバーのエンドポイントURL
            String aiUrl = "http://localhost:8000/analyze";

            // 요청 본문 생성
            // リクエスト本文の生成
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("content", dto.getContent());

            // 2. AI 서버로 POST 요청 전송
            // 2. AIサーバーへPOSTリクエストを送信
            System.out.println("INFO: Sending request to AI Server... " + dto.getContent());
            Map<String, Object> response = restTemplate.postForObject(aiUrl, requestBody, Map.class);

            // 3. 응답 파싱 및 점수 추출
            // 3. レスポンス解析およびスコア抽出
            if (response != null && response.get("score") != null) {
                // 데이터 타입 안전성 확보 (Integer/Double 모두 호환되도록 String 변환 후 파싱)
                // データタイプの安全性確保 (Integer/Double 両方互換するようにString変換後パース)
                String scoreStr = response.get("score").toString();
                score = Double.parseDouble(scoreStr);
                System.out.println("INFO: AI Analysis Result Score: " + score);
            }

        } catch (Exception e) {
            // 4. 예외 처리 (장애 격리)
            //    AI 서버와의 통신 중 오류가 발생하더라도, 리뷰 저장 자체는 성공해야 합니다.
            // 4. 例外処理 (障害分離)
            //    AIサーバーとの通信中にエラーが発生しても、レビュー保存自体は成功させる必要があります。
            System.err.println("WARN: AI Service is unavailable. Using default score. Error: " + e.getMessage());
        }

        // 5. 데이터베이스 저장
        // 5. データベースへ保存
        Place place = placeRepository.findById(dto.getPlaceId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Place ID: " + dto.getPlaceId()));

        Review review = new Review();
        review.setPlace(place);
        review.setContent(dto.getContent());
        review.setSentimentScore(score); // 분석된 점수 또는 기본값 (分析されたスコアまたはデフォルト値)

        reviewRepository.save(review);
    }

    /**
     * 리뷰 ID로 해당 리뷰를 삭제합니다.
     * レビューIDで該当レビューを削除します。
     *
     * @param reviewId 삭제할 리뷰 ID (削除するレビューID)
     */
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}