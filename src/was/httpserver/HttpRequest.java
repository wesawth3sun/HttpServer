package was.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequest {
    //HTTP 요청 메시지를 파싱하고 그 내용을 저장하는 객체

    private String method;
    private String path; //URI
    // ?q=hello&key=value -> 형식으로 들어오니까 map 구조로 저장할 것
    private final Map<String, String> queryParameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();

    //생성자, HttpRequest를 생성하면 자동으로 호출됨
    public HttpRequest(BufferedReader reader) throws IOException {
        //parsing: 구조화되지 않은 데이터를 구조화된 형태로 변환하는 작업
        parseRequestLine(reader);
        parseHeader(reader);
        //메시지 바디는 이후에 처리
    }

    //요청 메서드의 시작 라인 GET /search?q=hello HTTP/1.1
    //method = GET, path = search, queryParameters -> q, hello <key, value>
    private void parseRequestLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null) {
            throw new IOException("EOF: No request line received");
            //뭔가 잘못된 요청 메서드가 들어온 것임
        }
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            //GET /search?q=hello HTTP/1.1 이걸 공백 기준으로 자르면
            //GET, /search?q=hello, HTTP/1.1 세 개가 나와야 함
            //세 개가 안 나오면 예외를 던진다
            throw new IOException("Invalid request line: " + requestLine);
        }

        method = parts[0];
        // /search?q=hello -> /search, q=hello 분리
        String[] pathParts = parts[1].split("\\?");
        path = pathParts[0];

        // key=value&key1=value1 이런 식으로 들어온다면
        if (pathParts.length > 1) {
            //key=value, key1=value1 이렇게 또 나눠야 함
            parseQueryParameters(pathParts[1]);
        }
    }


    private void parseQueryParameters(String queryString) {
        for (String param : queryString.split("&")) {
            String[] keyValue = param.split("=");
            //영어가 들어오면 상관없는데 한글이 들어오면 문제가 될 수 있음
            //그러니까 URLDecoder 해서 퍼센트 인코딩 풀어주기
            String key = URLDecoder.decode(keyValue[0], UTF_8);
            //가끔 q= 하고 뒤에 값은 없이 넘어오는 경우도 있어서 삼항 연산자 적용
            //뒤에 값이 있으면 디코드해서 리턴하고 없으면 공백을 보냄
            String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], UTF_8) : "";
            queryParameters.put(key, value);
        }
    }

    //헤더는 Host: localhost:12345
    //Connection: keep-alive
    //Cache-Control: max-age = 0 이런 식으로 전송됨
    private void parseHeader(BufferedReader reader) throws IOException {
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            //라인 한 줄씩 읽어올 건데 공백이 아닐 때까지만 읽어오겠다는 것
            int colonIndex = line.indexOf(':');
            if (colonIndex > 0) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                headers.put(key, value);
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    //HTTP 요청의 쿼리 파라미터를 조회하는 기능
    //String name - 조회하고자 하는 쿼리 파라미터의 이름
    public String getParameter(String name) {
        return queryParameters.get(name);
    }
    /* URL이 http://example.com/search?q=java&page=1일 경우:
    String searchTerm = request.getParameter("q");    // "java" 반환
    String page = request.getParameter("page");       // "1" 반환
    String nonexistent = request.getParameter("xyz"); // null 반환*/

    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", queryParameters=" + queryParameters +
                ", headers=" + headers +
                '}';
    }
}
