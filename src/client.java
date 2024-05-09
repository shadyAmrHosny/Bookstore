
import java.io.*;
import java.net.*;
import  java.util.Scanner;

public class client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5050;

    public static void main(String[] args) {
        Scanner cin=new Scanner(System.in);
        String options;
        boolean isLoggedIn = false;
        boolean isAdmin;
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out= new DataOutputStream(socket.getOutputStream());
            String s = in.readUTF();
            System.out.println(s);
            System.out.println("1- login" +"\n"+
                    "2- sign up" );
            options = cin.nextLine();
            if (options.equals("1")){
                out.writeUTF(options);
                System.out.println("Enter your Username");
                String userName; userName = cin.nextLine();
                System.out.println("Enter Your Password");
                String password; password = cin.nextLine();
                out.writeUTF(userName);
                out.writeUTF(password);
                isLoggedIn = in.readBoolean();
                System.out.println(isLoggedIn);
                if (!isLoggedIn){
                    System.out.println("password or username not valid");
                }
            } else if (options.equals("2")) {
                out.writeUTF(options);
                System.out.println("Enter you name :");
                String name; name = cin.nextLine();
                System.out.println("Enter your Username :");
                String userName; userName = cin.nextLine();
                System.out.println("Enter Your Password :");
                String password; password = cin.nextLine();
                out.writeUTF(name);
                out.writeUTF(userName);
                out.writeUTF(password);
                isLoggedIn = in.readBoolean();
                if (!isLoggedIn){
                    System.out.println("Username already exists");
                }
            }
            while (true){
                if (!isLoggedIn){
                    System.out.println("You are not logged in");
                    break;
                }
                System.out.println("1- List My Books\n" +
                        "2- List All Books\n" +
                        "3- Add models.Book\n" +
                        "4- Remove models.Book\n" +
                        "5- models.Request For models.Book\n"+
                        "6- Your models.Request History:\n"+
                        "7- Accept / Reject A models.Request:\n "+
                        "8- Search For A Specific Field\n"+
                        "9- Bookstore Stats ( YOU MUST BE ADMIN)\n"+
                        "10- My Messages\n");
                options = cin.nextLine();
                if (options.equals("1")){
                    out.writeUTF(options);
                    int size = in.read();
                    System.out.println(size);
                    if (size==0){
                        System.out.println("You Don't Have Books Yet");
                    }
                    for (int i = 1; i<=size; i++ ){
                        System.out.println("models.Book "+i);
                        System.out.println("ID : "+in.read());
                        System.out.println("Title : "+in.readUTF());
                        System.out.println("Author : "+in.readUTF());
                        System.out.println("Genre : "+in.readUTF());
                        System.out.println("Price : "+in.readDouble());
                        System.out.println("Quantity : "+in.read());
                        System.out.println("OWNER ID : "+in.read());
                        System.out.println("==============================");
                    }
                }
                else if (options.equals("2")){
                    out.writeUTF(options);
                    int size = in.readInt();
                    System.out.println(size);
                    for (int i = 1; i<=size; i++ ){
                        System.out.println("models.Book "+i);
                        System.out.println("ID : "+in.read());
                        System.out.println("Title : "+in.readUTF());
                        System.out.println("Author : "+in.readUTF());
                        System.out.println("Genre : "+in.readUTF());
                        System.out.println("Price : "+in.readDouble());
                        System.out.println("Quantity : "+in.read());
                        System.out.println("OWNER ID : "+in.read());
                        System.out.println("==============================");
                    }

                }else if (options.equals("3")) {
                    out.writeUTF(options);
                    String title;
                    String author;
                    String genre;
                    double price;
                    int quantity;
                    System.out.println("Enter title :");
                    title = cin.nextLine();
                    out.writeUTF(title);
                    System.out.println("Enter Author :");
                    author = cin.nextLine();
                    out.writeUTF(author);
                    System.out.println("Enter genre :");
                    genre = cin.nextLine();
                    out.writeUTF(genre);
                    System.out.println("Enter price :");
                    price = cin.nextDouble();
                    out.writeDouble(price);
                    System.out.println("Enter quantity :");
                    quantity=cin.nextInt();
                    out.write(quantity);
                    System.out.println(in.readUTF());
                }
                else if (options.equals("4")) {
                    out.writeUTF(options);
                    System.out.println("Enter your book Id :");
                    int id = cin.nextInt();
                    out.write(id);
                    System.out.println(in.readUTF());
                }else if (options.equals("5")){
                    out.writeUTF(options);
                    System.out.println("Enter the Id of the book u want to request :");
                    int id = cin.nextInt();
                    out.write(id);
                    System.out.println(in.readUTF());
                } else if (options.equals("6")) {
                    out.writeUTF(options);
                    int size = in.readInt();
                    if (size==0){
                        System.out.println("You Don't Have Requests Yet");
                    }
                   // System.out.println(size);
                    System.out.println("Your Current requests :");
                    for (int i = 1; i<=size; i++ ){
                        System.out.println("models.Request "+i);
                        System.out.println("models.Request ID "+in.read());
                        System.out.println("BOOK_NAME : "+in.readUTF());
                        System.out.println("BOOK_ID : "+in.read());
                        System.out.println("LENDER_ID : "+in.read());
                        System.out.println("STATUS : "+in.readUTF());
                        System.out.println("==============================");
                    }
                }else if (options.equals("7")) {
                    out.writeUTF(options);
                    int size = in.read();
                    System.out.println(size);
                    for (int i = 1; i<=size; i++ ){

                        System.out.println("models.Request "+i);
                        System.out.println("ID : "+in.read());
                        System.out.println("borrower id : "+in.read());
                        System.out.println("book name : "+in.readUTF());
                        System.out.println("status : "+in.readUTF());
                        System.out.println("==============================");
                    }
                    System.out.println("Enter request id to accept or reject or -1 to exit :");
                    int choose = cin.nextInt();
                    out.writeInt(choose);
                    if (choose==-1){
                        continue;
                    }else {
                        System.out.println("Enter 1 to accept 2 to reject :");
                        choose = cin.nextInt();
                        if (choose==1){
                            out.writeUTF("ACCEPTED");
                            System.out.println(in.readUTF());
                            System.out.println("Send message To The Borrower :");
                            String message = cin.nextLine();
                            if (message.isEmpty()) message=cin.nextLine();
                            out.writeUTF(message);
                            System.out.println(in.readUTF());
                        }
                        else if (choose==2) {
                            out.writeUTF("REJECTED");
                            System.out.println(in.readUTF());
                        }
                    }
                } else if (options.equals("8")) {
                    out.writeUTF(options);
                    System.out.println("Enter THE FIELD NAME U WANt TO SEARCH BY :");
                    String field=cin.nextLine();
                    out.writeUTF(field);

                    System.out.println("ENTER THE VALUE OF THE FIELD");
                   String value= cin.nextLine();
                    out.writeUTF(value);
                    int size = in.read();
                    System.out.println(size);
                    for (int i = 1; i<=size; i++ ){
                        System.out.println("models.Book "+i);
                        System.out.println("ID : "+in.read());
                        System.out.println("Title : "+in.readUTF());
                        System.out.println("Author : "+in.readUTF());
                        System.out.println("Genre : "+in.readUTF());
                        System.out.println("Price : "+in.readDouble());
                        System.out.println("Quantity : "+in.read());
                        System.out.println("OWNER ID : "+in.read());
                        System.out.println("==============================");
                    }
                }
                else if (options.equals("9")) {
                    out.writeUTF(options);
                    isAdmin = in.readBoolean();
                    if (isAdmin){
                        System.out.println("current borrowed books :");
                        int size=in.read();
                        if (size==0){
                            System.out.println(in.readUTF());
                        }else {
                            for (int i = 0;i<size;i++){
                                System.out.println("models.Book Title :" +in.readUTF());
                                System.out.println("models.Book Author :"+in.readUTF());
                                System.out.println("Borrower Username :"+in.readUTF());
                                System.out.println("===========================");
                            }
                        }
                        int size2 = in.read();
                        System.out.println(" Available Books :");
                        System.out.println(size2);
                        for (int i = 1; i<=size2; i++ ){
                            System.out.println("models.Book "+i);
                            System.out.println("ID : "+in.read());
                            System.out.println("Title : "+in.readUTF());
                            System.out.println("Author : "+in.readUTF());
                            System.out.println("Genre : "+in.readUTF());
                            System.out.println("Price : "+in.readDouble());
                            System.out.println("Quantity : "+in.read());
                            System.out.println("Owner ID : "+in.read());
                            System.out.println("==============================");
                        }
                        int size3 = in.read();
                        System.out.println(size3);
                        for (int i = 1; i<=size3; i++ ){
                            System.out.println("models.Request "+i);
                            System.out.println("ID : "+in.read());
                            System.out.println("borrower id : "+in.read());
                            System.out.println("book name : "+in.readUTF());
                            System.out.println("status : "+in.readUTF());
                            System.out.println("==============================");
                        }
                    }else {
                        System.out.println(in.readUTF());
                    }
                }
                else if (options.equals("10")) {
                    out.writeUTF(options);
                    int size = in.read();
                    if (size==0) {
                        System.out.println("there is no message");
                        System.out.println("=========================");
                    }
                    for (int i = 1; i<=size; i++ ){
                        System.out.println("message "+i);
                        System.out.println("Lender ID: "+in.read());
                        System.out.println("message : "+in.readUTF());
                        System.out.println("==============================");
                    }
                }else {
                    throw new RuntimeException("invalid input");
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}