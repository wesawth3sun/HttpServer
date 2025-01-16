package was.v7;

import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServlet;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.servlet.HomeServlet;
import was.v6.SearchControllerV6;
import was.v6.SiteControllerV6;

import java.io.IOException;
import java.util.List;

public class ServerMainV7 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {

        HttpServlet annotationServlet = new AnnotationServlet(List.of(new SiteControllerV7(), new SearchControllerV7()));
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet );

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
