## Kafak 部署

```shell
$ cd /usr/local/kafka

#创建partition
$ bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

$ bin/kafka-topics.sh --list --zookeeper localhost:2181

#Send message
$ bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

$ bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
```

## Flink 部署

```bash
$ cd /opt/flink-1.6.2

# starts a JobManager on the local node and connects to all worker nodes listed in the slaves file to start the TaskManager on each node
$ bin/start-cluster.sh
```
