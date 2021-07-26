/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.datafile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;
import model.BookReaderManagement;
import model.Reader;

/**
 *
 * @author Ngan Do
 */
public class BRMData {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    private Scanner scanner;

    public static BRMData INSTANCE = new BRMData();

    //Viết
    public void openFileToWrite(String fileName) {
        try {
            fileWriter = new FileWriter(fileName, true);//true: cho phép ghi thêm
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
        } catch (IOException ex) {
            Logger.getLogger(BRMData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void writeBRMToFile(BookReaderManagement brm, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(brm.getReader().getReaderID() + "|" + brm.getBook().getBookID() + "|"
                + brm.getNumOfBorrow() + "|" + brm.getState());
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
    public void openFileToRead(String fileName) {
        try {
            scanner = new Scanner(Paths.get(fileName), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<BookReaderManagement> readBRMFromFile(String fileName) {
        ArrayList<Book> books = BookData.INSTANCE.readBooksFromFile("BOOK.DAT");
        ArrayList<Reader> readers = ReaderData.INSTANCE.readReaderFromFile("READER.DAT");
        openFileToRead(fileName);
        ArrayList<BookReaderManagement> brms = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            BookReaderManagement brm = createBRMFromData(data, readers, books);
            brms.add(brm);
        }
        closeFileAferRead(fileName);
        return brms;
    }
    
    
    //phương thức này trả về một đối tượng Book trong danh sách với id cho trước
    private static Book getBook(ArrayList<Book> books, int bookID) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookID() == bookID) {
                return books.get(i);
            }
        }
        return null;
    }

    //phương thức này trả về một đối tượng Reader trong danh sách với id cho trước
    private static Reader getReader(ArrayList<Reader> readers, int readerID) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i).getReaderID() == readerID) {
                return readers.get(i);
            }
        }
        return null;

    }

    public BookReaderManagement createBRMFromData(String data,
            ArrayList<Reader> readers,
            ArrayList<Book> books) {
        String[] datas = data.split("\\|");
        BookReaderManagement brm = new BookReaderManagement(
                getBook(books,Integer.parseInt(datas[1])),
                getReader(readers,Integer.parseInt(datas[0])),
                Integer.parseInt(datas[2]),
                datas[3],
                0);

        return brm;
    }

    public void closeFileAferRead(String fileName) {
        try {
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBRMFile(ArrayList<BookReaderManagement> list, String fileName) {
        //xóa bỏ file cũ
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();//xóa nó đi
        }
        //ghi mới file này
        openFileToWrite(fileName);
        for (BookReaderManagement brm : list) {
            printWriter.println(brm.getReader().getReaderID() + "|"
                    + brm.getBook().getBookID() + "|"
                    + brm.getNumOfBorrow() + "|"
                    + brm.getState());
        }

        closeFileAferWrite(fileName);
    }

}
