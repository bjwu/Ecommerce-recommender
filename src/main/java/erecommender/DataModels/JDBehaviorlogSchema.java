package erecommender.DataModels;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class JDBehaviorlogSchema implements DeserializationSchema<JDBehaviorlog>, SerializationSchema<JDBehaviorlog> {

    @Override
    public byte[] serialize(JDBehaviorlog element) {
        return element.toString().getBytes();
    }

    @Override
    public JDBehaviorlog deserialize(byte[] message) {
        return JDBehaviorlog.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(JDBehaviorlog nextElement) {
        return false;
    }

    @Override
    public TypeInformation<JDBehaviorlog> getProducedType() {
        return TypeExtractor.getForClass(JDBehaviorlog.class);
    }
}
