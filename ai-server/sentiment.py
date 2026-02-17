import google.generativeai as genai
import re

# ==========================================
# [Security Notice]
# 실제 배포 시 API 키는 환경 변수(Environment Variable)로 관리해야 합니다.
# 実際のデプロイ時、APIキーは環境変数(Environment Variable)で管理する必要があります。
# ==========================================
API_KEY = "YOUR_API_KEY_HERE" # 본인의 API 키를 입력하세요 (ご自身のAPIキーを入力してください)

# Gemini API 클라이언트 설정
# Gemini APIクライアントの設定
genai.configure(api_key=API_KEY)

# 응답 속도와 비용 효율성이 뛰어난 Gemini 1.5 Flash 모델 사용
# 応答速度とコスト効率に優れたGemini 1.5 Flashモデルを使用
model = genai.GenerativeModel('gemini-1.5-flash')

def analyze_text(text: str) -> float:
    """
    Gemini API를 활용하여 일본어 리뷰의 감정 점수를 분석합니다.
    Gemini APIを活用して日本語レビューの感情スコアを分析します。

    Args:
        text (str): 분석할 리뷰 텍스트 (分析するレビューテキスト)
    Returns:
        float: 0.0 ~ 100.0 범위의 만족도 점수 (0.0 ~ 100.0範囲の満足度スコア)
    """

    # 디버깅 로그: 분석 요청 텍스트 확인
    # デバッグログ: 分析リクエストテキストの確認
    print(f"📡 [Gemini Request] Analyzing Text: {text}")

    try:
        # 감정 분석을 위한 시스템 프롬프트 구성 (일본어 분석 최적화)
        # 感情分析のためのシステムプロンプト構成 (日本語分析に最適化)
        prompt = f"""
        あなたは厳しい日本の旅行専門評論家です。
        以下の日本語レビューを読み、作成者の満足度を0〜100点の間で評価してください。
        
        [採点基準 - Scoring Criteria]
        - 95~100点: 完璧。感動レベル (例: 人生最高の経験)
        - 80~94点: 素晴らしい。強く推奨 (例: とても美味しい、また行きたい)
        - 60~79点: 普通。悪くない (例: 値段相応、可もなく不可もなく)
        - 40~59点: 少し残念 (例: 味はいいが高い、接客が微妙)
        - 20~39点: イマイチ。推奨しない (例: 美味しくない、失望した)
        - 0~19点: 最悪。絶対に行くな (例: 金の無駄、不快)

        [制約事項 - Constraints]
        1. 文脈の微妙なニュアンス(「しかし」「だけど」など)を正確に読み取ってください。
        2. 説明は一切せず、ただ「数字」のみを出力してください。
        
        [レビュー内容 - Review Content]
        {text}
        """

        # 1. AI 모델에 콘텐츠 생성 요청
        # 1. AIモデルへコンテンツ生成をリクエスト
        response = model.generate_content(prompt)

        # 2. 응답 텍스트 추출 및 공백 제거
        # 2. レスポンステキストの抽出および空白削除
        result_text = response.text.strip()
        print(f"✅ [Gemini Response] Raw Output: {result_text}")

        # 3. 정규표현식을 사용하여 응답에서 숫자만 추출 (예외 처리 강화)
        # 3. 正規表現を使用してレスポンスから数字のみを抽出 (例外処理の強化)
        numbers = re.findall(r'\d+', result_text)

        if numbers:
            score = float(numbers[0])
            # 점수 범위 유효성 검사 (0 ~ 100)
            # スコア範囲の有効性チェック (0 ~ 100)
            return max(0.0, min(score, 100.0))
        else:
            print("⚠️ [Warning] No valid score found in response. Returning default value.")
            return 50.0 # 파싱 실패 시 기본값 (パース失敗時のデフォルト値)

    except Exception as e:
        # API 통신 또는 로직 에러 발생 시 로그 출력
        # API通信またはロジックエラー発生時のログ出力
        print(f"💥 [Error] Analysis Failed: {e}")
        return 50.0 # 에러 발생 시 안전하게 중간값 반환 (エラー発生時、安全に中間値を返却)