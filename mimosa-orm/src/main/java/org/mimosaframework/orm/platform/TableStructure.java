package org.mimosaframework.orm.platform;

import java.util.ArrayList;
import java.util.List;

public class TableStructure {
    private String tableSchema;
    /**
     * must be
     */
    private String tableName;
    private String type;
    private long count;
    private Object lastUsed;
    /**
     * if support must be
     */
    private String comment;
    private Object createTime;

    private List<TableColumnStructure> columnStructures;
    private List<TableIndexStructure> indexStructures;
    private List<TableConstraintStructure> constraintStructures;

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Object getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Object lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public List<TableColumnStructure> getColumnStructures() {
        return columnStructures;
    }

    public void setColumnStructures(List<TableColumnStructure> columnStructures) {
        this.columnStructures = columnStructures;
    }

    public List<TableIndexStructure> getIndexStructures() {
        return indexStructures;
    }

    public void setIndexStructures(List<TableIndexStructure> indexStructures) {
        this.indexStructures = indexStructures;
    }

    public List<TableConstraintStructure> getConstraintStructures() {
        return constraintStructures;
    }

    public void setConstraintStructures(List<TableConstraintStructure> constraintStructures) {
        this.constraintStructures = constraintStructures;
    }

    public List<TableIndexStructure> getIndexStructures(String indexName) {
        List<TableIndexStructure> structures = null;
        if (indexStructures != null) {
            for (TableIndexStructure indexStructure : indexStructures) {
                if (indexStructure.getIndexName().equalsIgnoreCase(indexName)) {
                    if (structures == null) structures = new ArrayList<>();
                    structures.add(indexStructure);
                }
            }
        }
        return structures;
    }

    public boolean isPrimaryKeyColumn(String column) {
        if (constraintStructures != null) {
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (column.equalsIgnoreCase(constraintStructure.getColumnName()) && "P".equals(constraintStructure.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<TableConstraintStructure> getPrimaryKey() {
        if (constraintStructures != null) {
            List<TableConstraintStructure> structures = new ArrayList<>();
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if ("P".equals(constraintStructure.getType())) {
                    structures.add(constraintStructure);
                }
            }
            return structures;
        }
        return null;
    }

    public boolean isUniqueColumn(String mappingColumnName) {
        if (constraintStructures != null) {
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (mappingColumnName.equalsIgnoreCase(constraintStructure.getColumnName())
                        && "U".equals(constraintStructure.getType())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnlyNormalIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "D".equals(indexStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyUniqueIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "U".equals(indexStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyPrimaryKey(String mappingColumnName) {
        if (constraintStructures != null) {
            int c = 0;
            for (TableConstraintStructure constraintStructure : constraintStructures) {
                if (mappingColumnName.equalsIgnoreCase(constraintStructure.getColumnName())
                        && "P".equals(constraintStructure.getType())) {
                    c++;
                }
            }
            if (c == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean isOnlyNotNormalIndex(String mappingColumnName) {
        if (indexStructures != null) {
            int c1 = 0, c2 = 0;
            for (TableIndexStructure indexStructure : indexStructures) {
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "P".equals(indexStructure.getType())) {
                    c1++;
                }
                if (mappingColumnName.equalsIgnoreCase(indexStructure.getColumnName())
                        && "U".equals(indexStructure.getType())) {
                    c2++;
                }
            }
            if (c1 == 1 || c2 == 1) {
                return true;
            }
        }
        return false;
    }

    public List<TableColumnStructure> getAutoIncrement() {
        if (columnStructures != null) {
            List<TableColumnStructure> columnStructures = null;
            for (TableColumnStructure columnStructure : this.columnStructures) {
                if (columnStructure.isAutoIncrement()) {
                    if (columnStructures == null) {
                        columnStructures = new ArrayList<>();
                    }
                    columnStructures.add(columnStructure);
                }
            }
            return columnStructures;
        }
        return null;
    }
}
