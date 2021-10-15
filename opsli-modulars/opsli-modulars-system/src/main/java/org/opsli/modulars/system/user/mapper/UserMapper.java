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
package org.opsli.modulars.system.user.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.opsli.api.wrapper.system.user.UserPassword;
import org.opsli.modulars.system.menu.entity.SysMenu;
import org.opsli.modulars.system.user.entity.SysUser;
import org.opsli.modulars.system.user.entity.SysUserWeb;

import java.util.List;

/**
 * 用户信息 Mapper
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {


    /**
     * 修改密码
     * @param userPassword 账号密码
     * @return boolean
     */
    boolean updatePassword(UserPassword userPassword);

    /**
     * 更新用户最后登录IP
     * @param entity Entity
     * @return boolean
     */
    boolean updateLoginIp(SysUser entity);

    /**
     * 更新用户头像
     * @param entity Entity
     * @return boolean
     */
    boolean updateAvatar(SysUser entity);


    /**
     * 查询集合
     * @param wrapper 条件查询器
     * @return List
     */
    List<SysUserWeb> findList(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
