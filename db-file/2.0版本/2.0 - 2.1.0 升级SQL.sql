-- 删除vue2 组件菜单
delete from sys_menu
where parent_ids like '0,1314068325453574145,1314071137365307394%'
   or parent_ids like '0,1314068325453574145,1314123690283114498%';


-- 清理用户权限垃圾数据
delete ref from sys_role_menu_ref ref
left join sys_menu m on m.id = ref.menu_id
where m.id is null;
