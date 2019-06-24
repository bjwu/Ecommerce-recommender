package erecommender.Recommendation;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class RecommendationlogSchema implements DeserializationSchema<RecommendationLog>, SerializationSchema<RecommendationLog> {

    @Override
    public byte[] serialize(RecommendationLog element) {
        return element.toString().getBytes();
    }

    @Override
    public RecommendationLog deserialize(byte[] message) {
        return RecommendationLog.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(RecommendationLog nextElement) {
        return false;
    }

    @Override
    public TypeInformation<RecommendationLog> getProducedType() {
        return TypeExtractor.getForClass(RecommendationLog.class);
    }
}
