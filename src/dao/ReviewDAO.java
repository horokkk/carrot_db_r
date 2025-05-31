package dao;

import dao.ReviewDAO;
import dto.Review;
import db.DBUtil;

import java.sql.*;
import java.util.*;

public class ReviewDAO {

    public List<Review> getReviewsByBook(int bookId) {
        List<Review> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT r.review_id, r.book_id, m.user_id, r.content, r.rating, r.review_date " +
                         "FROM Review r JOIN Member m ON r.member_id = m.member_id WHERE r.book_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Review(
                    rs.getInt("review_id"),
                    rs.getInt("book_id"),
                    rs.getString("user_id"),
                    rs.getString("content"),
                    rs.getInt("rating"),
                    rs.getString("review_date")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean verifyReviewOwner(int reviewId, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT m.password FROM Review r JOIN Member m ON r.member_id = m.member_id " +
                         "WHERE r.review_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateReview(int reviewId, String content, int rating) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE Review SET content = ?, rating = ? WHERE review_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, content);
            stmt.setInt(2, rating);
            stmt.setInt(3, reviewId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteReview(int reviewId) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM Review WHERE review_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, reviewId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 비밀번호 확인
    public boolean verifyUserPassword(String userId, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT password FROM Member WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

// 리뷰 등록
public boolean writeReview(int bookId, String userId, String content, int rating, String date) {
    try (Connection conn = DBUtil.getConnection()) {
        System.out.println("📥 리뷰 등록 시도 중");
System.out.println("userId: " + userId);
System.out.println("bookId: " + bookId);
System.out.println("content: " + content);
System.out.println("rating: " + rating);
System.out.println("date: " + date);

        // member_id 얻기
        String getMemberSql = "SELECT member_id FROM Member WHERE user_id = ?";
        PreparedStatement getMemberStmt = conn.prepareStatement(getMemberSql);
        getMemberStmt.setString(1, userId);
        ResultSet rs = getMemberStmt.executeQuery();
        if (rs.next()) {
            int memberId = rs.getInt("member_id");

            String sql = "INSERT INTO Review (book_id, member_id, content, rating, review_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            stmt.setInt(2, memberId);
            stmt.setString(3, content);
            stmt.setInt(4, rating);
            stmt.setString(5, date);
            return stmt.executeUpdate() > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

}
