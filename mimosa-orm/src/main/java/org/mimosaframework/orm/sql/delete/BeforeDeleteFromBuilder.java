package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.AbsTableBuilder;

public interface BeforeDeleteFromBuilder<T> extends AbsTableBuilder<T> {
    T table(Class... table);

    T table(String... aliasNames);
}
