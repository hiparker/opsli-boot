package org.opsli.core.thread;

import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.result.ResultVo;
import org.opsli.api.thread.factory.NameableThreadFactory;
import org.opsli.api.web.system.logs.LogsApi;
import org.opsli.api.wrapper.system.logs.LogsModel;
import org.opsli.common.api.TokenThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4,
            new NameableThreadFactory("日志保存线程"));


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
        EXECUTOR_SERVICE.submit(()->{
            // 存储临时 token
           try {
               ResultVo<?> ret = logsApi.insert(logsModel);
               if(!ret.isSuccess()){
                   log.error(ret.getMsg());
               }
           }catch (Exception e){
               log.error(e.getMessage(), e);
           }
        });
    }


    // ========================
    @Autowired
    public  void setLogsApi(LogsApi logsApi) {
        LogsThreadPool.logsApi = logsApi;
    }

}
