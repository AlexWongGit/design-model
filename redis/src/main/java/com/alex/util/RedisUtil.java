package com.alex.util;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wangzf
 * @description: redis工具类
 * @date 2023/12/29
 */
@Component
public class RedisUtil {

    public static final Long LOCK_TTL = 120L;

    @Resource
    private RedisTemplate<String,Object> stringRedisTemplate;

    /**
     * @param key
     * @param value
     * @param ttl
     * @param unit
     * @return boolean
     * @description: 自定义互斥锁，利用redis的setnx方法来表示获取锁
     */
    public boolean tryLock(String key, String value, long ttl, TimeUnit unit) {
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, ttl, unit));
    }

    /**
     * @param key
     * @return void
     * @description: 释放互斥锁
     */
    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * @param key
     * @return 获取字符串
     * @description
     */
    public Object getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * @param key
     * @param clazz
     * @return T
     * @description: 获取指定的类
     */
    public <T> T getString(String key, Class<T> clazz) {
        Object data = stringRedisTemplate.opsForValue().get(key);
        return StringUtils.isEmpty(data) ? null : JSONUtil.toBean(String.valueOf(data), clazz);
    }

    /**
     * @param key
     * @param data
     * @return void
     * @description: 存入字符串型数据并设置过期时间
     */
    public void setString(String key, String data) {
        stringRedisTemplate.opsForValue().set(key, data);
    }

    /**
     * @param key
     * @param data
     * @param ttl
     * @param unit
     * @return void
     * @description: 存入字符串型数据并设置过期时间
     */
    public void setString(String key, String data, long ttl, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(key, data, ttl, unit);
    }

    /**
     * @param key
     * @param data
     * @return void
     * @description: 从后向集合中插入数据
     */
    public void rightPushList(String key, List<String> data) {
        stringRedisTemplate.opsForList().rightPushAll(key, data);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @param clazz
     * @return
     * @description: 按照索引获取集合并序列化为相应对象
     */
    public <T> List<T> getList(String key, long start, long end, Class<T> clazz) {
        List<Object> list = stringRedisTemplate.opsForList().range(key, start, end);
        List<T> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (Object json : list) {
                resultList.add(JSONUtil.toBean(String.valueOf(json), clazz));
            }
        }
        return resultList;
    }

    /**
     * @param key
     * @param hashKey
     * @param value
     * @return void
     * @description: 存入hash
     */
    public void putHash(String key, String hashKey, Object value) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * @param key
     * @param hashKey
     * @param value
     * @param ttl
     * @param unit
     * @return void
     * @description: 存入hash带过期时间
     */
    public void putHash(String key, String hashKey, Object value, long ttl, TimeUnit unit) {
        stringRedisTemplate.opsForHash().put(key, hashKey, value);
        stringRedisTemplate.opsForHash().getOperations().expire(key, ttl, unit);
    }

    /**
     * @param key
     * @return java.util.Map<java.lang.Object, java.lang.Object>
     * @description: 获取hash
     */
    public Map<Object, Object> getHash(String key) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(key);
        return entries;
    }

    /**
     * @param key
     * @return void
     * @description: 删除
     */
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * @param keys
     * @return void
     * @description: 批量删除
     */
    public void delete(Collection<String> keys) {
        stringRedisTemplate.delete(keys);
    }

    /**
     * @param key
     * @return boolean
     * @description: 检查键是否存在
     */
    public boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * @param key
     * @param delta
     * @return long
     * @description: 自增操作
     */
    public long increment(String key, long delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @param key
     * @param delta
     * @return double
     * @description: 自增操作（浮点数）
     */
    public double increment(String key, double delta) {
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * @param key
     * @param delta
     * @return long
     * @description: 自减操作
     */
    public long decrement(String key, long delta) {
        return stringRedisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * @param key
     * @param members
     * @return void
     * @description: 向Set中添加成员
     */
    public void addToSet(String key, String... members) {
        stringRedisTemplate.opsForSet().add(key, members);
    }

    /**
     * @param key
     * @param member
     * @return boolean
     * @description: 检查Set中是否存在成员
     */
    public boolean isMemberInSet(String key, String member) {
        return stringRedisTemplate.opsForSet().isMember(key, member);
    }

    /**
     * @param key
     * @param members
     * @return void
     * @description: 从Set中移除成员
     */
    public void removeFromSet(String key, String... members) {
        stringRedisTemplate.opsForSet().remove(key, members);
    }

    /**
     * @param key
     * @return void
     * @description: 从列表左侧插入数据
     */
    public void leftPushList(String key, String... values) {
        stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * @param key
     * @return String
     * @description: 从列表左侧弹出数据
     */
    public Object leftPopList(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    /**
     * @param key
     * @return String
     * @description: 从列表右侧弹出数据
     */
    public Object rightPopList(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    /**
     * @param channel
     * @param message
     * @return void
     * @description: 发布消息
     */
    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    /**
     * @param keys
     * @return List<String>
     * @description: 批量获取字符串值
     */
    public List<Object> multiGet(Collection<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * @param keys
     * @return void
     * @description: 批量设置字符串值
     */
    public void multiSet(Map<String, String> keys) {
        stringRedisTemplate.opsForValue().multiSet(keys);
    }

    /**
     * @param key
     * @return long
     * @description: 获取键的剩余过期时间
     */
    public long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    /**
     * @param key
     * @param ttl
     * @param unit
     * @return void
     * @description: 设置键的过期时间
     */
    public void expire(String key, long ttl, TimeUnit unit) {
        stringRedisTemplate.expire(key, ttl, unit);
    }

    /**
     * @param key
     * @return void
     * @description: 持久化键（移除过期时间）
     */
    public void persist(String key) {
        stringRedisTemplate.persist(key);
    }

    /**
     * @param key
     * @param field
     * @return Object
     * @description: 获取Hash中的某个字段值
     */
    public Object getHashField(String key, String field) {
        return stringRedisTemplate.opsForHash().get(key, field);
    }

    /**
     * @param key
     * @return void
     * @description: 删除Hash中的某个字段
     */
    public void deleteHashField(String key, String... fields) {
        stringRedisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * @param key
     * @param field
     * @return boolean
     * @description: 检查Hash中是否存在某个字段
     */
    public boolean hasHashField(String key, String field) {
        return stringRedisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * @param key
     * @return Set<String>
     * @description: 获取Set的所有成员
     */
    public Set<Object> getSetMembers(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    /**
     * @param key
     * @param count
     * @return List<String>
     * @description: 随机获取Set中的成员
     */
    public List<Object> randomSetMembers(String key, long count) {
        return new ArrayList<>(stringRedisTemplate.opsForSet().randomMembers(key, count));
    }

    /**
     * @param key
     * @return long
     * @description: 获取Set的大小
     */
    public long getSetSize(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    /**
     * @param key
     * @return long
     * @description: 获取列表的大小
     */
    public long getListSize(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    /**
     * @param key
     * @return void
     * @description: 清空列表
     */
    public void clearList(String key) {
        stringRedisTemplate.opsForList().trim(key, 0, 0);
    }

    /**
     * @param key
     * @return long
     * @description: 获取Hash的大小
     */
    public long getHashSize(String key) {
        return stringRedisTemplate.opsForHash().size(key);
    }

    /**
     * @param key
     * @return void
     * @description: 清空Hash
     */
    public void clearHash(String key) {
        stringRedisTemplate.opsForHash().delete(key);
    }

    /**
     * @param key
     * @return void
     * @description: 清空Set
     */
    public void clearSet(String key) {
        stringRedisTemplate.opsForSet().remove(key);
    }

    /**
     * @param key
     * @return void
     * @description: 清空字符串
     */
    public void clearString(String key) {
        stringRedisTemplate.delete(key);
    }


    /**
     * @param key
     * @param member
     * @return boolean
     * @description: 往SortedSet中添加成员
     */
    public boolean addToSortedSet(String key, String member, double score) {
        return stringRedisTemplate.opsForZSet().add(key, member, score);
    }

    /**
     * @param key
     * @param member
     * @return Double
     * @description: 获取SortedSet中成员的分数
     */
    public Double getSortedSetScore(String key, String member) {
        return stringRedisTemplate.opsForZSet().score(key, member);
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return Set<String>
     * @description: 获取SortedSet中分数范围内的成员
     */
    public Set<Object> getSortedSetRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * @param key
     * @param members
     * @return Long
     * @description: 删除SortedSet中的成员
     */
    public Long removeFromSortedSet(String key, String... members) {
        return stringRedisTemplate.opsForZSet().remove(key, members);
    }

    /**
     * @param key
     * @return Long
     * @description: 获取SortedSet的大小
     */
    public Long getSortedSetSize(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    /**
     * @param key
     * @return Set<String>
     * @description: 获取SortedSet的所有成员
     */
    public Set<Object> getSortedSetAllMembers(String key) {
        return stringRedisTemplate.opsForZSet().range(key, 0, -1);
    }

    /**
     * @param key
     * @return void
     * @description: 清空SortedSet
     */
    public void clearSortedSet(String key) {
        stringRedisTemplate.opsForZSet().removeRange(key, 0, -1);
    }

    /**
     * 使用事务执行一组 Redis 操作
     *
     * @param operations 一组 Redis 操作
     */
    @Transactional
    public void executeInTransaction(List<RedisCallback<Object>> operations) {

        stringRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.multi();
            for (RedisCallback<Object> operation : operations) {
                operation.doInRedis(connection);
            }
            connection.exec();
            return null;
        });
    }

    /**
     * 清空所有类型的数据（谨慎使用）
     *
     * @param key
     */
    @Transactional
    public void clearAllTypes(String key) {
        stringRedisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushAll();
            return null;
        });
    }

    /**
     * 执行 Lua 脚本
     *
     * @param script Lua 脚本内容
     * @param returnType 脚本返回值类型
     * @param keys Redis 键列表
     * @param args 脚本参数列表
     * @param <T> 返回值类型
     * @return 脚本执行结果
     */
    public <T> T executeScript(String script, Class<T> returnType, List<String> keys, Object... args) {
        RedisScript<T> redisScript = new DefaultRedisScript<>(script, returnType);
        return stringRedisTemplate.execute(redisScript, keys, args);
    }

    /**
     * 执行管道命令
     *
     * @param commands Redis 命令列表
     * @return 执行结果列表
     */
    public List<Object> executePipelineCommands(List<RedisCallback<?>> commands) {
        return stringRedisTemplate.executePipelined((RedisConnection connection) -> {
            for (RedisCallback<?> command : commands) {
                command.doInRedis(connection);
            }
            return null;
        });
    }

    public void demoPipelineUsage() {
        List<RedisCallback<?>> commands = List.of(
                connection -> {
                    connection.set("key1".getBytes(), "value1".getBytes());
                    return null;
                },
                connection -> {
                    connection.set("key2".getBytes(), "value2".getBytes());
                    return null;
                },
                connection -> {
                    return connection.get("key1".getBytes());
                },
                connection -> {
                    return connection.get("key2".getBytes());
                }
        );

        List<Object> results = executePipelineCommands(commands);

        results.forEach(System.out::println);
    }
}