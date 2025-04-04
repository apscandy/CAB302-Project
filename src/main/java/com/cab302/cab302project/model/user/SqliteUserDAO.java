package com.cab302.cab302project.model.user;

import com.cab302.cab302project.model.SqliteConnection;

import java.sql.Connection;

public class SqliteUserDAO {
    private final Connection con;

    public SqliteUserDAO() {
        con = SqliteConnection.getInstance();
    }


}
