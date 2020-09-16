-- 解锁脚本
-- 判断是当前线程持有锁，避免解了其他线程加的锁
if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then
   -- 重入次数大于1，扣减次数
   --if tonumber(redis.call('hget',KEYS[1],ARGV[1])) > 1 then
   --    return redis.call('hincrby', KEYS[1], ARGV[1], -1)
   -- 重入次数等于1，删除该锁
   --else
       redis.call('del', KEYS[1]);
       return 1
   --end
-- 判断不是当前线程持有锁，返回解锁失败
else
   return 0
end