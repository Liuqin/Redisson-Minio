
package org.qin.base.annotate;

@Component
public class RedisLuaLock {
    @Autowired
    private StringRedisTemplate template;

    private static final Long RELEASE_SUCCESS = 1L;

    private static final long DEFAULT_TIMEOUT = 1000 * 10;

    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 尝试获取锁 立即返回
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public boolean lock(String key, String value, long timeout) {
        return template.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 以阻塞方式的获取锁
     *
     * @param key
     * @param value
     * @param timeout
     * @return
     */
    public boolean lockBlock(String key, String value, long timeout) {
        long start = System.currentTimeMillis();
        while (true) {
            //检测是否超时
            if (System.currentTimeMillis() - start > timeout) {
                return false;
            }
            //执行set命令
            //1
            Boolean absent = template.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MILLISECONDS);
            //其实没必要判NULL，这里是为了程序的严谨而加的逻辑
            if (absent == null) {
                return false;
            }
            //是否成功获取锁
            if (absent) {
                return true;
            }
        }
    }

    public boolean unlock(String key, String value) {
        // 使用Lua脚本：先判断是否是自己设置的锁，再执行删除
        // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
        // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常EvalSha is not supported in cluster environment.，所以只能拿到原redis的connection来执行脚本

        List<String> keys = new ArrayList<>();
        keys.add(key);
        List<String> args = new ArrayList<>();
        args.add(value);
        Long result = template.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }

                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            }
        });

        //返回最终结果
        return RELEASE_SUCCESS.equals(result);
    }
}