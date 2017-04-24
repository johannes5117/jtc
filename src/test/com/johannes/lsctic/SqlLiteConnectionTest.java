package com.johannes.lsctic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johannes on 11.04.2017.
 */
public class SqlLiteConnectionTest {
    @Test
    public void closeConnections() throws Exception {
    }

    @Test
    public void query() throws Exception {
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void selectWhere() throws Exception {
    }

    @Test
    public void insert() throws Exception {
    }

    @Test
    public void updateOneAttribute() throws Exception {
    }

    @Test
    public void getConnection() throws Exception {
    }

    @Test
    public void getInterns() throws Exception {
    }

    @Test
    public void queryNoReturn() throws Exception {
    }

    @Test
    public void buildUpdateOrInsertStatementForSetting() {
        SqlLiteConnection sqlLiteConnection = new SqlLiteConnection("test.db");
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiPassword", "Vogel");
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("amiTest", "Test123");
        sqlLiteConnection.buildUpdateOrInsertStatementForSetting("MysqlPluginDatasource2", "Field;Ranger");

        Assert.assertEquals("Vogel", sqlLiteConnection.query("Select setting from Settings where description='amiPassword'"));
        Assert.assertEquals("Test123", sqlLiteConnection.query("Select setting from Settings where description='amiTest'"));
        Assert.assertEquals("Field;Ranger", sqlLiteConnection.query("Select setting from Settings where description='MysqlPluginDatasource2'"));
    }

}