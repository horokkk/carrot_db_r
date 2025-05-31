package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import db.DBUtil;

public class ReviewDAO {
    // 비밀번호 검증
public boolean verifyReviewOwner(int reviewId, String password) {
    try (Connection conn = DBUtil.getConnection()) {
        String sql = "SELECT r.review_id FROM Review r JOIN Member m ON r.member_id = m.member_id " +
                     "WHERE r.review_id = ? AND m.password = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, reviewId);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

// 리뷰 수정
public boolean updateReview(int reviewId, String newContent, int newRating) {
    try (Connection conn = DBUtil.getConnection()) {
        String sql = "UPDATE Review SET content = ?, rating = ? WHERE review_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newContent);
        stmt.setInt(2, newRating);
        stmt.setInt(3, reviewId);
        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

// 리뷰 삭제
public boolean deleteReview(int reviewId) {
    try (Connection conn = DBUtil.getConnection()) {
        String sql = "DELETE FROM Review WHERE review_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, reviewId);
        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


public Review getReviewById(int reviewId) {
    try (Connection conn = DBUtil.getConnection()) {
        String sql = "SELECT r.*, b.title, m.user_id FROM Review r " +
                     "JOIN Book b ON r.book_id = b.book_id " +
                     "JOIN Member m ON r.member_id = m.member_id " +
                     "WHERE r.review_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, reviewId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Review r = new Review();
            r.setReviewId(rs.getInt(\"review_id\"));
            r.setBookId(rs.getInt(\"book_id\"));
            r.setUserId(rs.getString(\"user_id\"));
            r.setContent(rs.getString(\"content\"));
            r.setRating(rs.getInt(\"rating\"));
            r.setDate(rs.getDate(\"review_date\"));
            r.setTitle(rs.getString(\"title\")); // 도서명
            return r;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
    
}
