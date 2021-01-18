/**
 * @作者 薛佳琪
 * @创建时间 2020/11/23 11:34
 */
package org.opsli.core.monitor.utils;

import lombok.Data;

import java.io.Serializable;
/**
 * 系统监控
 *
 * @author 薛佳琪
 */
@Data
public class Sys implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器名称
     */
    private String computerName;

    /**
     * 服务器Ip
     */
    private String computerIp;

    /**
     * 项目路径
     */
    private String userDir;

    /**
     * 操作系统
     */
    private String osName;

    /**
     * 系统架构
     */
    private String osArch;
}
