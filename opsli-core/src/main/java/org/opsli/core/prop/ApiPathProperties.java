package org.opsli.core.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 接口路径前缀配置
 * @author C西
 */
@Component
@ConfigurationProperties(prefix = "server.servlet.api.path")
@Data
public class ApiPathProperties {

    String globalPrefix = "api";

}