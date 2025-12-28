package org.leng.utils;

import org.leng.Lengbanlist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private Connection connection;

    public void initDatabase() {
        String type = Lengbanlist.getInstance().getConfig().getString("database.type");
        if ("yml".equalsIgnoreCase(type)) {
            String dbPath = Lengbanlist.getInstance().getDataFolder().getAbsolutePath() + "/lengbanlist.db";
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if ("mysql".equalsIgnoreCase(type)) {
            String host = Lengbanlist.getInstance().getConfig().getString("database.mysql.host");
            int port = Lengbanlist.getInstance().getConfig().getInt("database.mysql.port");
            String database = Lengbanlist.getInstance().getConfig().getString("database.mysql.database");
            String username = Lengbanlist.getInstance().getConfig().getString("database.mysql.username");
            String password = Lengbanlist.getInstance().getConfig().getString("database.mysql.password");
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}