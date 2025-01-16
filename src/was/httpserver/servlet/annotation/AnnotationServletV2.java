package was.httpserver.servlet.annotation;

import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationServletV2 implements HttpServlet {

    //SiteController, SearchController 받을 용도
    private final List<Object> controllers;

    public AnnotationServletV2(List<Object> controllers) {
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

    //site1, site2 등의 경우에는 매개변수 중 response 만 사용함 -> 그러면 동적으로 읽어들여서 쓰는 것만 받도록 하자
    private void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        try {
            //메서드가 어떤 파라미터 타입을 가지고 있는지 꺼냄
            Class<?>[] parameterTypes = method.getParameterTypes();
            //파라미터 타입만큼의 배열을 생성
            //request, response 둘 다 받는다 -> 2
            //response 하나만 받는다 -? 1
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                //첫 번째 파라미터의 정보가 request의 클래스 정보를 가지고 있으면
                if (parameterTypes[i] == HttpRequest.class) {
                    args[i] = request;
                } else if (parameterTypes[i] == HttpResponse.class) {
                    args[i] = response;
                } else {
                    //우리가 처리할 수 없는 부분
                    throw new IllegalArgumentException("Unsupported patameter type: " + parameterTypes[i]);
                }
            }
            method.invoke(controller, args);
       } catch (IllegalAccessException | InvocationTargetException e) {
           throw new RuntimeException(e);
       }
    }
}
