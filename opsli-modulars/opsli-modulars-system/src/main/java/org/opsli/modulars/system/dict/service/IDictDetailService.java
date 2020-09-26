package org.opsli.modulars.system.dict.service;

import org.opsli.api.wrapper.system.dict.DictDetailModel;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.system.dict.entity.SysDictDetail;

import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-17 13:07
 * @Description: 数据字典 明细 接口
 */
public interface IDictDetailService extends CrudServiceInterface<SysDictDetail, DictDetailModel> {

    /**
     * 根据父类ID 删除
     * @param parentId 父类ID
     * @return
     */
    boolean delByParent(String parentId);

    /**
     * 根据字典类型编号 查询出所有字典
     *
     * @param typeCode 字典类型编号
     * @return
     */
    List<DictDetailModel> findListByTypeCode(String typeCode);

}
