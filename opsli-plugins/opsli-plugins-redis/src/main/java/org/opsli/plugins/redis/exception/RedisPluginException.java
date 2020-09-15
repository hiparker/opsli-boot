package org.opsli.plugins.redis.exception;

import org.opsli.common.base.msg.BaseMsg;
import org.opsli.common.exception.ServiceException;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.plugins.mail.exception
 * @Author: Parker
 * @CreateTime: 2020-09-14 18:44
 * @Description: Redis 异常
 */
public class RedisPluginException extends ServiceException {

    public RedisPluginException(Integer code, String errorMessage) {
        super(code, errorMessage);
    }

    public RedisPluginException(BaseMsg msg) {
        super(msg);
    }
}
