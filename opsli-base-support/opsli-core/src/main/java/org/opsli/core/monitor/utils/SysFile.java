/**
 * @作者 薛佳琪
 * @创建时间 2020/11/23 11:36
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
public class SysFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 盘符路径
     */
    private String dirName;

    /**
     * 盘符类型
     */
    private String sysTypeName;

    /**
     * 文件类型
     */
    private String typeName;

    /**
     * 总大小
     */
    private String total;

    /**
     * 剩余大小
     */
    private String free;

    /**
     * 已经使用量
     */
    private String used;

    /**
     * 资源的使用率
     */
    private double usage;
}
