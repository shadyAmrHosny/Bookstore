import controllers.UserAuth;
import controllers.booksController;
import controllers.chatController;
import controllers.requestController;
import models.Book;
import models.BorrowedBooks;
import models.Chat;
import models.Request;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class server {
    private static final int PORT = 5050;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);


                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try{
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out= new DataOutputStream(clientSocket.getOutputStream());
            boolean isLoggedIn = false;
            out.writeUTF("Welcome to the bookstore server!");
            int currentUser = 0;
            String clientOption = in.readUTF();
            if (clientOption.equals("1")){
                UserAuth auth=new UserAuth("jdbc:mysql://localhost:3306/bookstore","root","pass1234");
                String username=in.readUTF();
                String pass=in.readUTF();
                currentUser=auth.login(username,pass);

                if (currentUser>0) {
                    isLoggedIn=true;
                    out.writeBoolean(isLoggedIn);
                }
                else  {
                    out.writeBoolean(false);
                    //System.out.println(currentUser);
                }
            }

            else if (clientOption.equals("2")){
                UserAuth auth=new UserAuth("jdbc:mysql://localhost:3306/bookstore","root","pass1234");
                String name=in.readUTF();
                String username=in.readUTF();
                String pass=in.readUTF();
                currentUser=auth.signUp(name,username,pass);
                //System.out.println(currentUser);
                if (currentUser>0) {
                    isLoggedIn=true;
                    out.writeBoolean(isLoggedIn);
                }
                else  {
                    out.writeBoolean(false);

                }

            }
            while (true){
                UserAuth auth=new UserAuth("jdbc:mysql://localhost:3306/bookstore","root","pass1234");
                String role = auth.getUserRole(currentUser);
                if (!isLoggedIn){
                    System.out.println("You are not logged in");
                    break;
                }
                clientOption = in.readUTF();
                System.out.println(clientOption);

                if (clientOption.equals("1"))

                {
                    booksController bookController=new booksController();
                 ArrayList<Book> books = bookController.getMyBooks(currentUser);
                    System.out.println(books.size());
                    out.write(books.size());
                    for (int i=0;i<books.size();i++) {
                        out.write(books.get(i).getId());
                        out.writeUTF(books.get(i).getTitle());
                        out.writeUTF(books.get(i).getAuthor());
                        out.writeUTF(books.get(i).getGenre());
                        out.writeDouble(books.get(i).getPrice());
                        out.write(books.get(i).getQuantity());
                        out.write(books.get(i).getOwnerId());
                    }
                }

                else if (clientOption.equals("2")) {

                    booksController bookController=new booksController();
                    ArrayList<Book> books = bookController.getAvailableBooks();
                    System.out.println(books.size());
                    out.writeInt(books.size());
                    for (int i=0;i<books.size();i++) {
                        out.write(books.get(i).getId());
                        out.writeUTF(books.get(i).getTitle());
                        out.writeUTF(books.get(i).getAuthor());
                        out.writeUTF(books.get(i).getGenre());
                        out.writeDouble(books.get(i).getPrice());
                        out.write(books.get(i).getQuantity());
                        out.write(books.get(i).getOwnerId());
                    }
                }
                else if (clientOption.equals("3"))

                {
                    boolean check=false;
                    booksController booksController=new booksController();
                    Book book = new Book();
                    book.setTitle(in.readUTF());
                    book.setAuthor(in.readUTF());
                    book.setGenre(in.readUTF());
                    book.setPrice(in.readDouble());
                    book.setQuantity(in.read());
                    book.setOwnerId(currentUser);
                    check=booksController.addBook(book);

                    System.out.println(check);
                    if (check){
                        out.writeUTF("models.Book added");
                    }else {
                        out.writeUTF("models.Book not added");
                    }
                }
                else if (clientOption.equals("4"))
                {
                    booksController booksController=new booksController();
                    int id = in.read();
                    boolean x = booksController.removeBook(currentUser,id);
                    if (x) out.writeUTF("book deleted successfully");
                    else out.writeUTF("book not found");

                }

                else if (clientOption.equals("5"))
                {
                    Book book=new Book();
                    booksController booksController=new booksController();
                    int book_id,lender_id;
                    String bookName;
                    book_id=in.read();
                    System.out.println(book_id);
                    book=booksController.getBook(book_id);
                    lender_id=book.getOwnerId();
                    bookName=book.getTitle();

                    System.out.println(lender_id);
                    requestController requestController=new requestController();
                    Request req=new Request();
                    req.setBookId(book_id);
                    req.setLenderId(lender_id);
                    req.setBorrowerId(currentUser);
                    req.setBookName(bookName);
                    req.setStatus("PENDING");
                    requestController.submitRequest(req);
                    out.writeUTF("your request submitted successfully ");

                }
                else if (clientOption.equals("6")) {
                   requestController requestController=new requestController();
                    ArrayList<Request> requests = requestController.getRequestHistory(currentUser);
                    System.out.println(requests.size());
                    out.writeInt(requests.size());
                    for (int i=0;i<requests.size();i++) {
                        out.write(requests.get(i).getId());
                        out.writeUTF(requests.get(i).getBookName());
                        out.write(requests.get(i).getBookId());
                        out.write(requests.get(i).getLenderId());
                        out.writeUTF(requests.get(i).getStatus());
                    }
                }

                else if (clientOption.equals("7")) {
                    requestController requestController=new requestController();
                    ArrayList<Request> requests=requestController.getIncomingRequests(currentUser);
                   // requests = requestService.showRequest(userDto.getId());
                    out.write(requests.size());
                    for ( int i=0;i<requests.size();i++){
                        System.out.println(requests.get(i).getBookName());
                        out.write(requests.get(i).getId());
                        out.write(requests.get(i).getBorrowerId());
                        out.writeUTF(requests.get(i).getBookName());
                        out.writeUTF(requests.get(i).getStatus());
                       // out.writeUTF(requestDto.getDate().toString());
                    }
                    int reqID = in.readInt();
                    if (reqID>0){
                        String Status = in.readUTF();
                        requestController.acceptRejectRequest(reqID,Status);
                        out.writeUTF("THE REQUEST IS "+Status);
                        if (Status.equals("ACCEPTED")){
                            chatController chatController=new chatController();
                            String message=in.readUTF();
                            System.out.println(message);
                            int borrowerID=requestController.getBorrowerIdFromReqId(reqID);
                           if (chatController.sendMessage(borrowerID,currentUser,message)){
                               out.writeUTF("message sent");
                           }
                           else out.writeUTF("failed to sent the message");

                        }
                    }
                }
                else if (clientOption.equals("8")) {
                    System.out.println("search");
                    booksController booksController=new booksController();
                    String field="";
                    field=in.readUTF();
                    System.out.println(field);
                    String value="";
                    value=in.readUTF();
                    System.out.println(value);
                    ArrayList<Book> books=booksController.searchBooks(field,value);
                    System.out.print(books.size());
                    out.write(books.size());
                    for (int i=0;i<books.size();i++) {
                        out.write(books.get(i).getId());
                        out.writeUTF(books.get(i).getTitle());
                        out.writeUTF(books.get(i).getAuthor());
                        out.writeUTF(books.get(i).getGenre());
                        out.writeDouble(books.get(i).getPrice());
                        out.write(books.get(i).getQuantity());
                        out.write(books.get(i).getOwnerId());
                    }

                }
                else if (clientOption.equals("9")) {
                    if (role.equals("admin")){
                        out.writeBoolean(true);
                        booksController booksController=new booksController();
                        ArrayList<BorrowedBooks> borrowedBooks =booksController.listBorrowedBooks();
                        System.out.print(borrowedBooks.size());
                        out.write(borrowedBooks.size());
                        if (borrowedBooks.isEmpty()) out.writeUTF("There are currently no borrowed books.");
                        else {
                            for (BorrowedBooks books : borrowedBooks){
                                out.writeUTF(books.getTitle());
                                out.writeUTF(books.getAuthor());
                                out.writeUTF(books.getBorrowerUsername());

                            }
                        }
                        ArrayList<Book> books;
                        books =booksController.getAvailableBooks();
                        out.write(books.size());
                        for (Book book : books){
                            out.write(book.getId());
                            out.writeUTF(book.getTitle());
                            out.writeUTF(book.getAuthor());
                            out.writeUTF(book.getGenre());
                            out.writeDouble(book.getPrice());
                            out.write(book.getQuantity());
                            out.write(book.getOwnerId());
                        }
                        ArrayList<Request> requests;
                        requestController requestController=new requestController();
                        requests = requestController.listAllRequests();
                        out.write(requests.size());
                        for (Request request : requests){
                            System.out.println(request.getBookName());
                            out.write(request.getId());
                            out.write(request.getBorrowerId());
                            out.writeUTF(request.getBookName());
                            out.writeUTF(String.valueOf(request.getStatus()));
                        }
                    }else {
                        out.writeBoolean(false);
                        out.writeUTF("You are not authorized");

                    }
                }
                else if (clientOption.equals("10")) {
                    chatController chatController=new chatController();
                    ArrayList<Chat> messages =chatController.getMessage(currentUser);
                    System.out.println(messages.size());
                    int size =messages.size();
                    out.write(size);
                    for (Chat message : messages){
                        System.out.println(message.getMessage());
                        out.write(message.getLenderId());
                        out.writeUTF(message.getMessage());
                    }
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
