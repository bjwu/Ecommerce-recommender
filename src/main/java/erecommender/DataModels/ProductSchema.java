package erecommender.DataModels;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;

public class ProductSchema implements DeserializationSchema<Product>, SerializationSchema<Product> {


        @Override
        public byte[] serialize(Product element) {
            return element.toString().getBytes();
        }

        @Override
        public Product deserialize(byte[] message) {
            return Product.fromString(new String(message));
        }

        @Override
        public boolean isEndOfStream(Product nextElement) {
            return false;
        }

        @Override
        public TypeInformation<Product> getProducedType() {
            return TypeExtractor.getForClass(Product.class);
        }


}
