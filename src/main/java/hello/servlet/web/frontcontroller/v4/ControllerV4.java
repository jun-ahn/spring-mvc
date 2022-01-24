package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    //Map이 param으로 전달되기 때문에 사용할때는 그냥 꺼내서 사용하면된다.
    String process(Map<String, String> paramMap, Map<String, Object> model);


}
