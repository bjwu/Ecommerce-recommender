package erecommender.DataModels;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class UserProfileSchema implements DeserializationSchema<UserProfile>, SerializationSchema<UserProfile> {


    @Override
    public byte[] serialize(UserProfile element) {
        return element.toString().getBytes();
    }

    @Override
    public UserProfile deserialize(byte[] message) {
        return UserProfile.fromString(new String(message));
    }

    @Override
    public boolean isEndOfStream(UserProfile nextElement) {
        return false;
    }

    @Override
    public TypeInformation<UserProfile> getProducedType() {
        return TypeExtractor.getForClass(UserProfile.class);
    }


}