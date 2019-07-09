package erecommender.HBase.pool;

import erecommender.DataModels.Behaviorlog;
import erecommender.DataModels.BehaviorlogSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;


import java.util.Properties;

public class Flink2HBase {

    private static String KAFKA_TOPIC = "test";
    private static String REMOTE_HOST = "10.21.4.133";


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
//
//        DataStream<String> transction = env.addSource(new FlinkKafkaConsumer011<String>("test", new SimpleStringSchema(), properties));
//
//
//        transction.rebalance().map(new HBaseLogSinkMapper());




        env.execute();


    }

}

