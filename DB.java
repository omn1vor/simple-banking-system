package banking;

import java.sql.*;

public class DB {
    private final String url;

    public DB(String filename) {
        url = String.format("jdbc:sqlite:%s", filename);
        init();
    }

    public String getUrl() {
        return url;
    }

    public void update(String sql) {
        try (Connection con = DriverManager.getConnection(url);
             Statement statement = con.createStatement()) {

            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void init() {
        String sql =
                "CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0);";
        update(sql);
    }
}
