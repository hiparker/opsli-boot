/**
 * Copyright 2020 OPSLI 快速开发平台 https://www.opsli.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.opsli.core.base.service.interfaces;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.core.base.entity.BaseEntity;
import org.opsli.core.persistence.Page;

import java.util.Collection;
import java.util.List;

/**
 * 增删改查 总接口
 *
 * 对于 增加、修改、删除处理 ===
 *
 *      不需要做锁状态处理
 *      不需要判断是否成功
 *      能往下走的只能是成功
 *      异常问题 已经统一被处理
 *
 * @param <T>
 * @param <E>
 *
 * @author Parker
 * @date 2020-09-15
 */
public interface CrudServiceInterface<T extends BaseEntity, E extends ApiWrapper> extends BaseServiceInterface<T> {


    /**
     * 获得Model Class
     * @return Class<E>
     */
    Class<E> getModelClass();


    /**
     * 获取单条数据
     *
     * @param id ID
     * @return E
     */
    E get(String id);

    /**
     * 获取单条数据
     *
     * @param model model 数据模型
     * @return E
     */
    E get(E model);


    /**
     * 插入数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model model 数据模型
     * @return E
     */
    E insert(E model);

    /**
     * 批量插入数据(批量插入数据 暂不支持更新热数据)
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param models model 数据模型
     * @return E
     */
    boolean insertBatch(List<E> models);


    /**
     * 更新数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model model 数据模型
     * @return E
     */
    E update(E model);


    /**
     * 保存数据
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * 注： 没有ID 则是 新增， 有ID 则默认进行保存
     *
     * @param model model 数据模型
     * @return E
     */
    E save(E model);

    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param id ID
     * @return boolean
     */
    boolean delete(String id);


    /**
     * 删除数据（物理删除，从数据库中彻底删除）
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param model 数据模型
     * @return boolean
     */
    boolean delete(E model);


    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param ids id数组
     * @return boolean
     */
    boolean deleteAll(String[] ids);

    /**
     * 批量物理删除
     *
     * 不需要做锁状态处理
     * 不需要判断是否成功
     * 能往下走的只能是成功
     * 异常问题 已经统一被处理
     *
     * @param models 封装模型
     * @return boolean
     */
    boolean deleteAll(Collection<E> models);


    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件构造器
     * @return List<E>
     */
    List<T> findList(QueryWrapper<T> queryWrapper);


    /**
     * 查询全部数据列表
     *
     * @return List<T>
     */
    List<T> findAllList();

    /**
     * 查询分页数据
     * @param page 分页
     * @return  Page<T>
     */
    Page<T,E> findPage(Page<T,E> page);

    /**
     * 查询分页数据 不查询 count
     * @param page 分页
     * @return  Page<T>
     */
    Page<T,E> findPageNotCount(Page<T,E> page);
    

}





