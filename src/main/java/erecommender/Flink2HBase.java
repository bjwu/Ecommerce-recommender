package erecommender;

import erecommender.Behavior.Behaviorlog;
import erecommender.Behavior.BehaviorlogSchema;
import erecommender.pool.HBase.HbaseConnectionPool;
import erecommender.pool.tool.ConnectionPoolConfig;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Flink2HBase {

    private static String KAFKA_TOPIC = "test";
    private static String REMOTE_HOST = "10.21.4.133";
    private static TableName tableName = TableName.valueOf("Flink2HBase");
    private static final String columnFamily = "info";
    private static final String ZOOKEEPER_PORT = "2182";
    private static final String HBASE_ROOTDIR = "hdfs://msp18033s1:9000/user/hduser/hbase";

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect",  REMOTE_HOST + ":2182");
        properties.setProperty("bootstrap.servers", REMOTE_HOST + ":9092"); //broker address
        properties.setProperty("group.id",KAFKA_TOPIC);
        properties.setProperty("auto.offset.reset", "latest");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(1000); // 非常关键，一定要设置启动检查点！！
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        FlinkKafkaConsumer011<Behaviorlog> myConsumer = new FlinkKafkaConsumer011<Behaviorlog>(
                KAFKA_TOPIC,
                new BehaviorlogSchema(),
                properties);

        DataStream<String> transction = env.addSource(new FlinkKafkaConsumer011<String>("test", new SimpleStringSchema(), properties));

//        //读取kafka消息
//        DataStream<Behaviorlog> Newlog = env
//                .addSource(myConsumer);

        transction.rebalance().map(new MapFunction<String, Object>() {
            private static final long serialVersionUID = 1L;

            public String map(String value) throws IOException {
                System.out.println(value);
                writeIntoHBase(value);
                return null;
            }
        });


        env.execute();


    }

    public static void writeIntoHBase(String m)throws IOException{
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);


        Configuration hbaseConfig = HBaseConfiguration.create();

        hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.defaults.for.version.skip", "true");
        hbaseConfig.set("hbase.rootdir",HBASE_ROOTDIR);
        hbaseConfig.set("hbase.zookeeper.quorum",REMOTE_HOST);
        hbaseConfig.set("hbase.zookeeper.property.clientPort",ZOOKEEPER_PORT);


        HbaseConnectionPool pool = null;

        try {
            pool = new HbaseConnectionPool(config, hbaseConfig);

            Connection con = pool.getConnection();

            Admin admin = con.getAdmin();

            if(!admin.tableExists(tableName)){
                admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor(columnFamily)));
            }
            Table table = con.getTable(tableName);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Put put = new Put(Bytes.toBytes(df.format(new Date())));

            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("col"), Bytes.toBytes(m));
            table.put(put);
            table.close();

            pool.returnConnection(con);

        } catch (Exception e) {
            pool.close();
        }
    }
}

