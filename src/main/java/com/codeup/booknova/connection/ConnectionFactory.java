/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.booknova.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.codeup.booknova.exception.DatabaseException;
import com.codeup.booknova.infra.config.AppConfig;


public class ConnectionFactory {
    private final AppConfig cfg;
    
 
    public ConnectionFactory(AppConfig cfg) { this.cfg = cfg; }


    public Connection open() throws DatabaseException {
        String vendor = cfg.get("db.vendor");
        String host = cfg.get("db.host");
        String port = cfg.get("db.port");
        String name = cfg.get("db.name");
        String user = cfg.get("db.user");
        String pass = cfg.get("db.password");

        String url;
        if ("postgres".equalsIgnoreCase(vendor)) {
            url = String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
        } else {
            String useSSL = cfg.get("db.useSSL");
            url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=UTC", host, port, name, useSSL);
        }
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }
}
