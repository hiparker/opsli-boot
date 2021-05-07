import cn.hutool.http.HttpUtil;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.opsli.common.thread.refuse.AsyncProcessQueueReFuse;

import java.util.Map;

/**
 * @BelongsProject: think-bboss-parent
 * @BelongsPackage: PACKAGE_NAME
 * @Author: Parker
 * @CreateTime: 2021-01-05 17:17
 * @Description: 限流器测试
 */
public class LimiterTest {

    @Test
    public void test(){
        for (int i = 0; i < 1000; i++) {
            AsyncProcessQueueReFuse.execute(()->{
                Map<String,Object> map = Maps.newHashMap();
                map.put("username","demo");
                String ret = HttpUtil.get("http://127.0.0.1:8080/opsli-boot/system/slipCount", map);
                System.out.println(ret);
            });
        }
    }

}
