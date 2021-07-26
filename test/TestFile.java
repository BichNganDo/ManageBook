
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestFile {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        FileWriter myWriter = new FileWriter("Test.txt", true);
        myWriter.write("Files in Java might be tricky, but it is fun enough!\n");
        myWriter.close();

        File file = new File("Test.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
}
