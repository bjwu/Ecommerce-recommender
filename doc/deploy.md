## Kafka 部署

```shell
$ cd /usr/local/kafka

# start zookeeper
$ ./bin/zookeeper-server-start.sh config/zookeeper.properties

# start kafka server
$ ./bin/kafka-server-start.sh config/server.properties

#创建topic
$ bin/kafka-topics.sh --create --zookeeper localhost:2182 --replication-factor 1 --partitions 1 --topic shoppinglogs

$ bin/kafka-topics.sh --list --zookeeper localhost:2182

#Send message
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning

# delete topic
$ bin/kafka-topics --delete --zookeeper 【zookeeper server:port】 --topic 【topic name】
```

## Flink 部署

```bash
$ cd /opt/flink-1.6.2

# starts a JobManager on the local node and connects to all worker nodes listed in the slaves file to start the TaskManager on each node
$ bin/start-cluster.sh
```

## Redis

```bash
# 启动redis
$ /usr/local/redis/bin/redis-server /usr/local/redis/etc/redis.conf
```

常用命令

```bash
redis-server /usr/local/redis/etc/redis.conf #启动redis

pkill redis #停止redis

kill -9 pid

卸载redis：
rm -rf /usr/local/redis #删除安装目录
rm -rf /usr/bin/redis-* #删除所有redis相关命令脚本
rm -rf /root/download/redis-4.0.8 #删除redis解压文件夹
```