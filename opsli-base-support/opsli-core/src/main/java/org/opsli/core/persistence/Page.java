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
package org.opsli.core.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageSerializable;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.opsli.api.base.warpper.ApiWrapper;
import org.opsli.core.base.entity.BaseEntity;

import java.util.List;

/**
 * 分页类
 *
 * @param <T>
 * @param <E>
 * @author Parker
 * @date 2020-09-21 23:57
 */
@Slf4j
public class Page<T extends BaseEntity,E extends ApiWrapper> extends PageSerializable<E>{

    private int pageNo = 1;
    private int pageSize = 10;
    /** 查询条件构造器 */
    private QueryWrapper<T> queryWrapper;

    public Page(){
        super();
    }

    /**
     * 分页 构造函数
     * @param pageNo 页
     * @param pageSize 分页大小
     */
    public Page(int pageNo, int pageSize) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    /**
     * 分页 构造函数
     * @param pageNo 页
     * @param pageSize 分页大小
     * @param queryWrapper 查询条件
     */
    public Page(int pageNo, int pageSize, QueryWrapper<T> queryWrapper) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.queryWrapper = queryWrapper;
    }

    /**
     * 分页函数
     */
    public void pageHelperBegin(){
        this.pageHelperBegin(true);
    }

    /**
     * 分页函数 不统计 count
     */
    public void pageHelperBegin(boolean isByCount){
        PageHelper.startPage(this.pageNo,this.pageSize, isByCount);
    }

    /**
     * 分页函数
     */
    public void pageHelperEnd(){
        PageHelper.clearPage();
    }


    /**
     * 设置数据
     * @param pageInfo 分页信息
     */
    public void instance(PageInfo<T> pageInfo, List<E> list) {
        super.setList(list);
        super.setTotal(pageInfo.getTotal());
    }


    /**
     * 获取bootstrap data分页数据
     * @return map对象
     */
    public Page.PageData getPageData(){
        Page.PageData pageData = new PageData();
        pageData.setRows(this.getList());
        pageData.setTotal(this.getTotal());
        return pageData;
    }


    // =======================================================

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public QueryWrapper<T> getQueryWrapper() {
        return queryWrapper;
    }

    public void setQueryWrapper(QueryWrapper<T> queryWrapper) {
        this.queryWrapper = queryWrapper;
    }


    //////////////////////////////////////////////////////////

    /**
     * 分页对象
     */
    @Data
    public static class PageData {

        /** 条数 */
        private Long total;

        /** 行 */
        private List<?> rows;

    }

}
