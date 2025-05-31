package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/carrot_db?useUnicode=true&characterEncoding=UTF-8"; // ✅ 로컬 DB 주소
    private static final String USER = "root";     // ✅ 로컬 MySQL 사용자명
    private static final String PASSWORD = "0118"; // ✅ 로컬 MySQL 비번

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("DB 연결 실패: " + e.getMessage());
            return null;
        }
    }
}

