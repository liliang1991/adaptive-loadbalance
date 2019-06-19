mvn clean install -DskipTests
nohup java -Dquota=small -jar  ./service-provider/target/service-provider.jar >small.txt 2>&1 &
nohup java -Dquota=medium -jar  ./service-provider/target/service-provider.jar >medium.txt 2>&1 &
nohup java -Dquota=large -jar  ./service-provider/target/service-provider.jar >large.txt 2>&1 &
sleep 5
nohup java -jar ./service-consumer/target/service-consumer.jar >consumer.txt 2>&1 &
echo 'success'
