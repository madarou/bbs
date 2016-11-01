#/bin/bash

echo "running crawler..."
#nohup java -jar bbscrawler.jar &
# Using full path to enable init on server bootup
nohup java -jar /crawler/bbscrawler.jar >>/var/log/crawler.log 2>&1 &
