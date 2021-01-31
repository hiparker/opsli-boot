import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import org.junit.Test;

/**
 * @author 周鹏程
 * @date 2021-01-31 5:15 下午
 **/
public class EncryptTest {

    @Test
    public void testEncrypt(){


        SM2 sm2 = SmUtil.sm2("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJqTLKryt+SYq8oHZ/PeFR64Pq2A9qsKZmZFzU6gvkQ37JeCFCYMitxmF7AtlktSktB3dPdxHEppbfSdY1+i140eAqMZga7j4FmjIdnyW1OauiVgaimJLAEyE7uTAAPggrU9kdqgXbA8hIffm5tu6DKRq/t1NpYEO42S/IGK/yoxAgMBAAECgYEAiWu+klwm0LxKPdpHuK7/58e1MVst8PHWB6aW2AhgHxX46NlkQE92RGsfNCnTLDPFAkCxZCrTE/SXJJmn9yY2qoS26OV0PbTGajk96M8lDi9JSmWCNV1eywPecObSyvtPd5jaPtq2jkgNY/hHJjH6kV7UAFZuaSK7jxskfq7uR2ECQQDPfmGjPiMc65+LE9U7jC4LokyUi1yCgN6AY5MgF6fkxUVJD2mtl9BqRK7qE0OnsRb0NzID3PSfa7aA2I0Rlsj/AkEAvrXUBQ6hfuEwD1896qpSJUr7tLidby/3jYwSoewuydDT2duDc2ZCz4/U/1NpxSxWT10ZZi2ExsFZn/3PDylczwJARA3oijkcHSUu69eybVh51bkCswnOasNHtwZxv+niWEdXhTH38EbFxcUHNaDh5MNRiwH7dobm+M7EShg8lJNHEwJAclRdU97OkFr9zeliHCGZd4P5XAFlWHfgJ7p2nR4Teqe3qZ6Aspj2qqpmnd7qxOrsn02H4YqeU+0sBs9I56T7XwJAAg8wHrh/FAPY96mAya0bpv6zm/7bave17vs+8B+fhBEHHuvetfv8Xi/RkXL0rjE4LaTHefoUbZPNbIhNYiN0CQ==",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCakyyq8rfkmKvKB2fz3hUeuD6tgParCmZmRc1OoL5EN+yXghQmDIrcZhewLZZLUpLQd3T3cRxKaW30nWNfoteNHgKjGYGu4+BZoyHZ8ltTmrolYGopiSwBMhO7kwAD4IK1PZHaoF2wPISH35ubbugykav7dTaWBDuNkvyBiv8qMQIDAQAB"

        );

        System.out.println(sm2.getPublicKeyBase64());
        System.out.println(sm2.getPrivateKeyBase64());


    }
}
