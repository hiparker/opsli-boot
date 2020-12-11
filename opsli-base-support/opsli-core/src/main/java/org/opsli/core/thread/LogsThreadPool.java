package org.opsli.core.thread;

import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.web.system.logs.LogsApi;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.common.thread.refuse.AsyncProcessQueueReFuse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: tank-design
 * @BelongsPackage: com.parker.tank.net.thread
 * @Author: Parker
 * @CreateTime: 2020-08-21 14:22
 * @Description: 日志保存线程
 */
@Slf4j
@Component
public class LogsThreadPool {


    /** 日志API */
    private static LogsApi logsApi;

    /**
     * 执行
     * @param logsModel
     */
    public static void process(LogsModel logsModel){
        if(logsModel == null){
            return;
        }

        AsyncProcessQueueReFuse.execute(()->{
            // 存储临时 token
            ResultVo<?> ret = logsApi.insert(logsModel);
            if(!ret.isSuccess()){
                log.error(ret.getMsg());
            }
        });
    }


    // ========================

    @Autowired
    public  void setLogsApi(LogsApi logsApi) {
        LogsThreadPool.logsApi = logsApi;
    }

}
