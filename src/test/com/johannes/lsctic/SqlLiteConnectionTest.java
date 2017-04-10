package com.johannes.lsctic;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by johannes on 11.04.2017.
 */
public class SqlLiteConnectionTest {
    @Test
    public void closeConnections() throws Exception {
        Assert.assertEquals("Closed","Closed");
    }

    @Test
    public void query() throws Exception {
        Assert.assertEquals("Test", "GF");
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

}