package erecommender;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class BehaviorlogSchema implements DeserializationSchema<Behaviorlog>, SerializationSchema<Behaviorlog> {

    @Override
    public byte[] serialize(Behaviorlog element) {
        return element.toString().getBytes();
    }

    @Override
    public Behaviorlog deserialize(byte[] message) {
        return Behaviorlog.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(Behaviorlog nextElement) {
        return false;
    }

    @Override
    public TypeInformation<Behaviorlog> getProducedType() {
        return TypeExtractor.getForClass(Behaviorlog.class);
    }
}
