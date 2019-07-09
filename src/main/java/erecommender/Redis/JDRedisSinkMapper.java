package erecommender.Redis;

import erecommender.DataModels.JDBehaviorlog;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;

public class JDRedisSinkMapper implements RedisMapper<JDBehaviorlog> {
    @Override
    public RedisCommandDescription getCommandDescription() {
        return new RedisCommandDescription(RedisCommand.LPUSH, "HASH_NAME");
    }

    @Override
    public String getKeyFromData(JDBehaviorlog data) {
//        System.out.print(data.getStrUserId());
        return "latest:"+data.getStrUserId();
    }

    @Override
    public String getValueFromData(JDBehaviorlog data) {
        return data.getStrItemId() + " " +data.getStrType();
    }
}
