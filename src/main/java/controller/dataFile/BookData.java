
package controller.dataFile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;

/**
 *
 * @author Ngan Do
 */
public class BookData {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    private Scanner scanner;

    public static BookData INSTANCE = new BookData();

    //Viết
    private void openFileToWrite(String fileName) {
        try {
            fileWriter = new FileWriter(fileName, true);//true: cho phép ghi thêm
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
        } catch (IOException ex) {
            Logger.getLogger(BookData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeBookToFile(Book book, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(book.getBookID() + "|" + book.getBookName() + "|"
                + book.getAuthor() + "|" + book.getSpecialization() + "|"
                + book.getPublishYear() + "|" + book.getQuantity());
        closeFileAferWrite(fileName);
    }

    private void closeFileAferWrite(String fileName) {
        //đóng từ dưới lên trên.
        try {
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Đọc
    private void openFileToRead(String fileName) {
        try {
            scanner = new Scanner(Paths.get(fileName), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> readBooksFromFile(String fileName) {
        openFileToRead(fileName);
        ArrayList<Book> books = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            Book book = createBookFromData(data);
            books.add(book);
        }
        closeFileAferRead(fileName);
        return books;
    }

    public Book createBookFromData(String data) {
        String[] datas = data.split("\\|");
        Book book = new Book(Integer.parseInt(datas[0]), datas[1], datas[2], datas[3], Integer.parseInt(datas[4]), Integer.parseInt(datas[5]));
        return book;
    }

    private void closeFileAferRead(String fileName) {
        try {
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
