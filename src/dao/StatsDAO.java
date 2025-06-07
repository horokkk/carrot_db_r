package dao;

import dto.BookReviewStat;
import dto.PopularBook;
import dto.AgeGenreStat;
import db.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO {

    // 도서별 평균 평점 및 리뷰 수 조회
    public List<BookReviewStat> getBookReviewStats() {
        List<BookReviewStat> stats = new ArrayList<>();
        String sql = "SELECT book_id, AVG(rating) AS avg_rating, COUNT(*) AS review_count " +
                     "FROM Review GROUP BY book_id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                stats.add(new BookReviewStat(
                    rs.getInt("book_id"),
                    rs.getDouble("avg_rating"),
                    rs.getInt("review_count")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    // 인기 도서 랭킹 조회 (윈도우 함수 사용)
    public List<PopularBook> getPopularBooks() {
        List<PopularBook> list = new ArrayList<>();
        String sql = "SELECT book_id, avg_rating, RANK() OVER (ORDER BY avg_rating DESC) AS rank_no " +
                     "FROM (SELECT book_id, AVG(rating) AS avg_rating FROM Review GROUP BY book_id) AS t";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new PopularBook(
                    rs.getInt("book_id"),
                    rs.getDouble("avg_rating"),
                    rs.getInt("rank_no")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 연령별 장르 선호 통계 조회 (ROLLUP 사용)
    public List<AgeGenreStat> getAgeGenreStats() {
        List<AgeGenreStat> list = new ArrayList<>();
        String sql = "SELECT CASE " +
                     "WHEN m.age BETWEEN 10 AND 19 THEN '10대' " +
                     "WHEN m.age BETWEEN 20 AND 29 THEN '20대' " +
                     "WHEN m.age BETWEEN 30 AND 39 THEN '30대' " +
                     "WHEN m.age BETWEEN 40 AND 49 THEN '40대' " +
                     "WHEN m.age BETWEEN 50 AND 59 THEN '50대' " +
                     "ELSE '60대 이상' END AS age_group, " +
                     "g.genre_name, COUNT(*) AS cnt " +
                     "FROM Review r " +
                     "JOIN Member m ON r.member_id = m.member_id " +
                     "JOIN Book b ON r.book_id = b.book_id " +
                     "JOIN Genre g ON b.genre_id = g.genre_id " +
                     "GROUP BY ROLLUP(age_group, g.genre_name)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(new AgeGenreStat(
                    rs.getString("age_group"),
                    rs.getString("genre_name"),
                    rs.getInt("cnt")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}