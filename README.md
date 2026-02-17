# ✈️ J-Trip (Japan Trip AI Review Analyzer)

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-green)
![Python](https://img.shields.io/badge/Python-3.10-blue)
![Gemini](https://img.shields.io/badge/AI-Gemini%201.5%20Flash-purple)

## 📖 프로젝트 소개 (Project Overview / プロジェクト概要)

### 🇰🇷 한국어
**J-Trip**은 일본 여행 리뷰를 AI로 분석하여 여행지의 만족도를 객관적인 점수로 변환해주는 서비스입니다.
Google의 **Gemini 1.5 Flash** 모델을 활용하여 일본어 텍스트의 미묘한 뉘앙스까지 파악하고, 단순한 별점이 아닌 실제 감정을 반영한 점수(0~100점)를 제공합니다.

### 🇯🇵 日本語
**J-Trip**は、日本の旅行レビューをAIで分析し、観光地の満足度を客観的なスコアに変換するサービスです。
Googleの**Gemini 1.5 Flash**モデルを活用して日本語テキストの微妙なニュアンスまで把握し、単純な星評価ではなく、実際の感情を反映したスコア（0〜100点）を提供します。

---

## ✨ 주요 기능 (Key Features / 主な機能)

### 1. AI 감정 분석 (AI Sentiment Analysis)
- **KR**: 사용자가 입력한 일본어 리뷰를 Python 기반 AI 서버로 전송하여 분석합니다.
- **JP**: ユーザーが入力した日本語レビューをPythonベースのAIサーバーに送信して分析します。

### 2. 정량적 스코어링 (Quantitative Scoring)
- **KR**: "맛있지만 비싸다" 같은 복합적인 리뷰를 0~100점 사이의 점수로 정량화합니다.
- **JP**: 「美味しいけど高い」のような複合的なレビューを0〜100点間のスコアに定量化します。

### 3. 실시간 처리 (Real-time Processing)
- **KR**: Spring Boot와 Python 서버 간의 연동을 통해 빠른 응답 속도를 보장합니다.
- **JP**: Spring BootとPythonサーバー間の連携により、高速なレスポンスを保証します。

---

## 🛠 기술 스택 (Tech Stack / 技術スタック)

| Category | Technology |
| --- | --- |
| **Backend (Main)** | Java 17, Spring Boot 3.2.2, Spring Data JPA |
| **Backend (AI)** | Python 3.x, Google Generative AI (Gemini) |
| **Database** | MySQL |
| **Build Tool** | Gradle |

---

## 🚀 실행 방법 (How to Run / 実行方法)

### 1. 환경 설정 (Prerequisites)
*   Java 17+
*   Python 3.10+
*   MySQL

### 2. 프로젝트 클론 (Clone)
```bash
git clone https://github.com/YOUR_GITHUB_ID/jtrip.git
cd jtrip
```

### 3. AI 서버 실행 (Run AI Server)
> ⚠️ **Note**: `ai-server/sentiment.py` 파일의 `API_KEY`를 설정해주세요.
> ⚠️ **注意**: `ai-server/sentiment.py`ファイルの`API_KEY`を設定してください。

```bash
cd ai-server
pip install -r requirements.txt
python main.py
```

### 4. 메인 서버 실행 (Run Spring Boot)
```bash
# Root Directory
./gradlew bootRun
```

---

## 📬 Contact
*   **Developer**: Jihoon (Your Name)
*   **Email**: your.email@example.com
*   **Github**: [https://github.com/YOUR_GITHUB_ID](https://github.com/YOUR_GITHUB_ID)
