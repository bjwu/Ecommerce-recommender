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

* 常用命令

```bash
redis-server /usr/local/redis/etc/redis.conf #启动redis

pkill redis #停止redis

kill -9 pid

卸载redis：
rm -rf /usr/local/redis #删除安装目录
rm -rf /usr/bin/redis-* #删除所有redis相关命令脚本
rm -rf /root/download/redis-4.0.8 #删除redis解压文件夹
```

## hbase
## Hbase 部署

- [Hbase部署参考]([http://hbase.praveendeshmane.co.in/hbase/hbase-1-2-3-fully-distributed-mode-installation-on-ubuntu-14-04.jsp](http://hbase.praveendeshmane.co.in/hbase/hbase-1-2-3-fully-distributed-mode-installation-on-ubuntu-14-04.jsp))

- 按上述部署的几个注意点：

  - mater和slave的ip地址必须是互相能ping通的（在部署完之后）。
  - 这里的hduser相当于master对外的名字
  - `hbase-site.xml`中的namenode记得替换成master名字

- 巨坑：

  - 即使按照部署做了，并且可以在master下用 `jps`命令看到HMaster 和 HRuorumPeer(Hbase的zookeeper)；但当ssh到slave1或者slave2的时候，用`jps`只能看到HQuoromPeer进程，没有regionserver进程。也就是说其实regionserver都没有开起来。

    通过Hbase提供的默认可视化端口：192.168.128.111:16010 可以看到region server下面没有结点：

    ![](/Users/zhu/Documents/courses/Project/Images/Hbase1.png)

  - 这时候，需要手动分别到 slave1和slave2 开始region server：

    ```python
    $ cd /usr/local/hbase/bin
    $ ./hbase-daemon.sh start regionserver
    ```

    通过 jps 命令可以看到在slave1和slave2上有了HRegionServer进程，并且在可视化界面看到Region Servers被开启：

    ![](/Users/zhu/Documents/courses/Project/Images/Hbase2.png)

- 最后，可以在master进行简单的测试：

  ```python
  $ cd /usr/local/hbase/bin
  $ ./hbase shell
  hbase(main):001:0> list
  TABLE
  TableName
  member
  student                                                                      
  3 row(s) in 0.2920 seconds
  => ["TableName", "member", "student"]
  ```

  上面的 member和student是我创建的表格。现在测试正常，表明我可以在这个集群上使用Hbase了。原先在没有开启regionserver的时候会报错。

  

  

- Hbase利用RPC java API 在我自己没有部署集群的mac上对Hbase进行远程调用。示例程序。

  但是这个程序在我自己的电脑上会报错：

  ```python
  3908 [main-SendThread(192.168.128.111:2181)] DEBUG org.apache.zookeeper.ClientCnxn  - Reading reply sessionid:0x6b093bc95e0005, packet:: clientPath:null serverPath:null finished:false header:: 14,4  replyHeader:: 14,73014444100,0  request:: '/hbase/master,F  response:: #ffffffff000146d61737465723a31363030301affffffa7ffffffaf50ffffffbc41357450425546a13a77a68752d686b7510ffffff807d18ffffffa9ffffffa5ffffffefffffffc9ffffffb02d10018ffffff8a7d,s{73014444036,73014444036,1559228047291,1559228047291,0,0,0,102185568823476224,55,0,73014444036} 
  3909 [main] DEBUG org.apache.hadoop.hbase.ipc.RpcClientImpl  - Use SIMPLE authentication for service MasterService, sasl=false
  3909 [main] DEBUG org.apache.hadoop.hbase.ipc.RpcClientImpl  - Connecting to zhu-hku/192.168.128.111:16000
  5954 [main-SendThread(192.168.128.111:2181)] DEBUG org.apache.zookeeper.ClientCnxn  - Reading reply sessionid:0x6b093bc95e0005, packet:: clientPath:null serverPath:null finished:false header:: 15,3  replyHeader:: 15,73014444100,0  request:: '/hbase,F  response:: s{4294967298,4294967298,1559055464098,1559055464098,0,83,0,0,0,17,73014444056} 
  5961 [main-SendThread(192.168.128.111:2181)] DEBUG org.apache.zookeeper.ClientCnxn  - Reading reply sessionid:0x6b093bc95e0005, packet:: clientPath:null serverPath:null finished:false header:: 16,4  replyHeader:: 16,73014444100,0  request:: '/hbase/master,F  response:: #ffffffff000146d61737465723a31363030301affffffa7ffffffaf50ffffffbc41357450425546a13a77a68752d686b7510ffffff807d18ffffffa9ffffffa5ffffffefffffffc9ffffffb02d10018ffffff8a7d,s{73014444036,73014444036,1559228047291,1559228047291,0,0,0,102185568823476224,55,0,73014444036} 
  5961 [main] DEBUG org.apache.hadoop.hbase.ipc.RpcClientImpl  - Use SIMPLE authentication for service MasterService, sasl=false
  5962 [main] DEBUG org.apache.hadoop.hbase.ipc.RpcClientImpl  - Connecting to zhu-hku/192.168.128.111:16000
  ```

  上面这个问题研究了一天多还是没有解决。最后只好把写好的java程序打包成 jar，然后发送给master，直接在master上运行。可以看到程序是正常运行的。

  这里有两种方式把文件发送给服务器（master）：

  1. scp

     ```python
     $ scp -r /home/USERNAME/FILTTOSEND  hduser@10.10.10.2:/usr/local/hbase
     ```

     后面也可以指定端口 -p 。[具体参考](https://linuxtools-rst.readthedocs.io/zh_CN/latest/tool/scp.html)。

  2. samba

     - 利用前面的samba文件共享，直接把本地的jar拖进共享文件，master直接运行。这种方式比scp更方便也更快。
- Hbase常用命令
    ```python
    $ cd /usr/local/hbase/conf   #配置hbase文件
    $ cd /usr/local/hbase/bin   #启动hbase相关程序目录
    $ ./start-hbase.sh 			#在bin目录下启动 hbase，将会自动启动zookeeeper和HMaster
    $ ./hbase-daemon.sh start regionserver #在slave的bin目录中开启region server
    $ ./hbase shell  #在master的bin目录下开始hbase的shell界面
    ```



