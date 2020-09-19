package org.opsli.api.conf.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 接口路径前缀配置
 * @author parker
 */
@Component
@ConfigurationProperties(prefix = "server.servlet.api.path")
@Data
public class ApiPathProperties {

    String globalPrefix = "api";

}