package controllers;

import models.Book;
import models.BorrowedBooks;

import java.sql.*;
import java.util.ArrayList;

public class booksController {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USER = "root";
    private static final String PASSWORD = "pass1234";

    public boolean addBook(Book book) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Books (title, author, genre, price, quantity, owner_id) VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setDouble(4, book.getPrice());
            stmt.setInt(5, book.getQuantity());
            stmt.setInt(6, book.getOwnerId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("models.Book added successfully!");
                return true;
            } else {
                throw new SQLException("Failed to add book");
            }
        } catch (SQLException e) {
            throw e;
        }
    }


    public boolean removeBook(int ownerId, int bookId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM Books WHERE owner_id = ? AND id = ?");
            stmt.setInt(1, ownerId);
            stmt.setInt(2, bookId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("models.Book removed successfully!");
            } else if (rowsAffected == 0) {
                System.out.println("No book found for removal.");
            } else {
                throw new SQLException("Unexpected error occurred while removing book");
            }
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }
    public ArrayList<Book> searchBooks(String fieldName, String value) throws SQLException {
        ArrayList<Book> results = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            if (!isValidSearchField(fieldName)) {
                System.out.println("Invalid search field: " + fieldName);
                return results;
            }

            String query = "SELECT * FROM Books WHERE " + fieldName + " = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, value);

            ResultSet bookResults = stmt.executeQuery();

            while (bookResults.next()) {
                int id = bookResults.getInt(1);
                String title = bookResults.getString(2);
                String author = bookResults.getString(3);
                String genre = bookResults.getString(4);
                double price = bookResults.getDouble(5);
                int quantity = bookResults.getInt(6);
                int ownerId = bookResults.getInt(7);

                Book book = new Book(id, title, author, genre, price, quantity, ownerId);
                results.add(book);
            }
        } catch (SQLException e) {
            throw e;
        }

        return results;
    }


    private boolean isValidSearchField(String fieldName) {
        String[] validFields = {"title", "author", "genre","owner_id","id","price","quantity"};
        for (String field : validFields) {
            if (field.equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Book> getAvailableBooks() throws SQLException {
        ArrayList<Book> availableBooks = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Books WHERE quantity > 0";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet bookResults = stmt.executeQuery();

            while (bookResults.next()) {
                int id = bookResults.getInt(1);
                String title = bookResults.getString(2);
                String author = bookResults.getString(3);
                String genre = bookResults.getString(4);
                double price = bookResults.getDouble(5);
                int quantity = bookResults.getInt(6);
                int ownerId = bookResults.getInt(7);

                Book book = new Book(id, title, author, genre, price, quantity, ownerId);
                availableBooks.add(book);
            }
        } catch (SQLException e) {
            throw e;
        }

        return availableBooks;
    }
    public ArrayList<Book> getMyBooks(int userId) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Books WHERE owner_id = ? AND quantity > 0";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet bookResults = stmt.executeQuery();

            while (bookResults.next()) {
                int id = bookResults.getInt(1);
                String title = bookResults.getString(2);
                String author = bookResults.getString(3);
                String genre = bookResults.getString(4);
                double price = bookResults.getDouble(5);
                int quantity = bookResults.getInt(6);
                int ownerId = bookResults.getInt(7);

                Book book = new Book(id, title, author, genre, price, quantity, ownerId);
                books.add(book);
            }
        } catch (SQLException e) {
            throw e;
        }

        return books;
    }
    public Book getBook(int bookId) throws SQLException {
        Book book = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT * FROM Books WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookId);
            ResultSet bookResults = stmt.executeQuery();

            if (bookResults.next()) {
                int id = bookResults.getInt(1);
                String title = bookResults.getString(2);
                String author = bookResults.getString(3);
                String genre = bookResults.getString(4);
                double price = bookResults.getDouble(5);
                int quantity = bookResults.getInt(6);
                int ownerId = bookResults.getInt(7);

                book = new Book(id, title, author, genre, price, quantity, ownerId);
            }
        } catch (SQLException e) {
            throw e;
        }

        return book;
    }
    public ArrayList<BorrowedBooks> listBorrowedBooks() throws SQLException {

        ArrayList<BorrowedBooks> books = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD))
         {
             String q="SELECT b.title, b.author, u.username AS borrower_username " +
                     "FROM Requests r " +
                     "INNER JOIN Books b ON r.book_id = b.id " +
                     "INNER JOIN Users u ON r.borrower_id = u.id " +
                     "WHERE r.status = 'ACCEPTED'";
             PreparedStatement stmt = connection.prepareStatement(q);
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String borrowerUsername = resultSet.getString("borrower_username");
                books.add(new BorrowedBooks(title, author, borrowerUsername));
            }
        }

        return books;
    }





}
