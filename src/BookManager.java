import java.sql.*;
import java.util.*;

public class BookManager {
    // 添加图书（数据库版）
    public void addBook(Book book) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DtBstil.getConnection();
            String sql = "INSERT INTO books (id, title, author, price) VALUES (?, ?, ?, ?)"; // SQL语句
            st = conn.prepareStatement(sql);
            st.setString(1, book.getId());
            st.setString(2, book.getTitle());
            st.setString(3, book.getAuthor());
            st.setDouble(4, book.getPrice()); // 四条指令对应values中的四个参数，后续操作同理
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("添加图书失败喵: " + e.getMessage());
        } finally {
            DtBstil.close(conn, st, null);
        }
    }

    public void deleteBook(String id) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = DtBstil.getConnection();
            String sql = "DELETE FROM books WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除图书失败喵: " + e.getMessage());
        } finally {
            DtBstil.close(conn, st, null);
        }

    }

    public void UpdateBook(String id, String title, String author, double price) {
        Connection conn = null;
        PreparedStatement st = null;

        try {
            conn = DtBstil.getConnection();
            String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, title);
            st.setString(2, author);
            st.setDouble(3, price);
            st.setString(4, id);
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("更新图书失败喵: " + e.getMessage());
        } finally {
            DtBstil.close(conn, st, null);
        }
    }

    public Book searchByid(String id) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = DtBstil.getConnection();
            String sql = "SELECT * FROM books WHERE id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"));
            }
        } catch (SQLException e) {
            System.err.println("查询图书失败喵: " + e.getMessage());
        } finally {
            DtBstil.close(conn, st, rs);
        }
        return null;
    }

    public List<Book> searchByname(String keyword) {
        List<Book> result = new ArrayList<>();

        return result;
    }

    public List<Book> DisplayAll() {
        List<Book> books = new ArrayList<>();

        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DtBstil.getConnection();
            String sql = "SELECT * FROM books";
            st = conn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price")));
            }
        } catch (SQLException e) {
            System.err.println("查询图书失败喵: " + e.getMessage());
        } finally {
            DtBstil.close(conn, st, rs);
        }
        return books;
    }
}
