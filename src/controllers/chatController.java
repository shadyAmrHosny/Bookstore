package controllers;

import models.Chat;

import java.sql.*;
import java.util.ArrayList;
public class chatController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "pass1234";
    public boolean sendMessage(int borrowerId, int lenderId,String message) throws Exception {

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "INSERT INTO Chat (borrower_id, lender_id, message) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, borrowerId);
            stmt.setInt(2, lenderId);
            stmt.setString(3, message);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw e;
        }
    }
    public ArrayList<Chat> getMessage(int borrower_id) throws Exception {
        ArrayList<Chat> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Chat WHERE borrower_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, borrower_id);
            ResultSet requestResults = stmt.executeQuery();

            while (requestResults.next()) {
                int lender_id = requestResults.getInt("lender_id");
                String message = requestResults.getString("message");

                Chat chat = new Chat(lender_id, borrower_id, message);
                messages.add(chat);
            }

        } catch (SQLException e) {
            throw e;
        }

        return messages;
    }




}
