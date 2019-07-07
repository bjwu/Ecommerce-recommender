package erecommender.pool.HBase;

import org.apache.flink.api.common.functions.MapFunction;

import static erecommender.pool.HBase.HBaseLogSink.writeIntoHBase;

public class HBaseLogSinkMapper implements MapFunction<String, Object> {
    private static final long serialVersionUID = 1L;
    @Override
    public Object map(String value) throws Exception {

        writeIntoHBase(value);
        return null;
    }


}
