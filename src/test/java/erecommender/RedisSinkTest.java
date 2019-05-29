package erecommender;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;

public class RedisSinkTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build();

        DataStream<Behaviorlog> logs = env.fromElements(
                new Behaviorlog(1234, 223123124, "hello", 1231233, 121),
                new Behaviorlog(121234, 223123124, "hi", 1231233, 121)
        );

        logs.addSink(new RedisSink<Behaviorlog>(conf, new RedisSink());
    }
}
