package erecommender;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

import java.util.Properties;

public class FlinkEngine {
    public static void main(String[] args) throws Exception {

        final String KafkaTopic = "test";
        final FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("192.168.128.111").build();


        // Consume from kafka
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect", "192.168.128.111:2182");
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "test");
        properties.setProperty("auto.offset.reset", "earliest");

        FlinkKafkaConsumer011<Behaviorlog> myConsumer = new FlinkKafkaConsumer011<Behaviorlog>(
                KafkaTopic,
                new BehaviorlogSchema(),
                properties);

        DataStream<Behaviorlog> Newlog = env
                .addSource(myConsumer);


//        DataStream<Behaviorlog> Oldlog = Newlog
//                .keyBy((Behaviorlog log) -> log.getUserId())
//                .process(new KeyedProcessFunction<Integer, Behaviorlog, Behaviorlog>() {
//                    @Override
//                    public void processElement(Behaviorlog Behaviorlog, KeyedProcessFunction.Context context, Collector<Behaviorlog> collector) throws Exception {
//
//                    }
//                });

//        Newlog.addSink(new RedisSink<Behaviorlog>(conf, new RedisSink());



        // Execute
        env.execute();

    }
}
