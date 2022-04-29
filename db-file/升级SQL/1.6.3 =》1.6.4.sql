SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

update sys_menu
set parent_ids = CONCAT_WS(',',  0, id)
where parent_id = 0;

update sys_menu pp
    left join sys_menu p on p.id = pp.parent_id
    set
        pp.parent_ids = CONCAT_WS(',',  p.parent_ids, pp.id)
where p.parent_id = 0;

update sys_menu ppp
    left join sys_menu pp on pp.id = ppp.parent_id
    left join sys_menu p on p.id = pp.parent_id
    set
        ppp.parent_ids = CONCAT_WS(',',  pp.parent_ids, ppp.id)
where p.parent_id = 0;


update sys_menu pppp
    left join sys_menu ppp on ppp.id = pppp.parent_id
    left join sys_menu pp on pp.id = ppp.parent_id
    left join sys_menu p on p.id = pp.parent_id
    set
        pppp.parent_ids = CONCAT_WS(',',  ppp.parent_ids, pppp.id)
where p.parent_id = 0;



update sys_menu ppppp
    left join sys_menu pppp on pppp.id = ppppp.parent_id
    left join sys_menu ppp on ppp.id = pppp.parent_id
    left join sys_menu pp on pp.id = ppp.parent_id
    left join sys_menu p on p.id = pp.parent_id
    set
        ppppp.parent_ids = CONCAT_WS(',',  pppp.parent_ids, ppppp.id)
where p.parent_id = 0;


SET FOREIGN_KEY_CHECKS = 1;
