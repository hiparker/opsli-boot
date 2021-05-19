package org.opsli.plugins.waf.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * 软防火墙 配置
 *
 * @author Parker
 * @date 2020-12-19
 */
@ConfigurationProperties(prefix = WafProperties.PROP_PREFIX)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WafProperties {

    public static final String PROP_PREFIX = "opsli.waf";


    /** 是否生效 */
    private boolean enable;

    /** xss 过滤  */
    private boolean xssFilter;

    /** sql 过滤 */
    private boolean sqlFilter;

    /** 过滤器需要过滤的路径 */
    private Set<String> urlPatterns;

    /** 过滤器需要排除过滤的路径 */
    private Set<String> urlExclusion;

    /** 过滤器的优先级，值越小优先级越高 */
    private int order;

}
