package org.opsli.core.persistence;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageSerializable;

import java.util.HashMap;
import java.util.Map;

public class Page<E,T> extends PageSerializable<E>{


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
     * 分页 构造函数
     * @param pageNo 页
     * @param pageSize 分页大小
     */
    public Page(int pageNo, int pageSize, String orderBy) {
        super();
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }



    /**
     * 分页函数
     */
    public void pageHelperBegin(){
        PageHelper.startPage(this.pageNo,this.pageSize);
    }

    /**
     * 分页函数
     */
    public void pageHelperEnd(){
        PageHelper.clearPage();
    }


    /**
     * 设置数据
     * @param pageInfo
     */
    public void instance(PageInfo<E> pageInfo) {
        super.setList(pageInfo.getList());
        super.setTotal(pageInfo.getTotal());
    }


    /**
     * 获取bootstrap data分页数据
     * @return map对象
     */
    public Map<String, Object> getBootstrapData(){
        Map<String, Object> map = new HashMap<>();
        map.put("rows", this.getList());
        map.put("total", this.getTotal());
        return map;
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

}
