package erecommender.HBase.pool.HBase;

import erecommender.DataModels.JDBehaviorlog;
import org.apache.flink.api.common.functions.MapFunction;

import static erecommender.HBase.pool.HBase.HBaseLogSink.writeIntoHBase;

public class HBaseLogSinkMapper implements MapFunction<JDBehaviorlog, Object> {
    private static final long serialVersionUID = 1L;
    @Override
    public Object map(JDBehaviorlog log) throws Exception {

        writeIntoHBase(log);
        return null;
    }


}
