package org.example.lottery_system.common.filter;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestMysqlConnection {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://127.0.0.1:3307/lottery_system?characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true";
        String user = "root";
        String password = "123456";
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println("Connected successfully!");
        conn.close();
    }
}
