# OpenSW
## 202111238 강성진(군휴학)

## 컴파일 명령어

### WINDOWS
`javac -cp (외부 jar 파일 이름 1):(외부 jar 파일 이름 2):,,,, src/scripts/*.java -d bin (-encoding UTF8)`

ex) `javac -cp jars/jsoup-1.14.3.jar:jars/kkma-2.1.jar src/scripts/*.java -d bin -encoding UTF8`

`javac -cp "(외부 jar 파일 이름 1);(외부 jar 파일 이름 2);,,,," src/scripts/*.java -d bin (-encoding UTF8)`

ex) `javac -cp "jars/jsoup-1.14.3.jar;jars/kkma-2.1.jar" src/scripts/*.java -d bin -encoding UTF8`

## 실행 명령어
### WINDOWS
#### 마지막 경로는 ./data/
`java -cp (외부 jar 파일 이름 1);(외부 jar 파일 이름 2);,,,,;bin scripts.kuir (args 1) (args 2) ,,, (args n)`

1. `java -cp ./jars/jsoup-1.14.3.jar;./jars/kkma-2.1.jar;bin scripts.kuir -c ./data/`
2. `java -cp ./jars/jsoup-1.14.3.jar;./jars/kkma-2.1.jar;bin scripts.kuir -k ./collection.xml'
3. `java -cp ./jars/jsoup-1.14.3.jar;./jars/kkma-2.1.jar;bin scripts.kuir -i ./index.xml`
