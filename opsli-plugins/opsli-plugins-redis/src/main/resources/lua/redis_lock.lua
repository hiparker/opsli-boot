-- 加锁脚本
-- key1：要加锁的名称 argv1:当前线程或主机的地址 argv2：锁存活的时间ms 
local expire_time = tonumber(ARGV[2])
if redis.call('exists', KEYS[1]) == 0 then
   -- 锁不存在，创建一把锁，存入hash类型的值
   redis.call('hset', KEYS[1], ARGV[1], 1)
   -- 设置锁的存活时间，防止死锁
   redis.call('pexpire', KEYS[1], expire_time)
   return 1
end
if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
   -- 表示是同一线程重入
   redis.call('hincrby', KEYS[1], ARGV[1], 1)
   -- 重新设置锁的过期时间
   redis.call('pexpire', KEYS[1], expire_time)
   return 1
end
-- 没抢到锁，返回失败
return 0