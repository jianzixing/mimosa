package org.mimosaframework.orm.platform.mysql;

import org.mimosaframework.core.utils.StringTools;
import org.mimosaframework.orm.mapping.MappingField;
import org.mimosaframework.orm.mapping.MappingTable;
import org.mimosaframework.orm.platform.*;
import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.alter.DefaultSQLAlterBuilder;
import org.mimosaframework.orm.sql.stamp.*;

import java.sql.SQLException;
import java.util.Set;

public class MysqlPlatformDialect extends PlatformDialect {
    public MysqlPlatformDialect() {
        registerColumnType(KeyColumnType.INT, "INT");
        registerColumnType(KeyColumnType.VARCHAR, "VARCHAR");
        registerColumnType(KeyColumnType.CHAR, "CHAR");
        registerColumnType(KeyColumnType.TINYINT, "TINYINT");
        registerColumnType(KeyColumnType.SMALLINT, "SMALLINT");
        registerColumnType(KeyColumnType.BIGINT, "BIGINT");
        registerColumnType(KeyColumnType.FLOAT, "FLOAT");
        registerColumnType(KeyColumnType.DOUBLE, "DOUBLE");
        registerColumnType(KeyColumnType.DECIMAL, "DECIMAL");
        registerColumnType(KeyColumnType.BOOLEAN, "BOOLEAN");
        registerColumnType(KeyColumnType.DATE, "DATE");
        registerColumnType(KeyColumnType.TIME, "TIME");
        registerColumnType(KeyColumnType.DATETIME, "DATETIME");
        registerColumnType(KeyColumnType.TIMESTAMP, "TIMESTAMP");

        registerColumnType(KeyColumnType.BLOB, "BLOB");
        registerColumnType(KeyColumnType.MEDIUMBLOB, "MEDIUMBLOB");
        registerColumnType(KeyColumnType.LONGBLOB, "LONGBLOB");
        registerColumnType(KeyColumnType.TEXT, "TEXT");
        registerColumnType(KeyColumnType.MEDIUMTEXT, "MEDIUMTEXT");
        registerColumnType(KeyColumnType.LONGTEXT, "LONGTEXT");
    }

    @Override
    public SQLBuilderCombine alter(StampAlter alter) {
        StampCombineBuilder builder = new MysqlStampAlter();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, alter);
        return combine;
    }

    @Override
    public SQLBuilderCombine create(StampCreate create) {
        StampCombineBuilder builder = new MysqlStampCreate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, create);
        return combine;
    }

    @Override
    public SQLBuilderCombine drop(StampDrop drop) {
        StampCombineBuilder builder = new MysqlStampDrop();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, drop);
        return combine;
    }

    @Override
    public SQLBuilderCombine insert(StampInsert insert) {
        StampCombineBuilder builder = new MysqlStampInsert();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, insert);
        return combine;
    }

    @Override
    public SQLBuilderCombine delete(StampDelete delete) {
        StampCombineBuilder builder = new MysqlStampDelete();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, delete);
        return combine;
    }

    @Override
    public SQLBuilderCombine select(StampSelect select) {
        StampCombineBuilder builder = new MysqlStampSelect();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, select);
        return combine;
    }

    @Override
    public SQLBuilderCombine update(StampUpdate update) {
        StampCombineBuilder builder = new MysqlStampUpdate();
        SQLBuilderCombine combine = builder.getSqlBuilder(this.mappingGlobalWrapper, update);
        return combine;
    }

    @Override
    public void define(DataDefinition definition) throws SQLException {
        if (definition != null) {
            DataDefinitionType type = definition.getType();
            if (type == DataDefinitionType.CREATE_TABLE) {
                MappingTable mappingTable = definition.getMappingTable();
                StampCreate stampCreate = this.commonCreateTable(mappingTable);
                if (StringTools.isNotEmpty(mappingTable.getEngineName())) {
                    stampCreate.extra = "ENGINE=InnoDB";
                }
                this.runner(stampCreate);
                Set<MappingField> mappingFields = mappingTable.getMappingFields();
                for (MappingField mappingField : mappingFields) {
                    if (mappingField.isMappingFieldUnique() || mappingField.isMappingFieldIndex()) {
                        this.triggerIncrAndKeys(type, definition.getMappingTable(),
                                null, mappingField, null);
                    }
                }
            }
            if (type == DataDefinitionType.DROP_TABLE) {
                TableStructure tableStructure = definition.getTableStructure();
                StampDrop stampDrop = this.commonDropTable(tableStructure);
                this.runner(stampDrop);
            }
            if (type == DataDefinitionType.ADD_COLUMN) {
                StampAlter stampAlter = this.commonAddColumn(definition.getMappingTable(), definition.getMappingField());
                this.runner(stampAlter);

                this.triggerIncrAndKeys(type, definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), null);
            }
            if (type == DataDefinitionType.MODIFY_COLUMN) {
                TableStructure tableStructure = definition.getTableStructure();
                MappingTable mappingTable = definition.getMappingTable();
                MappingField mappingField = definition.getMappingField();
                TableColumnStructure columnStructure = definition.getColumnStructure();
                ColumnType columnType = this.getColumnType(JavaType2ColumnType
                        .getColumnTypeByJava(mappingField.getMappingFieldType()));

                String tableName = mappingTable.getMappingTableName();
                String columnName = mappingField.getMappingColumnName();
                DefaultSQLAlterBuilder sql = new DefaultSQLAlterBuilder();
                sql.alter().table(tableName).modify().column(columnName);


                // ColumnEditType.TYPE;
                this.setSQLType(sql, mappingField.getMappingFieldType(),
                        mappingField.getMappingFieldLength(), mappingField.getMappingFieldDecimalDigits());

                boolean isPk = tableStructure.isPrimaryKeyColumn(columnStructure.getColumnName());
                if (!isPk) {
                    if (!mappingField.isMappingFieldNullable()) {
                        sql.not();
                        sql.nullable();
                    }
                }

                String def = mappingField.getMappingFieldDefaultValue();
                if (StringTools.isNotEmpty(def)) {
                    sql.defaultValue(def);
                }

                String cmt = mappingField.getMappingFieldComment();
                if (StringTools.isNotEmpty(cmt)) {
                    sql.comment(cmt);
                }

                // 需要单独修改
                if (columnStructure.isAutoIncrement() != mappingField.isMappingAutoIncrement()) {
                    // ColumnEditType.AUTO_INCREMENT;
                }
                // 需要单独修改
                if (mappingField.isMappingFieldPrimaryKey() != isPk) {
                    // ColumnEditType.PRIMARY_KEY;
                }

                this.triggerIncrAndKeys(type, definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), columnStructure);
            }
            if (type == DataDefinitionType.DROP_COLUMN) {
                StampAlter stampAlter = this.commonDropColumn(definition.getMappingTable(), definition.getColumnStructure());
                this.runner(stampAlter);
                this.triggerIncrAndKeys(type, definition.getMappingTable(), definition.getTableStructure(),
                        definition.getMappingField(), definition.getColumnStructure());
            }
            if (type == DataDefinitionType.ADD_INDEX) {
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.MODIFY_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
                StampCreate sql = this.commonAddIndex(definition.getMappingTable(), definition.getMappingIndex());
                this.runner(sql);
            }
            if (type == DataDefinitionType.DROP_INDEX) {
                StampDrop stampDrop = this.commonDropIndex(definition.getMappingTable(), definition.getIndexName());
                this.runner(stampDrop);
            }
        }
    }

    @Override
    protected void rebuildPrimaryKey(MappingTable mappingTable, TableStructure tableStructure) {

    }

    @Override
    protected void rebuildAutoIncrement(MappingTable mappingTable, TableStructure tableStructure) {
    }

    @Override
    protected void createIndex(MappingTable mappingTable, MappingField mappingField, boolean unique) {
    }

    @Override
    protected void dropIndex(MappingTable mappingTable, MappingField mappingField) {
    }
}
