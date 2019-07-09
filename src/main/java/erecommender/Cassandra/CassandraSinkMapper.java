package erecommender.Cassandra;

import erecommender.DataModels.Behaviorlog;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple5;


public class CassandraSinkMapper implements MapFunction<Behaviorlog, Tuple5<Integer, Integer,Integer,Long,String>> {

    @Override
    public Tuple5<Integer, Integer,Integer,Long,String> map(Behaviorlog s) {
        Tuple5<Integer, Integer,Integer,Long,String> rlog = new Tuple5<>(s.getUserId(),s.getItemId(),s.getcate(),s.getTimeStamp(),s.getbtag());

        return rlog;
    }
}
