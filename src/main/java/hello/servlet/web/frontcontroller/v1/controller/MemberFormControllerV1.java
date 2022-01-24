package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.web.frontcontroller.v1.ControllerV1;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberFormControllerV1 implements ControllerV1 {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";//WEB-INF/에 있는 자원들은 url로 호출해도 호출되지 않는다. 항상 컨트롤러를 거쳐서 가야한다
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); //viewPath로 이동하겠다는 뜻.
        dispatcher.forward(request, response);//다른 서블릿이나 JSP로 이동할 수 있는 기능이다. '서버 내부'에서 다시 호출이 발생한다.(client를 갔다오는게 아니다)
    }
}
