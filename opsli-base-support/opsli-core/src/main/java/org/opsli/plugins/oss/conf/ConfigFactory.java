package org.opsli.plugins.oss.conf;


/**
 * 获得配置信息 接口
 *
 * @author Parker
 * @date 2021年5月7日17:39:32
 */
public interface ConfigFactory<Conf> {

    /**
     * 获得配置信息
     * @return T
     */
     Conf getConfig();

}
