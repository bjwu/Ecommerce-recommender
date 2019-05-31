package erecommender;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HBaseSink extends ProcessFunction<String,String> {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(HBaseSink.class);

    private String _zookeeper;
    private String _port;
    private String _tableName;
    private Table _table;
    private Connection conn = null;

    public HBaseSink(String zookeeper, String port, String tableName) {
        _zookeeper = zookeeper;
        _port = port;
        _tableName = tableName;
    }

    @Override
    public void open(org.apache.flink.configuration.Configuration parameters) throws Exception {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set(HConstants.ZOOKEEPER_QUORUM, _zookeeper);
            conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, _port);
            conf.set(HConstants.ZOOKEEPER_ZNODE_PARENT, "/hbase");

//            User user = User.create(UserGroupInformation.createRemoteUser("bmps"));
            conn = ConnectionFactory.createConnection(conf);
            _table = conn.getTable(TableName.valueOf(_tableName));

            LOGGER.error("[HbaseSink] : open HbaseSink finished");
        } catch (Exception e) {
            LOGGER.error("[HbaseSink] : open HbaseSink faild {}", e);
        }
    }

    @Override
    public void close() throws Exception {
        _table.close();
    }

    @Override
    public void processElement(String  value, Context ctx, Collector<String > out)
            throws Exception {
        LOGGER.error("process String {}",value);
        String rowKey = new StringBuffer().append("1").toString();
        Put put = new Put(Bytes.toBytes(rowKey));
        put.setDurability(Durability.ASYNC_WAL);
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("flink"), Bytes.toBytes(value));
        _table.put(put);
        LOGGER.error("[HbaseSink] : put rowKey:{}, value:{} to hbase", rowKey, value);
    }



}