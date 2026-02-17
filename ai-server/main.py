from fastapi import FastAPI
from pydantic import BaseModel
from sentiment import analyze_text

# FastAPI 인스턴스 초기화
# FastAPIインスタンスの初期化
app = FastAPI()

# 요청 데이터 검증을 위한 데이터 모델(DTO) 정의
# リクエストデータの検証を行うデータモデル(DTO)の定義
class ReviewRequest(BaseModel):
    content: str

# 감정 분석 API 엔드포인트 정의
# 感情分析APIのエンドポイント定義
@app.post("/analyze")
def analyze_sentiment(item: ReviewRequest):
    """
    클라이언트(Java Spring Boot)로부터 텍스트를 받아 감정 점수를 반환합니다.
    クライアント(Java Spring Boot)からテキストを受け取り、感情スコアを返します。
    """

    # 1. 감정 분석 모듈 호출 (Gemini API 연동)
    # 1. 感情分析モジュールの呼び出し (Gemini API連携)
    score = analyze_text(item.content)

    # 디버깅을 위한 로그 출력
    # デバッグのためのログ出力
    print(f"📡 [Log] Analyzed Score: {score}")

    # 2. 분석 결과를 JSON 형식으로 반환
    # 2. 分析結果をJSON形式で返却
    return {"score": score}