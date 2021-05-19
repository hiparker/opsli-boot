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
package org.opsli.modulars.system.menu.service;

import org.opsli.api.wrapper.system.menu.MenuFullModel;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.core.base.entity.HasChildren;
import org.opsli.core.base.service.interfaces.CrudServiceInterface;
import org.opsli.modulars.system.menu.entity.SysMenu;

import java.util.List;
import java.util.Set;


/**
 * 菜单表 Service
 *
 * @author Parker
 * @date 2020-09-16 17:33
 */
public interface IMenuService extends CrudServiceInterface<SysMenu, MenuModel> {


    /**
     * 根据菜单权限 获得菜单
     * @param permissions 权限
     * @return MenuModel
     */
    MenuModel getByPermissions(String permissions);


    /**
     * 是否有下级
     * @param parentIds 父节点Id Set
     * @return List
     */
    List<HasChildren> hasChildren(Set<String> parentIds);

    /**
     * 是否有下级 - 选择控件
     * @param parentIds 父节点Id Set
     * @return List
     */
    List<HasChildren> hasChildrenByChoose(Set<String> parentIds);

    /**
     * 保存完成菜单
     * @param menu 菜单
     */
    void saveMenuByFull(MenuFullModel menu);

}
