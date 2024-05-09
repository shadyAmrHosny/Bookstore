package controllers;

import java.sql.*;

public class UserAuth {
    private final String url;
    private final String user;
    private final String password;

    public UserAuth(String url, String user, String password) throws SQLException {
        this.url = url;
        this.user = user;
        this.password = password;
    }

public int signUp(String name, String username, String password) throws SQLException {
    int userId = -1;

    try (Connection connection = DriverManager.getConnection(this.url, this.user, this.password)) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
              //  throw new SQLException("Username already exists");
                System.out.println("Username already exists");
                return -1;
            }
        }
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO Users (name, username, password) VALUES (?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
        }
        try (PreparedStatement lastIdStmt = connection.prepareStatement("SELECT LAST_INSERT_ID()")) {
            ResultSet lastIdResult = lastIdStmt.executeQuery();

            if (lastIdResult.next()) {
                userId = lastIdResult.getInt(1);
            }
        }
    } catch (SQLException e) {
        throw e;
    }

    return userId;
}

    public  int login(String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(this.url, this.user,this.password);
             PreparedStatement stmt = connection.prepareStatement("SELECT id, password FROM Users WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                System.out.println("404 error  (not found)");
                return -1;
            }

            String storedPassword = rs.getString("password");
            if (!password.equals(storedPassword)) {
                //throw new SQLException("401 error should appear (unauthorized)");
                System.out.println("401 error unauthorized)");
                return -1;
            }

            return rs.getInt("id");
        } catch (SQLException e) {
            throw e;
        }
    }
    public String getUserRole(int userId) throws SQLException {
        String role = null;

        try (Connection connection = DriverManager.getConnection(this.url, this.user,this.password)) {
            String query = "SELECT role FROM Users WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet userResults = stmt.executeQuery();

            if (userResults.next()) {
                role = userResults.getString("role");
            }
        } catch (SQLException e) {
            throw e;
        }

        return role;
    }


}

