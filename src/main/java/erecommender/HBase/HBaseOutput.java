package erecommender.HBase;

import erecommender.DataModels.*;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

public class HBaseOutput {
    private static String host = "10.21.4.133"; //10.64.194.162  192.168.128.111

    // change kafkatopic! "product"=> product_info; "user_profile" => user_profile
    private static String kafkaTopic = "user_profile";
    public static void main(String[] args) throws Exception {

        // kafka配置
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect",  host + ":2182");
        properties.setProperty("bootstrap.servers", host + ":9092"); //broker address
        properties.setProperty("group.id", kafkaTopic);
        properties.setProperty("auto.offset.reset", "latest");


        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000); // 非常关键，一定要设置启动检查点！！默认就是5000ms，这里只是为了提醒
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);



        //读取kafka消息
        FlinkKafkaConsumer011<UserProfile> myConsumer = new FlinkKafkaConsumer011<UserProfile>(
                kafkaTopic,
                new UserProfileSchema(),
                properties);

        DataStream<UserProfile> newlog = env.addSource(myConsumer);



//        flink 写到 HBase


        newlog.writeUsingOutputFormat(new HBaseOutputFormatUserProfile());




//        flink 写到 kafka
//        FlinkKafkaProducer011<RecommendationLog> flinkKafkaProducer = new FlinkKafkaProducer011<RecommendationLog> (
//                RecommendationTopic,
//                new RecommendationlogSchema(),
//                properties);
//
//        Increlog
//                .map(new RecommentdationMapper())
//                .addSink(flinkKafkaProducer);


        env.execute();




//        flink 写到 cassandra
//        CassandraSink.addSink(result)
//        .setQuery("INSERT INTO " + WordCount.CQL_KEYSPACE_NAME + "." + WordCount.CQL_TABLE_NAME + ...

//        CassandraSink.addSink(Newlog.map(new CassandraSinkMapper()))
//                .setQuery("INSERT INTO logs.userlogs(userId, itemid,cate,timestamp,btag) values (?, ?,?,?,?);")
//                .setHost("10.64.194.162") //10.64.194.162   192.168.128.111
//                .build();



    }


}
