package was.v6;

import was.httpserver.HttpServer;
import was.httpserver.HttpServlet;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.servlet.HomeServlet;
import was.v5.servlet.SearchServlet;
import was.v5.servlet.Site1Servlet;
import was.v5.servlet.Site2Servlet;

import java.io.IOException;
import java.util.List;

public class ServerMainV6 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {

        //각 서블릿 클래스들 -> 하나의 클래스는 하나의 기능만 구현 가능한 단점
        //새로 서블릿이 추가되면 메인 클래스에서도 추가해 매핑해 주어야 하는 단점

        HttpServlet reflectionServlet = new ReflectionServlet(List.of(new SiteControllerV6(), new SearchControllerV6()));
        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);

        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
