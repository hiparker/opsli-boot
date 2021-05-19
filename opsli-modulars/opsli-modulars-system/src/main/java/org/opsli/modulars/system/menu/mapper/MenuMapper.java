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
package org.opsli.modulars.system.menu.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.modulars.system.menu.entity.SysMenu;

import java.util.List;

/**
 * 菜单表 Mapper
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {

    /**
     * 是否有下级
     * @param wrapper 条件查询器
     * @return List
     */
    List<HasChildren> hasChildren(@Param(Constants.WRAPPER) Wrapper<?> wrapper);



}
