package everteam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class DBManager {

    private SQLServerDataSource dataSource;

    private static DBManager instance;

    private DBManager() {
        dataSource = new SQLServerDataSource();
        dataSource.setUser("sa");
        dataSource.setPassword("channel13");
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("FaceRecognitionDB");
    }

    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }

        return instance;
    }

    public Connection getConnetion()
            throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            //silent
        }
    }

    public void closestatement(Statement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (Exception e) {
            //silent
        }
    }

    public void closeResulSet(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (Exception e) {
            //silent
        }
    }
}
