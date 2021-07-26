package view;

import controller.ControllerUltility;
import controller.dataFile.BRMData;
import controller.dataFile.BookData;
import controller.dataFile.ReaderData;
import java.util.ArrayList;
import java.util.Scanner;
import model.Book;
import model.BookReaderManagement;
import model.Reader;

public class View {

    public static void main(String[] args) {
        int choice = 0;

        String booksFileName = "BOOK.DAT";
        BookData bookData = new BookData();
        ArrayList<Book> books = new ArrayList<>();
        boolean isBookChecked = false;

        String readersFileName = "READER.DAT";
        ReaderData readerData = new ReaderData();
        ArrayList<Reader> readers = new ArrayList<Reader>();
        boolean isReaderChecked = false;

        String brmFileName = "BRM.DAT";
        BRMData brmData = new BRMData();
        ArrayList<BookReaderManagement> brms = new ArrayList<>();

        ControllerUltility ultility = new ControllerUltility();

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("--------------MENU--------------");
            System.out.println("0.Thoát khỏi ứng dụng");
            System.out.println("1.Thêm 1 đầu sách vào file");
            System.out.println("2.Hiển thị danh sách các sách có trong file");
            System.out.println("3.Thêm 1 bạn đọc vào file");
            System.out.println("4.Hiển thị danh sách các bạn đọc có trong file");
            System.out.println("5.Lập thông tin quản lý mượn");
            System.out.println("6.Sắp xếp");
            System.out.println("7.Tìm kiếm thông tin quản lý mượn theo tên bạn đọc");
            System.out.println("Bạn chọn gì?");

            choice = scanner.nextInt();
            scanner.nextLine(); // đọc bỏ dòng chưa lựa chọn
            switch (choice) {
                case 0:
                    System.out.println("-------------------------------------------");
                    System.out.println("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi!");
                    break;
                case 1:
                    if (!isBookChecked) {
                        checkBookID(bookData, booksFileName);
                        isBookChecked = true;
                    }

                    System.out.println("Nhập tên sách:");
                    String bookName = scanner.nextLine();

                    System.out.println("Nhập tên tác giả:");
                    String author = scanner.nextLine();

                    String[] specs = {"Science", "Art", "Economic", "IT"};
                    int choiceSpec;
                    do {
                        System.out.println("Nhập thể loại sách:");
                        System.out.println("1. Science.\n2. Art.\n3. Economic.\n4. IT");
                        choiceSpec = scanner.nextInt();
                    } while (choiceSpec < 1 || choiceSpec > 4);
                    String specialization = specs[choiceSpec - 1];

                    System.out.println("Nhập năm xuất bản:");
                    int publishYear = scanner.nextInt();

                    System.out.println("Nhập số lượng:");
                    int quantity = scanner.nextInt();

                    Book book = new Book(0, bookName, author, specialization,
                            publishYear, quantity);
                    bookData.writeBookToFile(book, booksFileName);

                    break;
                case 2:
                    books = bookData.readBooksFromFile(booksFileName);
                    showBookInfo(books);
                    break;
                case 3:
                    if (!isReaderChecked) {
                        checkreaderID(readerData, readersFileName);
                        isReaderChecked = true;
                    }
                    System.out.println("Nhập họ và tên:");
                    String fullName = scanner.nextLine();

                    System.out.println("Nhập địa chỉ:");
                    String address = scanner.nextLine();

                    String phoneNumber;
                    do {
                        System.out.println("Nhập số điện thoại:");
                        phoneNumber = scanner.nextLine();
                    } while (!phoneNumber.matches("\\d{10}"));

                    Reader reader = new Reader(0, fullName, address, phoneNumber);
                    readerData.writeReaderToFile(reader, readersFileName);
                    break;
                case 4:
                    readers = readerData.readReaderFromFile(readersFileName);
                    showReaderInfo(readers);
                    break;
                case 5:
                    //B0: Khởi tạo danh sách
                    readers = readerData.readReaderFromFile(readersFileName);
                    books = bookData.readBooksFromFile(booksFileName);
                    brms = brmData.readBRMFromFile(brmFileName);
                    //B1: Chọn 1 bạn đọc từ danh sách để cho mượn, 
                    //nếu đã mượn đủ số lượng thì không cho mượn.
                    int readerID,
                     bookID;
                    boolean isBorrowable = false;
                    do {
                        showReaderInfo(readers);
                        System.out.println("----------------------------------");
                        System.out.println("Nhập mã bạn đọc, nhập 0 để bỏ qua");
                        readerID = scanner.nextInt();
                        if (readerID == 0) { //tất cả các bạn đọc đã mượn đủ sách đúng quy định
                            break;
                        }
                        isBorrowable = checkBrrowed(brms, readerID);
                        if (isBorrowable) {
                            break;
                        } else {
                            System.out.println("Bạn đã mượn đủ số lượng cho phép.");
                        }
                    } while (true);

                    //B2:
                    boolean isFull = false;
                    do {
                        showBookInfo(books);
                        System.out.println("----------------------------------");
                        System.out.println("Nhập mã sách, nhập 0 để bỏ qua");
                        bookID = scanner.nextInt();

                        if (bookID == 0) {
                            break;
                        }

                        isFull = checkFull(brms, readerID, bookID);//true nếu đã mượn đủ 3
                        if (isFull) {
                            System.out.println("Vui lòng chọn đầu sách khác.");
                        } else {
                            break;
                        }
                    } while (true);

                    //B3:
                    int total = getTotal(brms, readerID, bookID);
                    do {
                        System.out.println("Nhập số lượng mượn, tối đa 3 cuốn (đã mượn " + total + "):");
                        int x = scanner.nextInt();
                        if (x + total >= 1 && x + total <= 3) {
                            total = total + x;
                            break;
                        } else {
                            System.out.println("Nhập quá số lượng quy định. Vui lòng nhập lại.");
                        }
                    } while (true);
                    scanner.nextLine();//đọc bỏ dòng có chứa số.
                    //B4:
                    System.out.println("Nhập tình trạng:");
                    String status = scanner.nextLine();

                    //B4: 
                    Book currentBook = getBook(books, bookID);
                    Reader currentReader = getReader(readers, readerID);
                    BookReaderManagement b = new BookReaderManagement(currentBook,
                            currentReader, total, status, 0);

                    //B4:
                    brms = ultility.updateBRMInfo(brms, b);//Cập nhật danh sách quản lý mượn
                    BRMData.INSTANCE.updateBRMFile(brms, brmFileName);//Cập nhật file dữ liệu

                    //B5:
                    showBRMInfo(brms);
                    break;
                case 6:
                    brms = BRMData.INSTANCE.readBRMFromFile(brmFileName); //đọc ra danh sách quản lý
                    //update tổng số lượng mượn
                    brms = ultility.updateTotalBorrowed(brms);

                    System.out.println("-----------------------------------------");
                    System.out.println("-------------Các lựa chọn sắp xếp-------------");
                    int x = 0;
                    do {
                        System.out.println("1.Sắp xếp theo tên của bạn đọc (A-Z)");
                        System.out.println("2.Sắp xếp theo tổng số lượng mượn (Giảm dần)");
                        System.out.println("0.Trở lại menu chính");
                        System.out.println("Bạn chọn gì?");
                        x = scanner.nextInt();
                        if (x == 0) {
                            break;
                        }
                        switch (x) {
                            case 1:
                                //sắp xếp theo tên
                                brms = ultility.sortByReaderName(brms);
                                showBRMInfo(brms);
                                break;
                            case 2:
                                //sắp xếp theo số lượng mượn
                                brms = ultility.sortByNumberOfBorrowed(brms);
                                showBRMInfo(brms);
                                break;
                            case 0:
                                break;
                            default:
                                throw new AssertionError();
                        }

                    } while (true);

                    break;
                case 7:
                    brms = BRMData.INSTANCE.readBRMFromFile(brmFileName);
                    System.out.println("Nhập tên bạn đọc cần tìm");
                    String key = scanner.nextLine();
                    ArrayList<BookReaderManagement> result = ultility
                            .searchByReaderName(brms, key);

                    if (result.size() == 0) {
                        System.out.println("Không tìm thấy bạn đọc!");
                    } else {
                        showBRMInfo(result);
                    }
                    break;
                default:
                    throw new AssertionError();
            }
        } while (choice != 0);
    }

    private static void showBookInfo(ArrayList<Book> books) {
        System.out.println("-----Thông tin sách trong file-----");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private static void checkBookID(BookData bookData, String fileName) {
        ArrayList<Book> listBooks = bookData.readBooksFromFile(fileName);
        if (listBooks.size() > 0) {
            Book lastBook = listBooks.get(listBooks.size() - 1);
            Book.setID(lastBook.getBookID() + 1);
        }

    }

    private static void showReaderInfo(ArrayList<Reader> readers) {
        System.out.println("-----Thông tin bạn đọc trong file-----");
        for (Reader reader : readers) {
            System.out.println(reader);
        }
    }

    private static void checkreaderID(ReaderData readerData, String fileName) {
        ArrayList<Reader> listReaders = readerData.readReaderFromFile(fileName);
        if (listReaders.size() > 0) {
            Reader lastReader = listReaders.get(listReaders.size() - 1);
            Reader.setID(lastReader.getReaderID() + 1);
        }

    }

    private static boolean checkBrrowed(ArrayList<BookReaderManagement> brms, int readerID) {
        int total = 0;
        for (BookReaderManagement brm : brms) {
            if (brm.getReader().getReaderID() == readerID) {
                total = total + brm.getNumOfBorrow();
            }
        }
        if (total == 15) {
            return false; //hết lượt mượn
        }
        return true;
    }

    private static boolean checkFull(ArrayList<BookReaderManagement> brms, int readerID, int bookID) {
        for (BookReaderManagement brm : brms) {
            if (brm.getReader().getReaderID() == readerID
                    && brm.getBook().getBookID() == bookID
                    && brm.getNumOfBorrow() == 3) {
                return true;
                //không được mượn tiếp đầu sách
            }
        }
        return false;//được mượn

    }

    private static int getTotal(ArrayList<BookReaderManagement> brms, int readerID, int bookID) {
        for (BookReaderManagement brm : brms) {
            if (brm.getReader().getReaderID() == readerID
                    && brm.getBook().getBookID() == bookID) {
                return brm.getNumOfBorrow();
            }
        }
        return 0;
    }

    private static Book getBook(ArrayList<Book> books, int bookID) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookID() == bookID) {
                return books.get(i);
            }
        }
        return null;
    }

    private static Reader getReader(ArrayList<Reader> readers, int readerID) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).getReaderID() == readerID) {
                return readers.get(i);
            }
        }
        return null;

    }

    private static void showBRMInfo(ArrayList<BookReaderManagement> brms) {
        for (BookReaderManagement brm : brms) {
            System.out.println(brm);
        }

    }
}
