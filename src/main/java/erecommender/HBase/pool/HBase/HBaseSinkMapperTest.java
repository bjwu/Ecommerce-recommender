package erecommender.HBase.pool.HBase;

import org.apache.flink.api.common.functions.MapFunction;

import static erecommender.HBase.pool.HBase.HBaseSinkTest.writeIntoHBase;

public class HBaseSinkMapperTest implements MapFunction<String, Object> {
private static final long serialVersionUID = 1L;
@Override
public Object map(String log) throws Exception {

        writeIntoHBase(log);
        return null;
        }
}
