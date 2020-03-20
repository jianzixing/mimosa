package org.mimosaframework.orm.sql;

import org.mimosaframework.orm.sql.alter.AlterFactory;
import org.mimosaframework.orm.sql.create.Columns;
import org.mimosaframework.orm.sql.create.CreateFactory;
import org.mimosaframework.orm.sql.delete.DeleteFactory;
import org.mimosaframework.orm.sql.drop.DropFactory;
import org.mimosaframework.orm.sql.insert.InsertFactory;
import org.mimosaframework.orm.sql.select.SelectFactory;
import org.mimosaframework.orm.sql.update.UpdateFactory;

public class TestSQL {
    public static void main(String[] args) {
        DeleteFactory.delete()
                .table(TestSQL.class)
                .from()
                .table(TestSQL.class, TestSQL.class)
                .where()
                .column("")
                .eq().value("");

        DeleteFactory.delete().from().table(TestSQL.class)
                .where()
                .column("").gt().value("")
                .and()
                .wrapper(null).and().wrapper(null).and()
                .column("").in().value("")
                .orderBy().column("").asc()
                .orderBy().column("").desc()
                .orderBy().column("").asc()
                .limit(0, 10);

        DeleteFactory.delete().from()
                .table(TestSQL.class)
                .using(TestSQL.class)
                .where()
                .column("").eq().value("");

        DeleteFactory.delete()
                .table("")
                .from()
                .table(TestSQL.class)
                .where()
                .column("").eq().value("");

        ///

        SelectFactory.select()
                .fields(
                        Fields.build()
                                .field("", "")
                                .field("", "")
                                .fun(Fields.function().avg(new FieldItem(1)))
                )
                .from()
                .table(TestSQL.class, TestSQL.class)
                .where()
                .column("").eq().value("").and()
                .column("").isNull("")
                .and().wrapper(Wrapper.build().column("").eq().value("").and().wrapper(null));

        SelectFactory.select().all().from()
                .table(TableItems.build().table(TestSQL.class))
                .left().join().table(TestSQL.class).on().column("").eq().value("").and().wrapper(null).and().column("").eq().value("")
                .inner().join().table(TestSQL.class).on().column("").eq().value("")
                .where().column("").eq().value("").groupBy()
                .having().count(new FieldItem(1)).eq().value("")
                .orderBy().asc().limit(0, 10);


        ///

        UpdateFactory.update().table(TestSQL.class)
                .set()
                .column("").eq().value("")
                .split()
                .column("").eq().value("")
                .where().column("").eq().value("")
                .orderBy().asc().limit(0, 10);


        ///

        InsertFactory.insert().into().table(TestSQL.class)
                .columns("", "")
                .values()
                .row("", "")
                .split()
                .row("", "")
                .split()
                .row("", "");


        ///

        CreateFactory.create().database().ifNotExist().name("").charset("").collate("");

        CreateFactory.create().table().ifNotExist().name("").columns(
                Columns.column("id").intType().primary().key().autoIncrement().not().nullable()
        ).charset("").extra("");

        CreateFactory.create().unique().index().name("").on().table(TestSQL.class).columns("", "");


        ///

        DropFactory.drop().database().ifExist().name("");
        DropFactory.drop().table().ifExist().table(TestSQL.class);
        DropFactory.drop().index().name("").on().table(TestSQL.class);

        ///
        AlterFactory.alter().database()
                .name("")
                .charset("").collate("");

        AlterFactory.alter().table(TestSQL.class).add().column().name("")
                .intType().autoIncrement().after().column("");


        AlterFactory.alter().table(TestSQL.class).add().index().name("").column("").comment("");

        AlterFactory.alter().table(TestSQL.class).change().oldColumn("")
                .newColumn("").intType().autoIncrement().after().column("");

        AlterFactory.alter().table(TestSQL.class).modify().column("")
                .intType().autoIncrement().after().column("");

        AlterFactory.alter().table(TestSQL.class).drop().column().name("");
        AlterFactory.alter().table(TestSQL.class).drop().index().name("");
        AlterFactory.alter().table(TestSQL.class).drop().primary().key();

        AlterFactory.alter().table(TestSQL.class).rename().column().oldColumn("").to().newColumn("");
        AlterFactory.alter().table(TestSQL.class).rename().index().oldColumn("").to().newColumn("");
        AlterFactory.alter().table(TestSQL.class).rename().name("");
    }
}
