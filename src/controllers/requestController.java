package controllers;

import models.Request;

import java.sql.*;
import java.util.ArrayList;
public class requestController {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "pass1234";
    public void submitRequest(Request request) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "INSERT INTO Requests (borrower_id, lender_id, book_id,status,book_name) VALUES (?, ?, ?,?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, request.getBorrowerId());
            stmt.setInt(2, request.getLenderId());
            stmt.setInt(3, request.getBookId());
            stmt.setString(4,request.getStatus());
            stmt.setString(5,request.getBookName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<Request> getRequestHistory(int BorrowerId) throws SQLException {
        ArrayList<Request> requests = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Requests WHERE borrower_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, BorrowerId);
            ResultSet requestResults = stmt.executeQuery();

            while (requestResults.next()) {
                int id = requestResults.getInt(1);
                int borrowerId = requestResults.getInt(2);
                int lenderId = requestResults.getInt(3);
                int bookId = requestResults.getInt(4);
                String status = requestResults.getString(5);
                String bookName=requestResults.getString(6);


                Request request = new Request(id, borrowerId, lenderId, bookId, status,bookName);
                requests.add(request);
            }
        } catch (SQLException e) {
            throw e;
        }

        return requests;
    }

    public ArrayList<Request> getIncomingRequests(int LenderId) throws SQLException {
        ArrayList<Request> requests = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Requests WHERE lender_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, LenderId);
            ResultSet requestResults = stmt.executeQuery();

            while (requestResults.next()) {
                int id = requestResults.getInt(1);
                int borrowerId = requestResults.getInt(2);
                int lenderId = requestResults.getInt(3);
                int bookId = requestResults.getInt(4);
                String status = requestResults.getString(5);
                String bookName=requestResults.getString(6);
                Request request = new Request(id, borrowerId, lenderId, bookId, status,bookName);
                requests.add(request);
            }
        } catch (SQLException e) {
            throw e;
        }

        return requests;
    }
    public ArrayList<Request> listAllRequests() throws SQLException {
        ArrayList<Request> requests = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Requests";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet requestResults = stmt.executeQuery();
            while (requestResults.next()) {
                int id = requestResults.getInt(1);
                int borrowerId = requestResults.getInt(2);
                int lenderId = requestResults.getInt(3);
                int bookId = requestResults.getInt(4);
                String status = requestResults.getString(5);
                String bookName=requestResults.getString(6);
                Request request = new Request(id, borrowerId, lenderId, bookId, status,bookName);
                requests.add(request);
            }
        } catch (SQLException e) {
            throw e;
        }

        return requests;
    }
    public boolean acceptRejectRequest(int requestId, String status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String updateRequestQuery = "UPDATE Requests SET status = ? WHERE id = ?";
            PreparedStatement updateRequestStmt = connection.prepareStatement(updateRequestQuery);
            updateRequestStmt.setString(1, status);
            updateRequestStmt.setInt(2, requestId);
            updateRequestStmt.executeUpdate();
            if (status.equals("ACCEPTED")) {
                String updateBookQuery = "UPDATE Books SET quantity = quantity - 1 WHERE id = (SELECT book_id FROM Requests WHERE id = ?)";
                PreparedStatement updateBookStmt = connection.prepareStatement(updateBookQuery);
                updateBookStmt.setInt(1, requestId);
                updateBookStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }
    public int getBorrowerIdFromReqId(int reqId) throws SQLException {

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Requests WHERE id =?";
            PreparedStatement statement= connection.prepareStatement(query);
            statement.setInt(1,reqId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("borrower_id");
            }
        } catch (SQLException e) {
            throw e;
        }
        return -1;
    }

}
