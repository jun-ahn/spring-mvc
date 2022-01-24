package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    //매핑정보. 어떤 controller가 실행될지 찾는다
    //기본 url
    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("FrontControllerServletV3.service");

        String requestURI = request.getRequestURI();
        ControllerV3 controller = controllerMap.get(requestURI); //MemberFormControllerV3

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /**
         * paramMap =
         * key: age value: 12
         * key: username value: aaa
         */
        Map<String, String> paramMap = createParamMap(request);
        ModelView modelView = controller.process(paramMap);

        String viewName = modelView.getViewName();
        MyView myView = viewResolver(viewName);

        myView.render(modelView.getModel(), request, response);
    }

    private MyView viewResolver(String viewPath) {
        //URL이 변경되어도 이것만 바꾸면 된다..
        return new MyView("/WEB-INF/views/" + viewPath + ".jsp");
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
