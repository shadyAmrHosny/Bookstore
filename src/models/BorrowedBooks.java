package models;

public class BorrowedBooks {

    private String title;
    private String author;
    private String borrowerUsername;

    public BorrowedBooks() {
    }

    public BorrowedBooks(String title, String author, String borrowerUsername) {
        this.title = title;
        this.author = author;
        this.borrowerUsername = borrowerUsername;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }


    // Setter methods
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

}
