package erecommender.HBase.pool.HBase;

import erecommender.HBase.pool.tool.ConnectionPoolConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HBaseSinkTest {
    private static TableName tableName = TableName.valueOf("Flink2HBase");
    private static String REMOTE_HOST = "10.21.4.133";
    private static final String columnFamily = "info";
    private static final String columnQualifier[] = {"it","ts","oi","ty"};
    private static final String ZOOKEEPER_PORT = "2182";
    private static final String HBASE_ROOTDIR = "hdfs://msp18033s1:9000/user/hduser/hbase";

    public static void writeIntoHBase(String log)throws IOException {
        ConnectionPoolConfig config = new ConnectionPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);


        Configuration hbaseConfig = HBaseConfiguration.create();

        hbaseConfig = HBaseConfiguration.create();
        hbaseConfig.set("hbase.defaults.for.version.skip", "true");
        hbaseConfig.set("hbase.rootdir",HBASE_ROOTDIR);
        hbaseConfig.set("hbase.zookeeper.quorum",REMOTE_HOST);
        hbaseConfig.set("hbase.zookeeper.property.clientPort",ZOOKEEPER_PORT);


        HbaseConnectionPool pool = null;

        try {
            pool = new HbaseConnectionPool(config, hbaseConfig);

            Connection con = pool.getConnection();

            Admin admin = con.getAdmin();

            if(!admin.tableExists(tableName)){
                admin.createTable(new HTableDescriptor(tableName).addFamily(new HColumnDescriptor(columnFamily)));
            }
            Table table = con.getTable(tableName);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Put put = new Put(Bytes.toBytes(df.format(new Date()))); //rowkey

            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[0]),Bytes.toBytes(log));
//
//            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[0]), Bytes.toBytes(log.getItemId()));
//            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[1]), Bytes.toBytes(log.getTimeStamp()));
//            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[2]), Bytes.toBytes(log.getOrderID()));
//            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnQualifier[3]), Bytes.toBytes(log.getType()));
            table.put(put);
            table.close();

            pool.returnConnection(con);

        } catch (Exception e) {
            pool.close();
        }
    }
}
