import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/college";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    // Get database connection (Singleton Pattern)
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {  // Ensure connection is valid
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database Connected Successfully!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    // Close database connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database Connection Closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
