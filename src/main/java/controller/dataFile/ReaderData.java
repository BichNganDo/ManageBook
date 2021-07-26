
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
import model.Reader;

/**
 *
 * @author Ngan Do
 */
public class ReaderData {

    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;
    private Scanner scanner;

    public static ReaderData INSTANCE = new ReaderData();

    //Viết
    private void openFileToWrite(String fileName) {
        try {
            fileWriter = new FileWriter(fileName, true);//true: cho phép ghi thêm
            bufferedWriter = new BufferedWriter(fileWriter);
            printWriter = new PrintWriter(bufferedWriter);
        } catch (IOException ex) {
            Logger.getLogger(ReaderData.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void writeReaderToFile(Reader reader, String fileName) {
        openFileToWrite(fileName);
        printWriter.println(reader.getReaderID() + "|" + reader.getFullName() + "|"
                + reader.getAddress() + "|" + reader.getPhoneNumber());
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

    public ArrayList<Reader> readReaderFromFile(String fileName) {
        openFileToRead(fileName);
        ArrayList<Reader> readers = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            Reader reader = createReaderFromData(data);
            readers.add(reader);
        }
        closeFileAferRead(fileName);
        return readers;
    }

    public Reader createReaderFromData(String data) {
        String[] datas = data.split("\\|");
        Reader reader = new Reader(Integer.parseInt(datas[0]), datas[1], datas[2], datas[3]);
        return reader;
    }

    private void closeFileAferRead(String fileName) {
        try {
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
