package org.opsli.core.creater.strategy.sync;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.opsli.common.exception.ServiceException;
import org.opsli.common.utils.Props;
import org.opsli.core.creater.msg.CreaterMsg;
import org.opsli.core.creater.strategy.sync.mysql.entity.FieldTypeAttribute;
import org.opsli.core.creater.strategy.sync.mysql.enums.MySQLSyncColumnType;
import org.opsli.core.waf.util.SQLFilterKit;
import org.opsli.modulars.creater.column.wrapper.CreaterTableColumnModel;
import org.opsli.modulars.creater.general.actuator.SQLActuator;
import org.opsli.modulars.creater.table.service.ITableService;
import org.opsli.modulars.creater.table.wrapper.CreaterTableAndColumnModel;
import org.opsli.modulars.creater.table.wrapper.CreaterTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: opsli-boot
 * @BelongsPackage: org.opsli.modulars.creater.strategy.sync.mysql
 * @Author: Parker
 * @CreateTime: 2020-11-18 13:21
 * @Description: MySQL 同步构建器
 */
@Service
@Transactional(readOnly = true)
public class MySQLSyncBuilder implements SyncStrategy {

    /** 数据库引擎 */
    private static final String ENGINE = "InnoDB";
    /** 字符格式 */
    private static final String CHARSET = "utf8mb4";

    /** 是 */
    private static final char YES = '1';
    /** 否 */
    private static final char NO = '0';
    /** 排除表 */
    private static final List<String> EXCLUDE_TABLES;

    static {
        Props props = new Props("creater.yaml");
        EXCLUDE_TABLES = props.getList("opsli.exclude-tables");
    }

    @Autowired(required = false)
    private SQLActuator sqlActuator;

    @Autowired
    private ITableService iTableService;

    @Override
    public String getType() {
        return "mysql";
    }

    /**
     * 新建表
     * @param model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(CreaterTableAndColumnModel model) {
        if(model == null) return;

        CreaterTableModel currTable = iTableService.get(model.getId());
        if(currTable == null){
            // 同步表失败，暂无该表
            throw new ServiceException(CreaterMsg.EXCEPTION_SYNC_NULL);
        }

        // 排查该表 是否是 在排除外的表， 如果是则不允许同步
        if(EXCLUDE_TABLES.contains(currTable.getOldTableName()) || EXCLUDE_TABLES.contains(currTable.getTableName())){
            // 同步表失败 系统核心关键表不允许同步
            throw new ServiceException(CreaterMsg.EXCEPTION_SYNC_CORE);
        }

        // 删除表
        sqlActuator.execute(this.genDropTableSQL(model.getOldTableName()));
        sqlActuator.execute(this.genDropTableSQL(model.getTableName()));

        // 新建表
        sqlActuator.execute(this.genCreateTableSQL(model));

        // 更新同步状态
        iTableService.renewSyncState(model.getId());
    }

    /**
     * 生成删除表SQL
     * @param tableName
     * @return sql
     */
    private String genDropTableSQL(String tableName) {
        return "DROP TABLE IF EXISTS " + SQLFilterKit.replaceSQL(tableName) + ";";
    }

    /**
     * 生成创建表SQL
     * @param model
     * @return sql
     */
    private String genCreateTableSQL(CreaterTableAndColumnModel model){
        // 表名
        String tableName = model.getTableName();
        // 表字段
        List<CreaterTableColumnModel> columnList = model.getColumnList();

        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE `").append(SQLFilterKit.replaceSQL(tableName))
                .append("`").append(" (");

        for (int i = 0; i < columnList.size(); i++) {
            CreaterTableColumnModel tmp = columnList.get(i);

            // 字段名
            str.append("`").append(
                    SQLFilterKit.replaceSQL(tmp.getFieldName())
            ).append("`");

            // 字段类型
            String fieldType = SQLFilterKit.replaceSQL(tmp.getFieldType());
            FieldTypeAttribute fieldAttr = MySQLSyncColumnType.INSTANCE.getAttr(fieldType);
            str.append(" ").append(fieldType);
            if(fieldAttr != null){
                // 字段有长度
                if(fieldAttr.isIzLength()){
                    Integer len = tmp.getFieldLength();
                    str.append("(");
                    // 字段有精度
                    if(fieldAttr.isIzPrecision()){
                        str.append( len + tmp.getFieldPrecision() ).append(",").append(tmp.getFieldPrecision());
                    } else {
                        str.append(len);
                    }
                    str.append(")");
                }
            }

            // 判断是否为主键
            if(YES == tmp.getIzPk()){
                str.append(" ").append("PRIMARY KEY");
            }else{
                // 判断是否非空
                if(YES == tmp.getIzNull()){
                    str.append(" ").append("NOT NULL");
                }
            }

            // 字段描述
            if(StringUtils.isNotEmpty(tmp.getFieldComments())){
                str.append(" ").append("COMMENT '")
                      .append(
                              SQLFilterKit.replaceSQL(tmp.getFieldComments())
                      )
                      .append("'");
            }

            if(i != columnList.size()-1){
                str.append(",");
            }
        }

        str.append(" )");
        str.append(" ENGINE=").append(ENGINE);
        str.append(" DEFAULT");
        str.append(" CHARSET=").append(CHARSET);
        str.append(" COMMENT='").append(SQLFilterKit.replaceSQL(model.getComments()))
                .append("'");
        str.append(";");

        return str.toString();
    }
}
