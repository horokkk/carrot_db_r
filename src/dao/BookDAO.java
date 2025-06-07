package dao;

import dto.Book;
import db.DBUtil;
import java.sql.*;
import java.util.*;

public class BookDAO {
    //도서 검색
    public List<Book> searchBooks(String keyword) {
        List<Book> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT b.book_id, b.title, g.genre_name, a.author_name " +
                         "FROM Book b JOIN Genre g ON b.genre_id = g.genre_id " +
                         "JOIN Author a ON b.author_id = a.author_id " +
                         "WHERE b.title LIKE ? OR g.genre_name LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Book(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("genre_name"),
                    rs.getString("author_name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //도서 추가
    public boolean insertBook(int genreId, int authorId, String title, String publisher) {
    String sql = "INSERT INTO Book (genre_id, author_id, title, publisher) VALUES (?, ?, ?, ?)";
    try (Connection conn = DBUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, genreId);
        stmt.setInt(2, authorId);
        stmt.setString(3, title);
        stmt.setString(4, publisher);

        return stmt.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    // 장르별 도서 조회
    public List<Book> findByGenre(String genreName) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT b.book_id, b.title, g.genre_name, a.author_name " +
                     "FROM Book b " +
                     "JOIN Genre g ON b.genre_id = g.genre_id " +
                     "JOIN Author a ON b.author_id = a.author_id " +
                     "WHERE g.genre_name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, genreName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("genre_name"),
                        rs.getString("author_name")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
