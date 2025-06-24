import java.sql.*;

public class DtBstil {
    private static final String URL = "jdbc:mysql://localhost:3306/Book_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "Scarlet_Nanami2333";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 加载驱动
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL驱动未找到喵", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            System.err.println("关闭数据库连接失败喵: " + e.getMessage());
        }
    }
}