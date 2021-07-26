package servlets.reader;

import com.google.gson.Gson;
import common.APIResult;
import controller.dataFile.BookData;
import controller.dataFile.ReaderData;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Book;
import model.Reader;
import org.json.JSONObject;

public class ReaderApiServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        ArrayList<Reader> readReaderFromFile = ReaderData.INSTANCE.readReaderFromFile("READER.DAT");
        ServletUtil.printJson(request, response, gson.toJson(readReaderFromFile));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "add": {
                String bodyData = HttpHelper.getBodyData(request);
                JSONObject jData = new JSONObject(bodyData);

                String fullName = jData.optString("fullName");
                String address = jData.optString("address");
                String phoneNumber = jData.optString("phoneNumber");

                Reader reader = new Reader(fullName, address, phoneNumber);
                ReaderData.INSTANCE.writeReaderToFile(reader, "READER.DAT");
                result.setErrorCode(0);
                result.setMessage("Thêm sách thành công!");
                break;
            }
            case "delete":

                break;
            case "edit": {
               
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }
}
