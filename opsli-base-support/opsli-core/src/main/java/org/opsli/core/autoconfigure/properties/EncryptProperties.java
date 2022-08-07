package org.opsli.core.autoconfigure.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 加密配置加载
 *
 * @author Parker
 * @date 2022-08-07
 */
@Configuration
@ConfigurationProperties(prefix = EncryptProperties.PROP_PREFIX)
@Data
@EqualsAndHashCode(callSuper = false)
public class EncryptProperties {

    public static final String PROP_PREFIX = "opsli.data-encrypt";

    /** 秘钥 */
    private String key;

}
