package urlsApp;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;


public class myData {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:myData.db";
    private Connection conn;
    private Statement stat;

    public myData() {
        try {
            Class.forName(myData.DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTables();
    }

    public boolean createTables()  {
        String createHistoryLinks = "CREATE TABLE IF NOT EXISTS historyLinks (id_hLink INTEGER PRIMARY KEY AUTOINCREMENT, historyLink varchar(255))";
        try {
            stat.execute(createHistoryLinks);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertHistoryLink(String historyLink) {
        try {
            PreparedStatement prepStmt = conn.prepareStatement(
                    "insert into historyLinks values (NULL, ?);");
            prepStmt.setString(1, historyLink);
            prepStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> selectHistoryLinks() {
        List<String> historyLinks = new LinkedList<String>();
        try {
            ResultSet result = stat.executeQuery("SELECT * FROM historyLinks");
            String historyLink;
            while(result.next()) {
                historyLink = result.getString("historyLink");
                historyLinks.add(historyLink);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return historyLinks;
    }


    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
