package erecommender;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Properties;

public class FlinkEngine {
    private static String HBaseTableName = "test1";
    private static String host = "192.168.128.111";
    private static String Flink2HBasePort = "2181";
    public static void main(String[] args) throws Exception {

//        Scanner myObj = new Scanner(System.in);
//        String host = myObj.nextLine();
//        host = "192.168.128.111";
        // kafka配置
        final String KafkaTopic = "shoppinglogs";
        Properties properties = new Properties();
        properties.setProperty("zookeeper.connect",  host + ":2182");
        properties.setProperty("bootstrap.servers", host + ":9092");
        properties.setProperty("group.id", "shoppinglogs");
        properties.setProperty("auto.offset.reset", "latest");

        // Redis配置
        final FlinkJedisPoolConfig Redisconf = new FlinkJedisPoolConfig.Builder().setHost(host).setPort(6379).build();


        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        FlinkKafkaConsumer011<Behaviorlog> myConsumer = new FlinkKafkaConsumer011<Behaviorlog>(
                KafkaTopic,
                new BehaviorlogSchema(),
                properties);
        System.out.print("Consuming data...");

        DataStream<Behaviorlog> Newlog = env
                .addSource(myConsumer);


        // 返回<userId, 最近n次的itemId>
        DataStream<Tuple2<String, List<String>>> Increlog = Newlog
                .keyBy((Behaviorlog log) -> log.getStrUserId())
                .process(new KeyedProcessFunction<String, Behaviorlog, Tuple2<String, List<String>>>() {

                    private Jedis jedis;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        this.jedis = new Jedis(host);
                        System.out.print("open jedis success");
                    }

                    @Override
                    public void processElement(Behaviorlog log, Context context, Collector<Tuple2<String, List<String>>> out) throws Exception {
                        String userId = log.getStrUserId();
                        // 将redis中用户的log list数控制为12
                        jedis.ltrim(userId, 0, 11);
                        // 提取用户最近的6次log
                        List<String> value = jedis.lrange(userId, 0, 5);
                        if (null != value){
                            out.collect(new Tuple2<String, List<String>>(userId, value));
                        }
                    }
                });

        Newlog.addSink(new RedisSink<Behaviorlog>(Redisconf, new RedisMapperSink()));

//        Newlog.addSink(new HBaseSink<Behaviorlog>(host,Flink2HBasePort,HBaseTableName);


        Increlog.print();

        env.execute();

    }
}
