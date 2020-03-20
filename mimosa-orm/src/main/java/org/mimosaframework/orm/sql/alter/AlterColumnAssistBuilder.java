package org.mimosaframework.orm.sql.alter;

import org.mimosaframework.orm.sql.AbsColumnBuilder;
import org.mimosaframework.orm.sql.AfterBuilder;
import org.mimosaframework.orm.sql.FirstBuilder;
import org.mimosaframework.orm.sql.create.ColumnAssistBuilder;

public interface AlterColumnAssistBuilder
        extends
        FirstBuilder<AbsColumnBuilder>,
        AfterBuilder<AbsColumnBuilder>,

        ColumnAssistBuilder<AlterColumnAssistBuilder> {
}
