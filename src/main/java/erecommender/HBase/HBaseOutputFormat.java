package erecommender.HBase;

import erecommender.DataModels.JDBehaviorlog;
import org.apache.flink.configuration.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseOutputFormat implements org.apache.flink.api.common.io.OutputFormat<erecommender.DataModels.JDBehaviorlog> {

    private org.apache.hadoop.conf.Configuration conf = null;
    private Connection conn = null;
    private Table table = null;
    private String taskNumber = null;

    private static TableName tableName = TableName.valueOf("action_train");
    private static String REMOTE_HOST = "10.21.4.133";
    private static final String columnFamily = "info";
    private static final String columnQualifier[] = {"it","ts","oi","ty"};
    private static final String ZOOKEEPER_PORT = "2182";
    private static final String HBASE_ROOTDIR = "hdfs://msp18033s1:9000/user/hduser/hbase";

    private static final long serialVersionUID = 1L;

    @Override
    public void configure(Configuration parameters) {

        conf = HBaseConfiguration.create();
        conf.set("hbase.rootdir",HBASE_ROOTDIR);
        conf.set("hbase.zookeeper.quorum",REMOTE_HOST);
        conf.set("hbase.zookeeper.property.clientPort",ZOOKEEPER_PORT);

    }

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {



        conn = ConnectionFactory.createConnection(conf);
        table = conn.getTable(tableName);
        this.taskNumber = String.valueOf(taskNumber);

    }

    @Override
    public void writeRecord(JDBehaviorlog log) throws IOException {
        Put put = new Put(Bytes.toBytes(log.getStrUserId())); //rowkey

        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[0]), Bytes.toBytes(log.getStrItemId()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[1]), Bytes.toBytes(log.getTimeStamp()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[2]), Bytes.toBytes(log.getStrOrderID()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[3]), Bytes.toBytes(log.getStrType()));
        table.put(put);


    }

    @Override
    public void close() throws IOException {

        if (table != null) {

            table.close();
        }
        if (conn != null) {
            conn.close();
        }

    }
}
