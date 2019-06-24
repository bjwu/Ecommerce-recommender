package erecommender.Recommendation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;

import java.util.List;

public class RecommentdationMapper implements MapFunction<Tuple2<String, List<String>>, RecommendationLog> {

    @Override
    public RecommendationLog map(Tuple2<String, List<String>> s) {
        RecommendationLog rlog = new RecommendationLog(Integer.parseInt(s.f0),Integer.parseInt(s.f1.get(0)));

        return rlog;
    }
}
