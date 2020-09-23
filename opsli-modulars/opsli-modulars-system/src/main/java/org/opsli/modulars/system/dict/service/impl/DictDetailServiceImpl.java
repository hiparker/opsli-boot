package org.opsli.modulars.system.dict.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.dict.DictModel;
import org.opsli.api.wrapper.system.dict.SysDictDetailModel;
import org.opsli.api.wrapper.system.dict.SysDictModel;
import org.opsli.common.constants.MyBatisConstants;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.HumpUtil;
import org.opsli.common.utils.WrapperUtil;
import org.opsli.core.base.service.impl.CrudServiceImpl;
import org.opsli.core.cache.pushsub.enums.CacheType;
import org.opsli.core.cache.pushsub.msgs.DictMsgFactory;
import org.opsli.core.persistence.querybuilder.GenQueryBuilder;
import org.opsli.core.persistence.querybuilder.QueryBuilder;
import org.opsli.core.utils.DictUtil;
import org.opsli.modulars.system.SystemMsg;
import org.opsli.modulars.system.dict.entity.SysDictDetail;
import org.opsli.modulars.system.dict.mapper.DictDetailMapper;
import org.opsli.modulars.system.dict.service.IDictDetailService;
import org.opsli.modulars.system.dict.service.IDictService;
import org.opsli.plugins.redis.RedisPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.test.service
 * @Author: Parker
 * @CreateTime: 2020-09-16 17:34
 * @Description: 数据字典 明细 接口实现类
 */
@Service
public class DictDetailServiceImpl extends CrudServiceImpl<DictDetailMapper, SysDictDetailModel, SysDictDetail> implements IDictDetailService {

    @Autowired(required = false)
    private DictDetailMapper mapper;
    @Autowired
    private IDictService iDictService;
    @Autowired
    private RedisPlugin redisPlugin;

    /**
     * 新增
     * @param model model 数据模型
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDictDetailModel insert(SysDictDetailModel model) {

        SysDictDetail entity = WrapperUtil.transformInstance(model, SysDictDetail.class);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByNameOrValue(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEL_DICT_UNIQUE);
        }

        SysDictDetailModel ret = super.insert(model);
        if(ret != null){
            DictModel dictModel = new DictModel();
            dictModel.setTypeCode(model.getTypeCode());
            dictModel.setDictName(model.getDictName());
            dictModel.setDictValue(model.getDictValue());
            dictModel.setModel(ret);
            // 先删老缓存
            DictUtil.del(dictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(dictModel, CacheType.DELETE)
            );

            // 再存 防止脏数据
            DictUtil.put(dictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(dictModel, CacheType.UPDATE)
            );
        }

        return ret;
    }

    /**
     * 修改
     * @param model model 数据模型
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysDictDetailModel update(SysDictDetailModel model) {

        SysDictDetail entity = WrapperUtil.transformInstance(model, SysDictDetail.class);
        // 唯一验证
        Integer count = mapper.uniqueVerificationByNameOrValue(entity);
        if(count != null && count > 0){
            // 重复
            throw new ServiceException(SystemMsg.EXCEL_DICT_DETAIL_UNIQUE);
        }

        // 旧数据 用于删除老缓存
        SysDictDetailModel oldModel = this.get(model);

        SysDictDetailModel ret = super.update(model);
        if(ret != null){
            // 先删老缓存
            DictModel oldDictModel = new DictModel();
            oldDictModel.setTypeCode(oldModel.getTypeCode());
            oldDictModel.setDictName(oldModel.getDictName());
            oldDictModel.setDictValue(oldModel.getDictValue());
            DictUtil.del(oldDictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(oldDictModel, CacheType.DELETE)
            );


            // 再put新缓存
            DictModel dictModel = new DictModel();
            dictModel.setTypeCode(model.getTypeCode());
            dictModel.setDictName(model.getDictName());
            dictModel.setDictValue(model.getDictValue());
            dictModel.setModel(ret);
            DictUtil.put(dictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(dictModel, CacheType.UPDATE)
            );
        }

        return ret;
    }


    /**
     * 删除
     * @param id ID
     * @return
     */
    @Override
    public boolean delete(String id) {
        SysDictDetailModel sysDictDetailModel = this.get(id);
        boolean ret = super.delete(id);
        if(ret){
            DictModel dictModel = new DictModel();
            dictModel.setTypeCode(sysDictDetailModel.getTypeCode());
            dictModel.setDictName(sysDictDetailModel.getDictName());
            dictModel.setDictValue(sysDictDetailModel.getDictValue());
            // 删除缓存
            DictUtil.del(dictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(dictModel, CacheType.DELETE)
            );
        }
        return ret;
    }

    /**
     * 删除
     * @param model 数据模型
     * @return
     */
    @Override
    public boolean delete(SysDictDetailModel model) {
        SysDictDetailModel sysDictDetailModel = this.get(model);
        boolean ret = super.delete(model);
        if(ret){
            DictModel dictModel = new DictModel();
            dictModel.setTypeCode(sysDictDetailModel.getTypeCode());
            dictModel.setDictName(sysDictDetailModel.getDictName());
            dictModel.setDictValue(sysDictDetailModel.getDictValue());
            // 删除缓存
            DictUtil.del(dictModel);
            // 广播缓存数据 - 通知其他服务器同步数据
            redisPlugin.sendMessage(
                    DictMsgFactory.createMsg(dictModel, CacheType.DELETE)
            );
        }
        return ret;
    }

    /**
     * 删除 - 多个
     * @param ids id数组
     * @return
     */
    @Override
    public boolean deleteAll(String[] ids) {
        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        List<?> idList = Convert.toList(ids);
        queryWrapper.in(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);
        List<SysDictDetail> list = this.findList(queryWrapper);
        boolean ret = super.deleteAll(ids);

        if(ret){
            if(list != null && list.size() > 0){
                List<DictModel> dictModels = Lists.newArrayListWithCapacity(list.size());
                // 删除缓存
                for (SysDictDetail sysDictDetail : list) {
                    DictModel dictModel = new DictModel();
                    dictModel.setTypeCode(sysDictDetail.getTypeCode());
                    dictModel.setDictName(sysDictDetail.getDictName());
                    dictModel.setDictValue(sysDictDetail.getDictValue());
                    // 删除缓存
                    DictUtil.del(dictModel);
                    dictModels.add(dictModel);
                }
                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        DictMsgFactory.createMsg(dictModels, CacheType.DELETE)
                );
            }

        }
        return ret;
    }

    /**
     * 删除 - 多个
     * @param models 封装模型
     * @return
     */
    @Override
    public boolean deleteAll(Collection<SysDictDetailModel> models) {

        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();

        List<String> idList = Lists.newArrayListWithCapacity(models.size());
        for (SysDictDetailModel model : models) {
            idList.add(model.getId());
        }
        queryWrapper.in(HumpUtil.humpToUnderline(MyBatisConstants.FIELD_ID),idList);

        List<SysDictDetail> list = this.findList(queryWrapper);

        boolean ret = super.deleteAll(models);

        if(ret){
            if(list != null && list.size() > 0){
                List<DictModel> dictModels = Lists.newArrayListWithCapacity(list.size());
                // 删除缓存
                for (SysDictDetail sysDictDetail : list) {
                    DictModel dictModel = new DictModel();
                    dictModel.setTypeCode(sysDictDetail.getTypeCode());
                    dictModel.setDictName(sysDictDetail.getDictName());
                    dictModel.setDictValue(sysDictDetail.getDictValue());
                    // 删除缓存
                    DictUtil.del(dictModel);
                    dictModels.add(dictModel);
                }
                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        DictMsgFactory.createMsg(dictModels, CacheType.DELETE)
                );
            }
        }

        return ret;
    }

    /**
     * 根据 父类ID 全部删除
     * @param parentId 父类ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delByParent(String parentId) {
        if(StringUtils.isEmpty(parentId)) return false;

        String key = HumpUtil.humpToUnderline("typeId");
        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, parentId);
        boolean removeFlag = super.remove(queryWrapper);
        if(removeFlag){
            SysDictModel sysDictModel = iDictService.get(parentId);
            List<SysDictDetailModel> listByTypeCode = this.findListByTypeCode(sysDictModel.getTypeCode());
            if(listByTypeCode != null && listByTypeCode.size() > 0){
                List<DictModel> dictList = Lists.newArrayListWithCapacity(listByTypeCode.size());
                for (SysDictDetailModel sysDictDetailModel : listByTypeCode) {
                    DictModel dictModel = new DictModel();
                    dictModel.setTypeCode(sysDictDetailModel.getTypeCode());
                    dictModel.setDictName(sysDictDetailModel.getDictName());
                    dictModel.setDictValue(sysDictDetailModel.getDictValue());
                    dictList.add(dictModel);
                }
                // 删除缓存
                DictUtil.delAll(sysDictModel.getTypeCode());
                // 广播缓存数据 - 通知其他服务器同步数据
                redisPlugin.sendMessage(
                        DictMsgFactory.createMsg(dictList, CacheType.DELETE)
                );
            }
        }
        return removeFlag;
    }

    /**
     * 根据字典编号 查询出所有字典
     *
     * @param typeCode 字典编号
     * @return
     */
    @Override
    public List<SysDictDetailModel> findListByTypeCode(String typeCode) {
        if(StringUtils.isEmpty(typeCode)) return null;

        String key = HumpUtil.humpToUnderline("typeCode");
        String deleted = HumpUtil.humpToUnderline("deleted");

        QueryBuilder<SysDictDetail> queryBuilder = new GenQueryBuilder<>();
        QueryWrapper<SysDictDetail> queryWrapper = queryBuilder.build();
        queryWrapper.eq(key, typeCode);
        queryWrapper.eq(deleted, '0');
        List<SysDictDetail> list = this.findList(queryWrapper);
        // 转化对象
        return WrapperUtil.transformInstance(list, SysDictDetailModel.class);
    }
}


