import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try {
            String url = "jdbc:mysql://localhost:3306/testdb";
            String user = "root";
            String password = "";

            @SuppressWarnings("unused")
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to MySQL!");
        } catch (SQLException e) {
        }
    }
}
