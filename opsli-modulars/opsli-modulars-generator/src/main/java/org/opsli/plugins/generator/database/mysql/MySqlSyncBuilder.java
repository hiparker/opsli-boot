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
package org.opsli.plugins.generator.database.mysql;

import org.apache.commons.lang3.StringUtils;
import org.opsli.common.enums.DictType;
import org.opsli.common.utils.Props;
import org.opsli.plugins.generator.enums.DataBaseType;
import org.opsli.plugins.generator.exception.GeneratorException;
import org.opsli.plugins.generator.msg.GeneratorMsg;
import org.opsli.plugins.generator.SyncStrategy;
import org.opsli.plugins.generator.FieldTypeAttribute;
import org.opsli.modulars.generator.column.wrapper.GenTableColumnModel;
import org.opsli.modulars.generator.general.actuator.SQLActuator;
import org.opsli.modulars.generator.table.service.IGenTableService;
import org.opsli.modulars.generator.table.wrapper.GenTableAndColumnModel;
import org.opsli.modulars.generator.table.wrapper.GenTableModel;
import org.opsli.plugins.waf.util.SQLFilterKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * MySQL 同步构建器
 *
 * @author parker
 * @date 2020-09-13 19:36
 */
@Service
@Transactional(readOnly = true)
public class MySqlSyncBuilder implements SyncStrategy {

    /** 数据库引擎 */
    private static final String ENGINE = "InnoDB";
    /** 字符格式 */
    private static final String CHARSET = "utf8mb4";
    /** 默认排序规则 */
    private static final String COLLATE = "utf8mb4_general_ci";
    /** 排除表 */
    private static final List<String> EXCLUDE_TABLES;

    static {
        Props props = new Props("generator.yaml");
        EXCLUDE_TABLES = props.getList("opsli.exclude-tables");
    }

    @Autowired(required = false)
    private SQLActuator sqlActuator;

    @Autowired
    private IGenTableService iGenTableService;

    @Override
    public DataBaseType getType() {
        return DataBaseType.MYSQL;
    }

    /**
     * 新建表
     * @param model
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void execute(GenTableAndColumnModel model) {
        if(model == null){
            return;
        }

        GenTableModel currTable = iGenTableService.get(model.getId());
        if(currTable == null){
            // 同步表失败，暂无该表
            throw new GeneratorException(GeneratorMsg.EXCEPTION_SYNC_NULL);
        }

        // 排查该表 是否是 在排除外的表， 如果是则不允许同步
        if(EXCLUDE_TABLES.contains(currTable.getOldTableName()) || EXCLUDE_TABLES.contains(currTable.getTableName())){
            // 同步表失败 系统核心关键表不允许同步
            throw new GeneratorException(GeneratorMsg.EXCEPTION_SYNC_CORE);
        }

        // 删除表
        sqlActuator.execute(this.genDropTableSql(model.getOldTableName()));
        sqlActuator.execute(this.genDropTableSql(model.getTableName()));

        // 新建表
        sqlActuator.execute(this.genCreateTableSql(model));

        // 更新同步状态
        iGenTableService.renewSyncState(model.getId());
    }

    /**
     * 生成删除表SQL
     * @param tableName 表名
     * @return sql
     */
    private String genDropTableSql(String tableName) {
        return "DROP TABLE IF EXISTS " + SQLFilterKit.replaceSQL(tableName) + ";";
    }

    /**
     * 生成创建表SQL
     * @param model 模型
     * @return sql
     */
    private String genCreateTableSql(GenTableAndColumnModel model){
        // 表名
        String tableName = model.getTableName();
        // 表字段
        List<GenTableColumnModel> columnList = model.getColumnList();

        StringBuilder str = new StringBuilder();
        str.append("CREATE TABLE `").append(SQLFilterKit.replaceSQL(tableName))
                .append("`").append(" (");

        for (int i = 0; i < columnList.size(); i++) {
            GenTableColumnModel tmp = columnList.get(i);

            // 字段名
            str.append("`").append(
                    SQLFilterKit.replaceSQL(tmp.getFieldName())
            ).append("`");

            // 字段类型
            String fieldType = SQLFilterKit.replaceSQL(tmp.getFieldType());
            FieldTypeAttribute fieldAttr = MySqlSyncColumnType.INSTANCE.getAttr(fieldType);
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
            if(DictType.NO_YES_YES.getValue().equals(tmp.getIzPk())){
                str.append(" ").append("PRIMARY KEY");
            }else{
                // 判断是否非空
                if(DictType.NO_YES_YES.getValue().equals(tmp.getIzNotNull())){
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
        str.append(" COLLATE=").append(COLLATE);
        str.append(" COMMENT='").append(SQLFilterKit.replaceSQL(model.getComments()))
                .append("'");
        str.append(";");

        return str.toString();
    }
}
