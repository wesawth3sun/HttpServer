package was.httpserver;

public class PageNotFoundException extends RuntimeException
{
    public PageNotFoundException(String message) {
      //페이지를 찾지 못했을 때 사용하는 예외
        super(message);
    }
}
