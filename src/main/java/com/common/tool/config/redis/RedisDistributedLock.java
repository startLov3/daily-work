package com.common.tool.config.redis;

import redis.clients.jedis.Jedis;

/*Java使用Redis来处理多服务器环境下的并发问题*/
public class RedisDistributedLock {

    private String redisHost = "127.0.0.1";
    private int redisPort = 6379;
    private Jedis jedis;

    public RedisDistributedLock() {
        this.jedis = new Jedis(redisHost, redisPort);
    }

    public boolean tryLock(String lockKey, String requestId, int expireTime) {
        String result = jedis.set(lockKey, requestId, "NX", "PX", expireTime);
        return "OK".equals(result);
    }

    public boolean releaseLock(String lockKey, String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, 1, lockKey, requestId);
        return "1".equals(result.toString());
    }

    // 尝试获取锁，带有等待时间
    public boolean tryLockWithWait(String lockKey, String requestId, int expireTime, long waitTime) throws InterruptedException {
        long endTime = System.currentTimeMillis() + waitTime;
        while (System.currentTimeMillis() < endTime) {
            if (tryLock(lockKey, requestId, expireTime)) {
                return true; // 成功获取到锁
            }
            // 短暂休眠后再次尝试，避免频繁请求
            Thread.sleep(100); // 这里的休眠时间可以根据实际情况调整
        }
        return false; // 获取锁超时
    }

    // 使用示例
    public static void main(String[] args) throws InterruptedException {
        RedisDistributedLock lock = new RedisDistributedLock();
        String lockKey = "dataLock"; // 针对特定数据的锁
        String requestId = "UUID或其他唯一标识"; // 用来标识锁的持有者
        int expireTime = 30000; // 锁的过期时间
        long waitTime = 60000; // 最大等待时间

        try {
            // 尝试获取锁，带有等待时间
            if (lock.tryLockWithWait(lockKey, requestId, expireTime, waitTime)) {
                // 处理业务逻辑
                System.out.println("获取锁成功，处理业务逻辑");

                // 处理完业务逻辑后释放锁
                if (lock.releaseLock(lockKey, requestId)) {
                    System.out.println("释放锁成功");
                } else {
                    System.out.println("释放锁失败");
                }
            } else {
                System.out.println("获取锁失败，已超时");
            }
        } finally {
            lock.jedis.close();
        }
    }
}
