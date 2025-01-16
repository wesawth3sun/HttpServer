package was.v8;

import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.annotation.AnnotationServletV2;
import was.v7.SearchControllerV7;

import java.io.IOException;
import java.util.List;

public class ServerMainV8 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {

        //이전과 같은데, 서블릿에 들어가는 변수만 동적 바인딩

        HttpServlet annotationServlet = new AnnotationServletV2(List.of(new SiteControllerV8(), new SearchControllerV7()));
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
