package was.v7;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;

public class SiteControllerV7 {

    @Mapping("/site1")
    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1> site1 </h1>");
    }

    @Mapping("/site2")
    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1> site2 </h1>");
    }

    @Mapping("/")
    public void home(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1> home </h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    @Mapping("/favicon.ico")
    public void favicon(HttpRequest request, HttpResponse response) throws IOException {
        //empty, favicon 처리 용도
    }
}
