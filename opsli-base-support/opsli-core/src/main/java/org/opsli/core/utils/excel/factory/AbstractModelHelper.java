package org.opsli.core.utils.excel.factory;


import cn.hutool.json.JSONObject;
import org.opsli.api.base.warpper.ApiWrapper;

/**
 * Created Date by 2020/1/11 0011.
 *
 * 抽象 实体助手
 * @author Parker
 */
public abstract class AbstractModelHelper {

    /**
     * 抽象 创建导入对象
     *
     * @param dictJson 字典Json
     * @param wrapper 导入 wrapper
     * @return Object
     */
    abstract public void transformByImport(JSONObject dictJson, ApiWrapper wrapper);

    /**
     * 抽象 创建导出对象
     *
     * @param dictJson 字典Json
     * @param wrapper 导出 wrapper
     * @return Object
     */
    abstract public void transformByExport(JSONObject dictJson, ApiWrapper wrapper);

}
