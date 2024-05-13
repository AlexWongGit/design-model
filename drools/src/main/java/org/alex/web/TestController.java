package org.alex.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/drools")
public class TestController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStack() {
        String lockKey = "lock:product:101";
        //setnx(v,x)
        //防止并发操作下，锁加完时被另一个请求删掉的风险，锁一致失效的风险
        String clientId = UUID.randomUUID().toString();
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 10, TimeUnit.SECONDS);
        if (!result) {
            return "fail";
        }


        try {
            //redis
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int realStock = stock - 1;
                //jedis.set(key,value)
                stringRedisTemplate.opsForValue().set("stock", realStock + "");
                System.out.println("扣减库存成功");
            } else {
                System.out.println("扣减失败，库存不足");
            }
        } finally {
            //释放锁
            if (clientId.equals(stringRedisTemplate.opsForValue().get(lockKey))) {
                //如果在判断时卡顿，锁正好超时，这里依旧会删除掉别的线程加的锁，所以这两步要保证原子性
                stringRedisTemplate.delete(lockKey);
            }
        }

        return "end";
    }

    //锁延期方案（锁自动续期）：解决过期时间的问题，检查线程是否结束，如果没结束就自动续期
    //redisson

    //zookeeper,树形存储，支持集群,子节点收到可以会给主节点发一个响应，主节点会判定加锁成功的数量，
    //zookeeper选举机制ZAB，会选举有锁的为主节点

    //分段锁思路 300库存分成10个30（分段存储）
}
