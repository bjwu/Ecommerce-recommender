package erecommender.HBase;

import erecommender.DataModels.Product;
import org.apache.flink.configuration.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HBaseOutputFormatProduct implements org.apache.flink.api.common.io.OutputFormat<Product> {

    private org.apache.hadoop.conf.Configuration conf = null;
    private Connection conn = null;
    private Table table = null;
    private String taskNumber = null;

    private static TableName tableName = TableName.valueOf("product_info");
    private static String REMOTE_HOST = "10.21.4.133";
    private static final String columnFamily = "info";
    private static final String columnQualifier[] = {"br","si","ca","ts"};
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
    public void writeRecord(Product log) throws IOException {
        Put put = new Put(Bytes.toBytes(log.getStrItemId())); //rowkey
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[0]), Bytes.toBytes(log.getStrBrandId()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[1]), Bytes.toBytes(log.getStrShopId()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[2]), Bytes.toBytes(log.getStrCate()));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[3]), Bytes.toBytes(log.getTimeStamp()));
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
