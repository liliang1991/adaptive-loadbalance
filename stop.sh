jps|grep "service-provider*"|awk '{print $1}'|xargs kill -9
sleep 2
jps|grep "service-consumer*"|awk '{print $1}'|xargs kill -9
echo 'success'
