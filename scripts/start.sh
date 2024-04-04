PROJECT_ROOT="/home/ubuntu"
ZIP_FILE="$PROJECT_ROOT/ticketradar.zip"

# zip 파일 압축 풀기
echo "$TIME_NOW > $ZIP_FILE 파일 압축 해제" >> $DEPLOY_LOG
unzip -q $ZIP_FILE -d $PROJECT_ROOT

# 실행 가능한 파일 확인
echo "$TIME_NOW > $PROJECT_ROOT 내에 있는 실행 가능한 파일 목록 확인" >> $DEPLOY_LOG
ls -l $PROJECT_ROOT
JAR_FILE="$PROJECT_ROOT/TicketRaider-0.0.1-SNAPSHOT.jar"
# jar 파일 실행
echo "$TIME_NOW > $JAR_FILE 파일 실행" >> $DEPLOY_LOG
nohup java -jar $PROJECT_ROOT/TicketRaider-0.0.1-SNAPSHOT.jar > $APP_LOG 2> $ERROR_LOG &

CURRENT_PID=$(pgrep -f $JAR_FILE)
echo "$TIME_NOW > 실행된 프로세스 아이디 $CURRENT_PID 입니다." >> $DEPLOY_LOG
