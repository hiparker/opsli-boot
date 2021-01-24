import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.Test;
import org.opsli.core.utils.EncryptAndDecryptByRsaUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rsa 加密认证
 */
public class RsaTest {


    /**
     * 加密数据
     */
    @Test
    public void getPublicKey(){
        System.out.println(EncryptAndDecryptByRsaUtil.INSTANCE.getPublicKey());

    }

    /**
     * 加密数据
     */
    @Test
    public void test(){
        Map<String, Object> map = Maps.newHashMap();
        map.put("test1", 123);
        map.put("test2", "aaa");

//        Test1 t1 = new Test1();
//        t1.setId( "123");
//        t1.setName( "张三");
//        t1.setAge( 16);

        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(111);

        Object parse = JSONObject.parse("{\"username\":\"demo\",\"password\":\"Aa123456\",\"captcha\":\"\",\"uuid\":\"0d3eea43edf19e4ed0e88aae8d56878046a5\"}");

        // 加密
        String encryptedData = EncryptAndDecryptByRsaUtil.INSTANCE.encryptedData(list);
        System.out.println(encryptedData);

        // 解密
        String decryptedData = EncryptAndDecryptByRsaUtil.INSTANCE.decryptedData(encryptedData);
        Object decryptedDataToObj = EncryptAndDecryptByRsaUtil.INSTANCE.decryptedDataToObj(encryptedData);
        System.out.println(decryptedData);

        // 解密
        List<Integer> integers = Convert.toList(Integer.class, decryptedDataToObj);
        //Map<String, Object> stringObjectMap = Convert.toMap(String.class, Object.class, decryptedData);
        //Map<String, Object> stringObjectMap = Convert.toMap(String.class, Object.class, decryptedDataToObj);

        System.out.println(123);

    }



}

@Data
class Test1 {

    private String id;

    private String name;

    private Integer age;

}
