package org.opsli.modulars.system.menu.factory;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.opsli.api.wrapper.system.menu.MenuFullModel;
import org.opsli.api.wrapper.system.menu.MenuModel;
import org.opsli.common.enums.DictType;

import java.util.List;

/**
 * 菜单生成工厂
 *
 * @author Parker
 * @date 2021-05-04 7:15
 **/
public enum MenuFactory {

    /** 实例 */
    INSTANCE;

    /** 菜单权限分割符 */
    private final static String MENU_DELIMITER = "_";
    /** 菜单按钮JSON 数组 */
    private final List<MenuBtn> menuBtnList;

    MenuFactory(){
        menuBtnList = Lists.newArrayList();

        menuBtnList.add(
                MenuBtn.builder()
                        .title("查看")
                        .permission("select")
                        .build()
        );


        menuBtnList.add(
                MenuBtn.builder()
                        .title("新增")
                        .permission("insert")
                        .build()
        );

        menuBtnList.add(
                MenuBtn.builder()
                        .title("修改")
                        .permission("update")
                        .build()
        );

        menuBtnList.add(
                MenuBtn.builder()
                        .title("删除")
                        .permission("delete")
                        .build()
        );

        menuBtnList.add(
                MenuBtn.builder()
                        .title("导入")
                        .permission("import")
                        .build()
        );

        menuBtnList.add(
                MenuBtn.builder()
                        .title("导出")
                        .permission("export")
                        .build()
        );
    }

    /**
     * 生成菜单模型集合
     * @param model 生成模型
     * @return MenuModel
     */
    public MenuModel createMenu(MenuFullModel model){
        if(model == null){
            return new MenuModel();
        }

        // 标题
        String title = model.getTitle();
        // 子模块名
        String subModuleName = model.getSubModuleName();

        MenuModel menu = new MenuModel();
        menu.setParentId(model.getParentId());
        menu.setMenuName(title);
        menu.setAlwaysShow(DictType.NO_YES_NO.getValue());
        menu.setHidden(DictType.NO_YES_NO.getValue());
        menu.setUrl(subModuleName);
        menu.setType(DictType.MENU_MENU.getValue());
        menu.setSortNo(1);
        menu.setLabel(DictType.MENU_LABEL_SYSTEM.getValue());
        menu.setComponent("views/modules/"+model.getModuleName()+"/"+model.getSubModuleName()+"/index");

        return menu;
    }


    /**
     * 生成菜单按钮
     * @param model 生成模型
     * @return MenuModel
     */
    public List<MenuModel> createMenuBtnList(MenuFullModel model){
        if(model == null){
            return ListUtil.empty();
        }

        List<MenuModel> menuBtnModelList = Lists.newArrayListWithCapacity(menuBtnList.size());
        for (int i = 0; i < menuBtnList.size(); i++) {
            MenuBtn menuBtn = menuBtnList.get(i);
            MenuModel menuBtnModel = this.createMenuBtn(model.getParentId(), model, menuBtn);
            menuBtnModel.setSortNo(i+1);
            menuBtnModelList.add(menuBtnModel);
        }
        return menuBtnModelList;
    }

    /**
     * 生成菜单按钮
     * @param menuParentId 上级菜单ID
     * @param model 生成模型
     * @param menuBtn 菜单按钮
     * @return MenuModel
     */
    private MenuModel createMenuBtn(String menuParentId, MenuFullModel model, MenuBtn menuBtn){
        // 标题
        String title = menuBtn.getTitle();
        // 模块名
        String moduleName = model.getModuleName();
        // 子模块名
        String subModuleName = model.getSubModuleName();
        // 权限
        String permission = menuBtn.getPermission();

        MenuModel menu = new MenuModel();
        menu.setParentId(menuParentId);
        menu.setMenuName(title);
        menu.setAlwaysShow(DictType.NO_YES_NO.getValue());
        menu.setHidden(DictType.NO_YES_NO.getValue());
        menu.setType(DictType.MENU_BUTTON.getValue());
        menu.setPermissions(
                this.createPermission(moduleName, subModuleName, permission)
        );
        menu.setSortNo(1);

        return menu;
    }

    /**
     * 生成权限
     * @param moduleName 模块名
     * @param subModuleName 子模块名
     * @param permission 权限
     * @return String
     */
    private String createPermission(String moduleName, String subModuleName, String permission){
        StringBuilder permissionSb = new StringBuilder();
        if(StringUtils.isNotEmpty(moduleName)){
            permissionSb.append(moduleName).append(MENU_DELIMITER);
        }
        if(StringUtils.isNotEmpty(subModuleName)){
            permissionSb.append(subModuleName).append(MENU_DELIMITER);
        }
        permissionSb.append(permission);
        return permissionSb.toString();
    }


    /**
     * 菜单按钮
     */
    @Data
    @Builder
    private static class MenuBtn{

        /** 标题 */
        private String title;

        /** 权限 */
        private String permission;

    }
}
