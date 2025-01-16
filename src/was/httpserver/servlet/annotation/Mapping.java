package was.httpserver.servlet.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Mapping {
    //서블릿들에 리플렉션을 적용할 때, 애노테이션을 사용하기
    String value();
}