package was.httpserver;

import java.io.PrintWriter;

import static java.nio.charset.StandardCharsets.*;

public class HttpResponse {

    //응답 메서드 기본
    //HTTP/1.1 200 OK //시작 라인
    //Content-Type: text/html //헤더
    //Content-Length: 20

    //<h1>Hello World</h1>

    //응답을 쓰기 위한 PrintWriter 객체
    private final PrintWriter writer;
    //HTTP 상태 코드를 저장
    private int statusCode = 200;
    //메시지 바디를 StringBuilder 객체로 만든다
    private final StringBuilder bodyBuilder = new StringBuilder();
    //응답의 Content-Type을 지정 (헤더 부분에 속함)
    private String contentType = "text/html; charset=UTF-8";

    public HttpResponse(PrintWriter writer) {
        this.writer = writer;
    }

    //HTTP 상태 코드를 설정
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    //응답의 Content-Type을 설정
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    //응답 본문에 내용을 추가
    public void writeBody(String body) {
        bodyBuilder.append(body);
    }

    //응답을 구성하고 클라이언트에게 전송
    public void flush() {
        //메시지 보내기 전에 바디에 들어있는 글자 길이를 여기서 계산해 줌
        int contentLength = bodyBuilder.toString().getBytes(UTF_8).length;
        //상태 라인을 작성
        writer.println("HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode));
        //헤더 작성 Content-Type, Content-Length
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + contentLength);
        writer.println(); //한 칸 띄워야 한다 (빈 줄을 추가하여 헤더와 본문을 구분)
        writer.println(bodyBuilder); //응답 본문을 작성하고
        writer.flush(); // 실제로 데이터를 전송
    }

    //상태 코드에 해당하는 상태 메시지를 반환
    private String getReasonPhrase(int statusCode) {
        switch (statusCode) {
            case 200:
                return "OK";
            case 404:
                return "Not Found";
            case 500:
                return "Internal Server Error";
            default:
                return "Unknown Status";
        }
    }
}
