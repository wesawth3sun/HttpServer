package was.httpserver.servlet.reflection;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;
import was.v7.Mapping;
import was.v7.SearchControllerV7;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionServlet implements HttpServlet {

    //SiteController, SearchController 받을 용도
    private final List<Object> controllers;

    public ReflectionServlet(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        // /site1, /site2, /search 등의 URI path 가 넘어옴
        String path = request.getPath();

        for (Object controller : controllers) {
            //컨트롤러들에서 클래스 정보를 가져옴 -> 메소드 이름 가져오기 위해서
            Class<?> aClass = controller.getClass();
            //어떤 컨트롤러 클래스에 있는 모든 메서드를 다 가져옴
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                //메서드 이름을 가져오기 = path랑 비교하기 위해서
                Mapping annotation = method.getAnnotation(Mapping.class);
                try {
                    if (annotation.value().equals(request.getPath())) {
                        method.invoke(controller, request, response);
                        return;
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Access to method denied", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("Method invocation failed", e);
                }
            }
        }
        throw new PageNotFoundException("request = " + path);
    }
}
