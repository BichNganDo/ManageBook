package model;

import controller.dataFile.BookData;
import controller.dataFile.ReaderData;
import java.util.ArrayList;

public class Reader {

    private static int id = 10000000;

    public static void setID(int id) {
        Reader.id = id;
    }
    private int readerID;
    private String fullName;
    private String address;
    private String phoneNumber;

    public Reader() {
    }

    public Reader(int readerID) {
        this.readerID = readerID;
    }

    public Reader(int readerID, String fullName, String address, String phoneNumber) {
        if (readerID == 0) {
            this.readerID = id++;
        } else {
            this.readerID = readerID;
        }

        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Reader(String fullName, String address, String phoneNumber) {
        ArrayList<Reader> listReaders = ReaderData.INSTANCE.readReaderFromFile("READER.DAT");
        if (listReaders.size() > 0) {
            Reader lastReader = listReaders.get(listReaders.size() - 1);
            this.readerID = lastReader.getReaderID() + 1;
        }
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getReaderID() {
        return readerID;
    }

    public void setReaderID(int readerID) {
        this.readerID = readerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Reader{" + "readerID=" + readerID + ", "
                + "fullName=" + fullName + ", "
                + "address=" + address + ", "
                + "phoneNumber=" + phoneNumber + '}';
    }

}
