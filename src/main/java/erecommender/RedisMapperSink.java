package erecommender;

import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class RedisMapperSink implements RedisMapper<Behaviorlog> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.SET, "HASH_NAME");
    }

    @Override
    public String getKeyFromData(Behaviorlog data) {
//        System.out.print(data.getStrUserId());
        return data.getStrUserId();
    }

    @Override
    public String getValueFromData(Behaviorlog data) {
        return data.getStrItemId();
    }
}
