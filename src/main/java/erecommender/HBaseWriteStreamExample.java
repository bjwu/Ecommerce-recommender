package erecommender;

import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * This is an example how to write streams into HBase. In this example the
 * stream will be written into a local Hbase but it is possible to adapt this
 * example for an HBase running in a cloud. You need a running local HBase with a
 * table "flinkExample" and a column "entry". If your HBase configuration does
 * not fit the hbase-site.xml in the resource folder then you gave to delete temporary this
 * hbase-site.xml to execute the example properly.
 */
public class HBaseWriteStreamExample {

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();

        // data stream with random numbers
        DataStream<String> dataStream = env.addSource(new SourceFunction<String>() {
            private static final long serialVersionUID = 1L;

            private volatile boolean isRunning = true;

            @Override
            public void run(SourceContext<String> out) throws Exception {
                while (isRunning) {
                    out.collect(String.valueOf(Math.floor(Math.random() * 100)));
                    System.out.println("isRuning..."+out.toString());
                }

            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });
        dataStream.writeUsingOutputFormat(new HBaseOutputFormat());

        env.execute();
    }

    /**
     * This class implements an OutputFormat for HBase.
     */
    private static class HBaseOutputFormat implements OutputFormat<String> {

        private Admin admin = null;
        private Connection conn = null;
        private static org.apache.hadoop.conf.Configuration conf;
        private Table table = null;
        private String taskNumber = null;
        private int rowNumber = 0;

        private static final long serialVersionUID = 1L;

        @Override
        public void configure(Configuration parameters){

            // 取得一个数据库连接的配置参数对象
            conf = HBaseConfiguration.create();
            // 设置连接参数：HBase数据库所在的主机IP
            conf.set("hbase.zookeeper.quorum", "192.168.128.111");
            // 设置连接参数：HBase数据库使用的端口
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            conf.set("hbase.master", "192.168.128.111:16000");
            try{
                conn = ConnectionFactory.createConnection(conf);
                // 取得一个数据库元数据操作对象
                admin = conn.getAdmin();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void open(int taskNumber, int numTasks) throws IOException {
            table = conn.getTable(TableName.valueOf("flinkExample"));
            this.taskNumber = String.valueOf(taskNumber);
        }

        @Override
        public void writeRecord(String record) throws IOException {
            Put put = new Put(Bytes.toBytes(taskNumber + rowNumber));
            put.addColumn(Bytes.toBytes("entry"), Bytes.toBytes("entry"),
                    Bytes.toBytes(rowNumber));
            rowNumber++;
            table.put(put);
        }

        @Override
        public void close() throws IOException {
//            table.flushCommits();
            table.close();
        }

    }
}
