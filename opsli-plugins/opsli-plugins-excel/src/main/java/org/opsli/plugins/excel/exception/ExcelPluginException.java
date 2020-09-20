package org.opsli.plugins.excel.exception;

import org.opsli.common.base.msg.BaseMsg;
import org.opsli.common.exception.ServiceException;

/**
 * Created Date by 2020/5/9 0009.
 *
 * Excel 异常类
 * @author Parker
 */
public class ExcelPluginException extends ServiceException {

    public ExcelPluginException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public ExcelPluginException(BaseMsg msg) {
        super(msg);
    }

}
