package was.v7;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;

import java.io.IOException;

public class SiteControllerV7 {

    @Mapping("/site1")
    public void site1(HttpRequest request, HttpResponse response) {
        response.writerBody("<h1> site1 </h1>");
    }

    @Mapping("/site2")
    public void site2(HttpRequest request, HttpResponse response) {
        response.writerBody("<h1> site2 </h1>");
    }

    @Mapping("/")
    public void home(HttpRequest request, HttpResponse response) {
        response.writerBody("<h1> home </h1>");
        response.writerBody("<ul>");
        response.writerBody("<li><a href='/site1'>site1</a></li>");
        response.writerBody("<li><a href='/site2'>site2</a></li>");
        response.writerBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writerBody("</ul>");
    }

    @Mapping("/favicon.ico")
    public void favicon(HttpRequest request, HttpResponse response) throws IOException {
        //empty, favicon 처리 용도
    }
}
