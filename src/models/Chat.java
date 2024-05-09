package models;

public class Chat {

    private int lender_Id;

    private int borrower_id ;
    private String message;

    public Chat(int lender_Id, int borrower_id, String message) {
        this.lender_Id = lender_Id;
        this.borrower_id = borrower_id;
        this.message = message;

    }

    public int getLenderId() {
        return lender_Id;
    }

    public int getBorrower_id() {
        return borrower_id;
    }

    public String getMessage() {
        return message;
    }



    public void setLenderId(int lender_Id) {
        this.lender_Id = lender_Id;
    }

    public void setBorrowerId(int borrower_id ) {
        this.borrower_id = borrower_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
