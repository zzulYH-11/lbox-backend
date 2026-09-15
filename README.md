# lbox-backend
CEOS 24기 엘박스 역기획 프로젝트입니다.

## 🚀 실행 방법 (Getting Started)

### 1. 요구 사항
프로젝트를 실행하기 위해 다음 환경이 필요합니다.
- **Java 21**

### 2. 프로젝트 실행 명령어
터미널에서 프로젝트 루트 디렉토리로 이동한 후, 아래 명령어를 실행하여 애플리케이션을 시작합니다.

**Mac/Linux**
```bash
./gradlew bootRun
```

**Windows**
```cmd
gradlew.bat bootRun
```

### 3. Health Check API 경로
서버가 정상적으로 구동되었는지 확인하려면 다음 API를 호출합니다.

- **URL**: `http://localhost:8080/api/health`
- **Method**: `GET`
- **정상 응답**:
  ```json
  {
    "status": "ok"
  }
  ```
