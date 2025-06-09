package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil { //사용자에 맞게 URL, USER, PASSWORD 수정할 것
	private static final String URL      = "jdbc:mysql://0.tcp.jp.ngrok.io:16560/carrot_db?useUnicode=true&characterEncoding=UTF-8";
	private static final String USER     = "carrot";
	private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
            return null;
        }
    }
}

