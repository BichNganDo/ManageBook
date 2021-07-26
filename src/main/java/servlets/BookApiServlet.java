package servlets;

import com.google.gson.Gson;
import common.APIResult;
import controller.dataFile.BookData;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Book;
import org.json.JSONObject;

public class BookApiServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        ArrayList<Book> readBooksFromFile = BookData.INSTANCE.readBooksFromFile("BOOK.DAT");
        ServletUtil.printJson(request, response, gson.toJson(readBooksFromFile));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");
        
        String action = request.getParameter("action");
        switch (action) {
            case "DELETE":
                String bodyData = HttpHelper.getBodyData(request);
                JSONObject jData = new JSONObject(bodyData);
                String idDel = jData.getString("id");
                Book.removeProduct(idDel);
                break;
            case "ADD":
                
                break;
            case "EDIT":

                break;
            default:
                throw new AssertionError();
        }
        
        ServletUtil.printJson(request, response, gson.toJson(result));
    }
    
}
