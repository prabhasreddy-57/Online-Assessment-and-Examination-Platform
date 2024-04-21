
import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    // Specify the correct URL format including the user and password as parameters if necessary
    // It's a better practice to pass the username and password as arguments to getConnection() method for security reasons
    private static String url = "jdbc:mysql://localhost:3306/users";
    private static String user = "root";
    private static String password = "PES1UG21CS401";

    public static Connection getConnection() throws Exception {
        // It's no longer necessary to explicitly load the driver class in modern JDBC 4.0+ drivers
        // Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }

    public static void main(String[] args) {
        try (Connection con = DbConnection.getConnection()) { // Try-with-resources to ensure closure
            if (con != null) {
                System.out.println("Connection established successfully.");
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
