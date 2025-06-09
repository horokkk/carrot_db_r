package dao;

import dto.Member;

import java.sql.*;

import db.DBUtil;

public class MemberDAO {

    //회원가입
       public boolean register(Member member) {
        String sql = "INSERT INTO Member (user_id, password, name, age, gender) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, member.getUserId());
            pstmt.setString(2, member.getPassword());
            pstmt.setString(3, member.getName());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getGender());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //로그인
    public boolean login(String user_id, String password) {
    String sql = "SELECT * FROM Member WHERE user_id = ? AND password = ?";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, user_id);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        return rs.next(); // 결과가 있으면 로그인 성공

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}
