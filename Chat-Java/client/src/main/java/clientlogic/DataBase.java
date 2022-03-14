package clientlogic;

import gui.*;
import org.intellij.lang.annotations.Language;

import java.sql.*;


public class DataBase implements AuthService {

    private static DataBase instance;

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataBase.class);

    public DataBase() throws Exception {
        createTable();
    }

    public static void main(String[] args) throws Exception {
        resultSet();
    }

    public static synchronized DataBase getInstance() {
        if (instance == null) {
            try {
                instance = new DataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private static void init() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    public static Connection getConnection() {
        @Language("SQL")
        String urlH2 = "jdbc:h2:./Chat-Java/client/src/main/resources/db/demodb";
        String user = "root";
        String pass = "root";
        try{
                return DriverManager.getConnection(urlH2, "", "");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorAlertExample.getErrorConnectionSQLFatal();
            return null;
        }
    }

    public static void addField() throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
            String query = "ALTER TABLE users ADD status INT";
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        }
    }

    private static void createTable() throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
            String query_01 = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100), password VARCHAR(100), avatar BLOB, status INTEGER)";
            try (Statement statement = connection.createStatement()) {
                statement.execute(query_01);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorAlertExample.getErrorConnectionSQLFatal();
        }
    }

    public static void resultSet() throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
            String query_02 = "SELECT * FROM users";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query_02);
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " : " + rs.getString("name") + " ; " + rs.getString("password") +
                            ";" + rs.getInt("status"));
                }
            }
        }
    }

    public static void delete(int ID) throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
            String query_00 = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query_00)) {
                statement.setInt(1, ID);
                statement.executeUpdate();
            }
        }
    }


    @Override
    public boolean addUser(String name, String pass) throws Exception {
        init();
        if (repeatUser(name)) {
            WarningAlertExample.getWarningRepeatUser();
            return false;
        }
        try (Connection connection = getConnection()) {
            if(connection == null) return false;
            @Language("SQL")
            String query_01 = "INSERT INTO users (name, password, avatar, status) VALUES (?,?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(query_01)) {
                statement.setString(1, name);
                statement.setString(2, pass);
                statement.setBytes(3, Avatar.createAvatar(name));
                statement.setInt(4, 1);
                statement.executeUpdate();
                logger.info("Операция добавления нового пользователя прошла успешно.");
                InformationAlertExample.getInformationRegistrationComplete(name);
                return true;
            } catch (SQLException e) {
                logger.error("SQLException error", e);
                return false;
            }
        }
    }

    @Override
    public boolean auth(String name, String pass) throws Exception {
        init();
        try (Connection connection = getConnection()) {
            if(connection == null) return false;
            @Language("SQL")
            String query_02 = "SELECT * FROM users";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query_02);
                while (rs.next()) {
                    if (rs.getString("name").equals(name) && rs.getString("password").equals(pass)) {
                        if(rs.getInt("status") == 1) {
                            WarningAlertExample.getWarningDoubleUser();
                            logger.info("Вы уже вошли в систему");
                            return false;
                        }
                        logger.info("Пользователь опознан.");
                        updateStatus(name, true);
                        InformationAlertExample.getInformationAuthComplete(name);
                        return true;
                    }
                }
                logger.info("Неизвестный пользователь.");
                return false;
            } catch (SQLException e) {
                logger.error("SQLException error", e);
                return false;
            }
        }
    }

    public boolean repeatUser(String name) throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
                    String query_00 = "SELECT * FROM users";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query_00);
                while (rs.next()) {
                    if (rs.getString("name").equals(name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void updateStatus(String name, boolean onOrOffLine) throws ClassNotFoundException, SQLException {
        init();
        try (Connection connection1 = getConnection()) {
            @Language("SQL")
            String query_00 = "UPDATE users SET status = ? Where name = ?";
            try (PreparedStatement preparedStatement = connection1.prepareStatement(query_00)) {
                if (onOrOffLine) {
                    preparedStatement.setInt(1, 1);
                    preparedStatement.setString(2, name);
                    preparedStatement.executeUpdate();
                } else preparedStatement.setInt(1, 0);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
            }
        }
    }


    public static byte[] getAvatar(String name) throws Exception {
        init();
        try (Connection connection = getConnection()) {
            @Language("SQL")
            String query = "SELECT * FROM users";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    if (rs.getString("name").equals(name)) return rs.getBytes("avatar");
                }
            }
        }
        return null;
    }


}
