package org.opsli.core.creater.strategy.sync;

import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.core.creater.strategy.sync
 * @Author: Parker
 * @CreateTime: 2020-11-18 11:47
 * @Description: 同步策略
 */
public interface SyncStrategy {

    /**
     * 获得分类
     * @return
     */
    String getType();

    /**
     * 执行 同步操作
     */
    void execute(CreaterTableAndColumnModel model);

}
