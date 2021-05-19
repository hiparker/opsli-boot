package org.opsli.core.autoconfigure.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 获得数据源工具类
 *
 * @author Parker
 * @date 2020-09-15
 */
@Slf4j
@Configuration
public class DbSourceProperties {

    /** 数据库详情 */
    private Map<String, DbSourceProperties.DataSourceInfo> dataSourceInfoMap;

    /**
     * 获得数据库详情
     * @return DataSourceInfo
     */
    public Map<String, DbSourceProperties.DataSourceInfo> getDataSourceInfoMap() {
        return this.dataSourceInfoMap;
    }

    // ==================================

    @Data
    public static class DataSourceInfo{
        /** URL */
        private String url;

        /** 地址 */
        private String host;

        /** 端口 */
        private Integer port;

        /** 数据库名 */
        private String dbName;

        /** 用户名 */
        private String userName;

        /** 密码 */
        private String passWord;

        /** 驱动类名称 */
        private String driverClassName;
    }

    @Autowired
    public void setDataSourceInfoMap(DynamicDataSourceProperties dataSourceProperties) {
        if(dataSourceProperties != null){
            Map<String, DataSourceProperty> datasourceMap = dataSourceProperties.getDatasource();
            if(CollUtil.isNotEmpty(datasourceMap)){
                dataSourceInfoMap = Maps.newTreeMap();
                for (Map.Entry<String, DataSourceProperty> dataSourcePropertyEntry : datasourceMap.entrySet()) {
                    String key = dataSourcePropertyEntry.getKey();
                    DataSourceProperty datasource = dataSourcePropertyEntry.getValue();

                    String url = datasource.getUrl();
                    String username = datasource.getUsername();
                    String password = datasource.getPassword();
                    String driverClassName = datasource.getDriverClassName();


                    // 非法判断
                    if(StringUtils.isBlank(url) || StringUtils.isBlank(username) ||
                            StringUtils.isBlank(password)
                    ){
                        return;
                    }

                    String[] split = url.split(":");
                    String host = String.format("%s:%s:%s", split[0], split[1], split[2]);
                    String[] portSplit = split[3].split("/");
                    String port = portSplit[0];

                    String[] databaseSplit = portSplit[1].split("\\?");
                    String dbName = databaseSplit[0];

                    DataSourceInfo dataSourceInfo = new DataSourceInfo();
                    dataSourceInfo.setUrl(url);
                    dataSourceInfo.setHost(host);
                    dataSourceInfo.setPort(Convert.toInt(port));
                    dataSourceInfo.setDbName(dbName);
                    dataSourceInfo.setUserName(username);
                    dataSourceInfo.setPassWord(password);
                    dataSourceInfo.setDriverClassName(driverClassName);

                    dataSourceInfoMap.put(key, dataSourceInfo);
                }
            }
        }
    }
}
