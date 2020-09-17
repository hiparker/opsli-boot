package org.opsli.plugins.cache.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.cache.conf
 * @Author: Parker
 * @CreateTime: 2020-09-16 21:35
 * @Description: EhCache
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.enable", havingValue = "true")
public class EhCacheConfig {


}