package erecommender.Behavior;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 *
 * Store the logs data locally and deliver log to kafka continuously
 *
 */
public class UserToKafka {

    private static final String dataFilePath = "./datasets/UserBehavior.csv.gz";
    private static transient BufferedReader reader;
    private static transient InputStream gzipStream;
    private static final String LocalHost = "192.168.128.111";
    private static final String RemoteHost = "10.64.193.88";

    public static void main(String[] args) throws Exception {

        String line;
        int cnt = 0;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", RemoteHost + ":9092"); //10.64.194.162  192.168.128.111
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = null;

        gzipStream = new GZIPInputStream(new FileInputStream(dataFilePath));
        reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

        try {
            producer = new KafkaProducer<String, String>(properties);
            while (reader.ready()) {
                if (reader.ready() && (line = reader.readLine()) != null) {
                    // avoid outputing the first label line
                    if (cnt == 0){
                        cnt++;
                        continue;
                    }
                    producer.send(new ProducerRecord<String, String>("testlogs", line));
                    // TODO: can be set to random
                    TimeUnit.SECONDS.sleep(1);
                    cnt++;
                }
//                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
