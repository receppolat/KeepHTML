package signup;
import DataBaseOperations.DBManagementUser;
import java.sql.*;
import java.util.Date;
/**
 *
 * @author RECEP
 */
public class DbHelper {
    private String userName = "root";
    private String password = "12345";
    private String dbUrl = "jdbc:mysql://localhost:3306/keephtml?useSSL=false&serverTimezone=UTC";
    public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(dbUrl, userName, password);
        
    }
    public void showErrorMessage(SQLException exception){
        System.out.println("Exception Hatası: "+exception.getMessage());
        System.out.println("Exception Code: "+ exception.getErrorCode());
    }
    
}
