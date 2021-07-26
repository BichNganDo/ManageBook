package main;

import servlets.book.ManageBookServlet;
import servlets.book.BookApiServlet;
import servlets.book.AddBookServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.reader.AddReaderServlet;
import servlets.reader.ManageReaderServlet;
import servlets.reader.ReaderApiServlet;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new BookApiServlet()), "/api/book");
        context.addServlet(new ServletHolder(new ReaderApiServlet()), "/api/reader");

        context.addServlet(new ServletHolder(new ManageBookServlet()), "/managebook");
        context.addServlet(new ServletHolder(new ManageReaderServlet()), "/managereader");

        context.addServlet(new ServletHolder(new AddBookServlet()), "/addbook");
        context.addServlet(new ServletHolder(new AddReaderServlet()), "/addreader");

        ContextHandler resourceHandler = new ContextHandler("/static");
        String resource = "./public";
        if (!resource.isEmpty()) {
            resourceHandler.setResourceBase(resource);
            resourceHandler.setHandler(new ResourceHandler());
        }

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(8080);

        server.setHandler(handlers);

        server.start();

        System.out.println("Server started");

        server.join();
    }
}
