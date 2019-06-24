# 0. Version Statement 

- Hadoop:
    - version = 2.6.5	
    - HADOOP_HOME = /usr/local/hadoop
- Cassandra:	
    -  verision = 3.9	
    - CASSANDRA_HOME = /usr/local/cassandra
- Redis: 	
    - version = 4.0.14	
    - REDIS_HOME = /usr/local/redis
- Kafka: 	
    - verision = 0.11.0.3	
    - KAFKA_HOME  = /usr/local/kafka
- Flink: 	
    - verison = 1.7.2	
    - FLINK_HOME = /opt/flink-1.7.2

# 1. Hadoop 

## 1.1. Start DFS

```shell
$ /usr/local/hadoop/sbin/start-dfs.sh
```

## 1.2. Start Yarn

```shell
$ /usr/local/hadoop/sbin/start-yarn.sh
```

## 1.3. Stop Yarn

```shell
$ /usr/local/hadoop/sbin/stop-yarn.sh
```

## 1.4. Stop DFS

```shell
$ /usr/local/hadoop/sbin/stop-dfs.sh
```

# 2. Cassandra

## 2.1. Start Server

```shell
 $ /usr/local/cassandra/bin/cassandra -f
```

## 2.2. Start cqlsh

```shell
$ /usr/local/cassandra/bin/cqlsh
```



# 3. Redis

## 3.1. Start Server

```shell
$ /usr/local/redis/bin/redis-server /usr/local/redis/etc/redis.conf
```

## 3.2. Start redis-cli

```shell
$ /usr/local/redis/bin/redis-cli 
```

## 3.3. Stop Server

```shell
$ /usr/local/redis/bin/redis-cli shutdown

```

# 4. Kafka

## 4.1. Start Server

```shell
$ /usr/local/kafka/bin/kafka-server-start.sh   /usr/local/kafka/config/server.properties

```

## 4.2. Create Topic

```shell
$ /usr/local/kafka/bin/kafka-topics.sh --zookeeper localhost:2182 --create --topic TOPICNAME --partitions 10  --replication-factor 1

```

## 4.3. List Topics

```shell
$ /usr/local/kafka/bin/kafka-topics.sh --list --zookeeper localhost:2182

```

## 4.4. kafka-console-consumer

```shell
$ /usr/local/kafka/bin/kafka-console-consumer.sh --zookeeper localhost:2182 --topic testlogs --from-beginning

```

## 4.5. Delete Topic

```shell
$ /usr/local/kafka/bin/kafka-topics.sh --zookeeper localhost:2182 --delete --topic TOPICNAME

```



# 5. Flink
## 5.0. Start Zookeeper
```shell
$ /usr/local/kafka/bin/zookeeper-server-start.sh config/zookeeper.properties
```
## 5.1. Start Server

```shell
$ /opt/flink-1.7.2/bin/start-cluster.sh

```

## 5.2. Stop Server

```shell
$ /opt/flink-1.7.2/bin/stop-cluster.sh

```

