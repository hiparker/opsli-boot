package org.opsli.common.constants;

/**
 * Order
 * @author parker
 * @date 2020-09-16
 */
public interface OrderConstants {

    /** token */
    int TOKEN_AOP_SORT = 150;

    /** 热点数据加载顺序 */
    int HOT_DATA_ORDER = 180;

    /** 参数非法验证顺序 */
    int PARAM_VALIDATE_AOP_SORT = 185;

    /** SQL 切面执行顺序 */
    int SQL_ORDER = 190;

    /** Controller异常拦截顺序 */
    int EXCEPTION_HANDLER_ORDER = 260;

}
