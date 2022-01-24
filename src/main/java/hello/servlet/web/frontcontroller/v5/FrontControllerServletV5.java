package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    /**
     * 기존에는 private Map<String, ControllerV4> controllerMap = new HashMap<>();이렇게 했다..
     * 하지만 V3든 V4든 모두 집어넣을 수 있어야 하기 때문에 Object를 넣었다.
     */
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapter = new ArrayList<>();
    
    public FrontControllerServletV5(){
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerAdapters() {
        handlerAdapter.add(new ControllerV3HandlerAdapter());
        handlerAdapter.add(new ControllerV4HandlerAdapter());
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        System.out.println("FrontControllerServletV5.service");

        Object handler = getHandler(request); //핸들러 찾아와

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /**
         * paramMap =
         * key: age value: 12
         * key: username value: aaa
         */

        MyHandlerAdapter adapter = getHadlerAdapter(handler);//핸들러 어댑터 찾아와
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private MyHandlerAdapter getHadlerAdapter(Object handler) {
        System.out.println("FrontControllerServletV5.getHadlerAdapter");

        for (MyHandlerAdapter adapter : handlerAdapter) {
            if (adapter.support(handler)){
                return adapter;
            }
        }
        throw new IllegalArgumentException("hadler adapter를 찾을 수 없습니다", (Throwable) handler);
    }

    private Object getHandler(HttpServletRequest request) {
        System.out.println("FrontControllerServletV5.getHandler");
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        return handlerMappingMap.get(requestURI);
    }

    private MyView viewResolver(String viewPath) {
        System.out.println("FrontControllerServletV5.viewResolver");
        //URL이 변경되어도 이것만 바꾸면 된다..
        return new MyView("/WEB-INF/views/" + viewPath + ".jsp");
    }
}
