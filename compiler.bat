cd frontend && ng build && cd .. && rmdir /Q /S backend\src\main\resources\static && mkdir backend\src\main\resources\static && xcopy frontend\dist\eternal-dynasties backend\src\main\resources\static /s /e && cd backend && mvn clean package -DskipTests=true