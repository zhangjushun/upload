
rem jar平级目录
set AppName=upload-0.0.1.jar
rem JVM参数
set JVM_OPTS="-Dname=%AppName%  -Duser.timezone=Asia/Shanghai -Xms1024m -Xmx2048m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC"


title upload
start jre1.8.0_301/bin/java %JVM_OPTS% -jar %AppName%
pause;
