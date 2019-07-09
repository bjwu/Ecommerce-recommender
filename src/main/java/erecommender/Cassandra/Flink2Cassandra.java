package erecommender.Cassandra;

import erecommender.DataModels.JDBehaviorlog;
import erecommender.DataModels.JDBehaviorlogSchema;
import erecommender.Redis.JDRedisSinkMapper;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import redis.clients.jedis.Jedis;

import java.util.Properties;
import java.util.Random;

public class Flink2Cassandra {
    private static String host = "10.21.4.133"; //10.64.194.162  192.168.128.111
    private static String kafkaTopic = "action_train";
    private static Random random = new Random(20);
    private static int randInt;
    public static void main(String[] args) throws Exception {

        // kafka配置
//        final String KafkaTopic = "test";
//        final String RecommendationTopic = "recommendationlogs";
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect",  host + ":2182");
        properties.setProperty("bootstrap.servers", host + ":9092"); //broker address
        properties.setProperty("group.id", kafkaTopic);
        properties.setProperty("auto.offset.reset", "latest");

        // Redis配置
        final FlinkJedisPoolConfig Redisconf = new FlinkJedisPoolConfig.Builder()
                .setMaxTotal(100).setMaxIdle(20)
                .setHost(host).setPort(6379).build();


        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000); // 非常关键，一定要设置启动检查点！！默认就是5000ms，这里只是为了提醒
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);



        //读取kafka消息
        FlinkKafkaConsumer011<JDBehaviorlog> myConsumer = new FlinkKafkaConsumer011<JDBehaviorlog>(
                kafkaTopic,
                new JDBehaviorlogSchema(),
                properties);

        DataStream<JDBehaviorlog> newlog = env.addSource(myConsumer);




//
        //flink 写到 redis
        newlog.map(new RichMapFunction<JDBehaviorlog, JDBehaviorlog>() {

            private transient Jedis jedis;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                jedis = new Jedis(host);
            }

            @Override
            public JDBehaviorlog map(JDBehaviorlog data) throws Exception {

                String userId = "latest:" + data.getStrUserId();
                // 将redis中用户的log list数控制为20
                randInt = random.nextInt(10);
                if(randInt == 0){
                    jedis.ltrim(userId, 0, 9);
                }

                return data;
            }
        }).addSink(new RedisSink<JDBehaviorlog>(Redisconf, new JDRedisSinkMapper()));


        //        flink 写到 cassandra
//        CassandraSink.addSink(result)
//        .setQuery("INSERT INTO " + WordCount.CQL_KEYSPACE_NAME + "." + WordCount.CQL_TABLE_NAME + ...

//        CassandraSink.addSink(newlog.map(new CassandraSinkMapper()))
//                .setQuery("INSERT INTO logs.userlogs(userId, itemid,cate,timestamp,btag) values (?, ?,?,?,?);")
//                .setHost("10.64.194.162") //10.64.194.162   192.168.128.111
//                .build();
//
//
//


        env.execute();







    }


}
