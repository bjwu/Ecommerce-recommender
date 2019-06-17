**MAKE SURE YOU ARE IN HDUSER !!!**

## Ngrok 连接

```bash
# on host
$ cd ./Download

$ ./ngrok tcp -region ap 22
# 保持弹出来的窗口不关闭，port口每次都会边
```

## Hadoop

```bash
# on host

# 开启hdfs，查看NameNode，DataNode
$ cd /opt/hadoop-2.6.5/sbin
$ ./start-dfs.sh

# 开启yarn，查看ResourceManager，NodeManager
$ ./start-yarn.sh

# 关闭
$ ./stop-all.sh
```

## Flink

```bash
# on host

# 开启，查看StandaloneSessionClusterEntrypoint，TaskManagerRunner
$ cd /opt/flink-1.6.4/bin
$ ./start-cluster.sh 

# 关闭
$ ./stop-cluster.sh
```

## Kafka

```shell
$ cd /usr/local/kafka

# 开启zookeeper，查看
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

## Redis

```bash
# 启动redis
$ /usr/local/redis/bin/redis-server /usr/local/redis/etc/redis.conf
```