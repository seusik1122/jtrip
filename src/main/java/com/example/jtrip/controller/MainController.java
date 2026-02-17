package com.example.jtrip.controller;

import com.example.jtrip.entity.Place;
import com.example.jtrip.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 애플리케이션의 메인 화면 렌더링을 담당하는 컨트롤러
 * アプリケーションのメイン画面レンダリングを担当するコントローラー
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    // 생성자 주입을 통해 리포지토리 의존성 주입 (Lombok 활용)
    // コンストラクタ注入を通じてリポジトリの依存性を注入 (Lombok活用)
    private final PlaceRepository placeRepository;

    /**
     * 메인 페이지(Root URL) 접근 시 호출됩니다.
     * DB에 저장된 모든 관광지 목록을 조회하여 뷰(View)에 전달합니다.
     *
     * メインページ(Root URL)へのアクセス時に呼び出されます。
     * DBに保存されたすべての観光地リストを照会し、ビュー(View)に渡します。
     *
     * @param model 뷰에 데이터를 전달하기 위한 스프링 모델 객체 (ビューにデータを渡すためのSpringモデルオブジェクト)
     * @return 렌더링할 템플릿 파일명 (レンダリングするテンプレートファイル名)
     */
    @GetMapping("/")
    public String index(Model model) {
        // 1. 모든 관광지 데이터 조회 (SELECT * FROM place)
        // 1. すべての観光地データを照会 (SELECT * FROM place)
        List<Place> places = placeRepository.findAll();

        // 2. 조회된 데이터를 모델에 속성으로 추가
        // 2. 照会されたデータをモデルに属性として追加
        model.addAttribute("places", places);

        // 3. place-list.mustache 템플릿 반환
        // 3. place-list.mustache テンプレートを返却
        return "place-list";
    }
}