package was.v8;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;

public class SiteControllerV8 {

    //변수를 동적으로 받기 때문에 필요없는 매개변수는 다 지워줘도 됨
    @Mapping("/site1")
    public void site1(HttpResponse response) {
        response.writerBody("<h1> site1 </h1>");
    }

    @Mapping("/site2")
    public void site2(HttpResponse response) {
        response.writerBody("<h1> site2 </h1>");
    }

    @Mapping("/")
    public void home(HttpResponse response) {
        response.writerBody("<h1> home </h1>");
        response.writerBody("<ul>");
        response.writerBody("<li><a href='/site1'>site1</a></li>");
        response.writerBody("<li><a href='/site2'>site2</a></li>");
        response.writerBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writerBody("</ul>");
    }

    @Mapping("/favicon.ico")
    public void favicon() throws IOException {
        //empty, favicon 처리 용도
    }
}
