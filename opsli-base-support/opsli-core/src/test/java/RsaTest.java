import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.Data;
import org.junit.Test;
import org.opsli.core.utils.AsymmetricCryptoUtil;

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
        System.out.println(AsymmetricCryptoUtil.INSTANCE.getPublicKey());

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

        Object parse = JSONObject.parse("{\"username\":\"demo\",\"password\":\"Aa123456\",\"captcha\":\"\",\"uuid\":\"0d3eea43edf19e4ed0e88aae8d56878046a5\"}");

        // 加密
        String encryptedData = AsymmetricCryptoUtil.INSTANCE.encryptedData(parse);
        System.out.println(encryptedData);

        // 解密
        String decryptedData = AsymmetricCryptoUtil.INSTANCE.decryptedData(encryptedData);
        Object decryptedDataToObj = AsymmetricCryptoUtil.INSTANCE.decryptedDataToObj(encryptedData);
        System.out.println(decryptedData);

        // 解密
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
