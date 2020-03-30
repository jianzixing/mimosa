package org.mimosaframework.orm.sql.delete;

import org.mimosaframework.orm.sql.*;
import org.mimosaframework.orm.sql.stamp.StampAction;

import java.io.Serializable;

public class DefaultSQLDeleteBuilder
        extends
        AbstractSQLBuilder
        implements
        RedefineDeleteBuilder {

    @Override
    public Object delete() {
        return this;
    }

    @Override
    public Object table(Class table) {
        return this;
    }

    @Override
    public Object table(Class table, String tableAliasName) {
        return this;
    }

    @Override
    public Object from() {
        return this;
    }

    @Override
    public Object where() {
        return this;
    }

    @Override
    public Object column(Serializable field) {
        return this;
    }

    @Override
    public Object column(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object column(String aliasName, Serializable field) {
        return this;
    }

    @Override
    public Object eq() {
        return this;
    }

    @Override
    public Object value(Object value) {
        return this;
    }

    @Override
    public Object in() {
        return this;
    }

    @Override
    public Object nin() {
        return this;
    }

    @Override
    public Object like() {
        return this;
    }

    @Override
    public Object ne() {
        return this;
    }

    @Override
    public Object gt() {
        return this;
    }

    @Override
    public Object gte() {
        return this;
    }

    @Override
    public Object lt() {
        return this;
    }

    @Override
    public Object lte() {
        return this;
    }

    @Override
    public BetweenValueBuilder between() {
        return this;
    }

    @Override
    public BetweenValueBuilder notBetween() {
        return this;
    }

    @Override
    public Object section(Object valueA, Object valueB) {
        return this;
    }

    @Override
    public Object and() {
        return this;
    }

    @Override
    public Object limit(int pos, int len) {
        return this;
    }

    @Override
    public Object or() {
        return this;
    }

    @Override
    public Object orderBy() {
        return this;
    }

    @Override
    public Object table(String... aliasNames) {
        return this;
    }

    @Override
    public Object wrapper(AboutChildBuilder builder) {
        return this;
    }

    @Override
    public Object table(Class... table) {
        return this;
    }

    @Override
    public Object asc() {
        return this;
    }

    @Override
    public Object desc() {
        return this;
    }

    @Override
    public Object isNull(Serializable field) {
        return this;
    }

    @Override
    public Object isNull(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object isNull(String aliasName, Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(Class table, Serializable field) {
        return this;
    }

    @Override
    public Object isNotNull(String aliasName, Serializable field) {
        return this;
    }

    @Override
    public Object using() {
        return this;
    }

    @Override
    public StampAction compile() {
        return null;
    }
}
