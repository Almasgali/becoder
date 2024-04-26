package ru.becoder.krax.utils;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;

public class VariableColumnFilter implements IColumnFilter {
    @Override
    public boolean accept(String tableName, Column column) {
        return !column.getColumnName().equalsIgnoreCase("id")
                && !column.getColumnName().equalsIgnoreCase("password");
    }
}