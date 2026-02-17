package com.example.jtrip.controller;

import com.example.jtrip.dto.ReviewRequestDto;
import com.example.jtrip.repository.ReviewRepository;
import com.example.jtrip.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 리뷰 관련 HTTP 요청(등록, 삭제)을 처리하는 프레젠테이션 계층 컨트롤러
 * レビュー関連のHTTPリクエスト(登録、削除)を処理するプレゼンテーション層のコントローラー
 */
@Controller
@RequiredArgsConstructor
public class ReviewController {

    // 비즈니스 로직 처리를 위한 서비스 의존성 주입
    // ビジネスロジック処理のためのサービス依存性注入
    private final ReviewService reviewService;

    // (참고: 컨트롤러는 리포지토리보다 서비스를 통해 접근하는 것이 일반적입니다)
    // (参考: コントローラーはリポジトリよりサービスを通じてアクセスするのが一般的です)
    private final ReviewRepository reviewRepository;

    /**
     * 새로운 리뷰를 등록합니다.
     * 사용자가 입력한 데이터를 DTO로 매핑하여 서비스 계층으로 전달합니다.
     *
     * 新しいレビューを登録します。
     * ユーザーが入力したデータをDTOにマッピングし、サービス層へ伝達します。
     *
     * @param dto 폼 데이터가 바인딩된 객체 (フォームデータがバインドされたオブジェクト)
     * @return 메인 화면으로 리다이렉트 (メイン画面へリダイレクト)
     */
    @PostMapping("/review")
    public String createReview(ReviewRequestDto dto) {
        // 비즈니스 로직(저장 및 AI 분석) 위임
        // ビジネスロジック(保存およびAI分析)への委譲
        reviewService.saveReview(dto);

        // PRG(Post-Redirect-Get) 패턴을 사용하여 새로고침 시 중복 등록 방지
        // PRG(Post-Redirect-Get)パターンを使用し、リロード時の重複登録を防止
        return "redirect:/";
    }

    /**
     * 특정 리뷰를 삭제합니다.
     * 요청 파라미터로 전달받은 ID를 사용하여 삭제를 수행합니다.
     *
     * 特定のレビューを削除します。
     * リクエストパラメータとして受け取ったIDを使用し、削除を実行します。
     *
     * @param reviewId 삭제할 리뷰의 고유 ID (削除するレビューの固有ID)
     * @return 삭제 후 메인 화면으로 리다이렉트 (削除後、メイン画面へリダイレクト)
     */
    @PostMapping("/review/delete")
    public String deleteReview(@RequestParam Long reviewId) {
        // 서비스 계층에 삭제 요청
        // サービス層へ削除リクエスト
        reviewService.deleteReview(reviewId);

        return "redirect:/";
    }
}