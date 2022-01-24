package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {

    //Adapter가 Controller를 받아들일수 있는지 여부를 반환
    @Override
    public boolean support(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws ServletException, IOException {

        ControllerV4 controllerV4 = (ControllerV4) handler;//support에서 한번 검증을 했기때문에 casting해도 문제 없다.

        Map<String, String> paramMap = createParamMap(request);
        HashMap<String, Object> model = new HashMap<>();

        String viewName = controllerV4.process(paramMap, model);

        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    //자잘한 부분은 service method의 level을 맞춰주기 위해서 메소드로 뺐다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();

        /**
         * String username = request.getParameter("username");
         * int age = Integer.parseInt(request.getParameter("age"));
         * 역활을 해주는 부분.
         */

        //request getParameter로 모두 꺼내와서 paramMap에 다 넣어줌.
        request.getParameterNames().asIterator().forEachRemaining(
            paramName -> paramMap.put(paramName, request.getParameter(paramName))
        );
        return paramMap;
    }

}
