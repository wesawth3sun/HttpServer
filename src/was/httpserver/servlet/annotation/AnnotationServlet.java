package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationServlet implements HttpServlet {

    //SiteController, SearchController 받을 용도
    private final List<Object> controllers;

    public AnnotationServlet(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (Object controller : controllers) {
            //컨트롤러들에서 클래스 정보를 가져옴 -> 메소드 이름 가져오기 위해서
            Class<?> aClass = controller.getClass();
            //어떤 컨트롤러 클래스에 있는 모든 메서드를 다 가져옴
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                //메서드 이름을 가져오기 = path랑 비교하기 위해서
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping  = method.getAnnotation(Mapping.class);
                    String value = mapping.value();
                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        //여기서 리턴해서 빠져나가지 않으면 밑에 있는 페이지 찾을 수 없음 문구도 같이 출력된다 주의!!!
                        return;
                    }
                }
            }
        }
        throw new PageNotFoundException("request: " + path);
    }

    //매개변수 위치 바꾸는 법 맥 기준 fn + command + f6
    private void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        try {
           method.invoke(controller, request, response);
       } catch (IllegalAccessException | InvocationTargetException e) {
           throw new RuntimeException(e);
       }
    }
}
