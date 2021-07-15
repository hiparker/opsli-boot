import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.opsli.common.thread.AsyncProcessExecutor;
import org.opsli.common.thread.AsyncProcessExecutorFactory;

import java.util.Map;

/**
 * 限流器测试
 *
 * @author Parker
 * @date 2021-01-05 17:17
 */
public class LimiterTest {

    @Test
    public void test(){
        AsyncProcessExecutor normalExecutor = AsyncProcessExecutorFactory.createNormalExecutor();
        for (int i = 0; i < 1000; i++) {
            normalExecutor.put(()->{
                Map<String,Object> map = Maps.newHashMap();
                map.put("username","demo");
                String ret = HttpUtil.get("http://127.0.0.1:8080/opsli-boot/system/slipCount", map);
                System.out.println(ret);
            });
        }
        normalExecutor.execute();
    }

}
