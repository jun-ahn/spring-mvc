@ServletComponentScan :자동으로 서블릿 등록
HttpServlet을 extends해야함
override해

http요청이 오면 servlet container가 request, response 객체를 만들어서 servlet에 던져준다

서버에게 보내는 요청 메세지를 하나씩 보고 싶을때
```
application.properties
logging.level.org.apache.coyote.http11=debug
```

Servlet

기본 사용법
HttpServlet 기능
http임시저장소 기능
http ltfecycle(요청 후 응답할때까지)동안 사용할 임시 저장소 제공
resquest.setAttribute

@WebServlet으로 동작 
`@WebServlet(name = "memberFormServlet", urlPatterns = "/servlet/members/new-form")`
locahost:8080/servlet/members/new-form을 입력하면 해당 페이지가 매핑됨

Servlet
Http를 동적으로 만들수 있다. 
```java
@WebServlet(name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {
    ...
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();
        ...
        for (Member member : members) {
            w.write("    <tr>");
            w.write("        <td>"+member.getId()+"</td>");
            w.write("        <td>"+member.getUsername()+"</td>");
            w.write("        <td>"+member.getAge()+"</td>");
            w.write("    </tr>");
        }
        ...
    }
}
 
```
하지만 Http를 다 java로 찍어줘야 해서 불편하다 -> Template engine을 사용 (대표적으로 JSP, timeleaf)

JSP
JSP는 servlet으로 변환된다 -> Servlet의 불편한점을 해결.
HTML은 HTML대로.. JSP는 JSP대로 하면 되지만, 비즈니스와 뷰가 합쳐져있어서 유지보수가 매우 불편하다.
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>
<%
    //request, response 사용 가능
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    ..
    Member member = new Member(username, age);

%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
```
Servlet과 JSP 공통의 약점: 하나의 Servlet이나, JSP가 너무 많은 역할을 한다.

MVC패턴
- Servlet과 JSP의 장점만을 취한다. 
- Servlet을 Controller, JSP를 View, Model은 HttpReqeust객체 
- jsp파일에 전용 태그를 쓰는 등 조금 더 편해졌다
```java
@WebServlet(name = "mvcMemberForServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberForServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";//WEB-INF/에 있는 자원들은 url로 호출해도 호출되지 않는다. 항상 컨트롤러를 거쳐서 가야한다
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath); //viewPath로 이동하겠다는 뜻.
        dispatcher.forward(request, response);//다른 서블릿이나 JSP로 이동할 수 있는 기능이다. '서버 내부'에서 다시 호출이 발생한다.(client를 갔다오는게 아니다)
    }
}
```
JSP는 전용 태그를 쓴다. 
```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --> 이것 덕분에 전용 tag를 쓸 수 있게 되었다
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<a href="/index.html">메인</a>
<table>
    <thead>
    <th>id</th>
    <th>username</th>
    <th>age</th>
    </thead>
    <tbody>
        <c:forEach var="item" items="${members}">
            <tr>
                <td>${item.id}</td>
                <td>${item.username}</td>
                <td>${item.age}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>
```
한계
forawrd중복
viewPath중복
사용하지않는 코드 (response)
공통처리가 어렵다 -> 해결 공통 기능을 처리하는 것을 만들어서 컨트롤러에 들어가기 전에 공통 기능을 처리하는 것을 호출 -> front controller

Frontcontroler Pattern 

V1: 프론트컨트로로러 도입. 기존 구조를 최대한 유지
서블릿 하나로 client의 요청을 받음
입구는 하나
공통처리 가능
프론트 컨트롤러를 제외한 나머지는 서블릿을 사용하지 않아도 됨

V2: View 분류
View의 중복을 막기위해서 View를 전담으로 처리하는 것을 만들것이다. 

V3: Model추가 
서블릿 종속성 제고
뷰 이름 중복 제거

V4:
ModelView
유연한 컨트롤러
V5
여러가지 Controller를 사용하기 위해서 어댑터 패턴을 사용한다. 
이전에는 FrontController에서 직접 Controller를 호출해주었지만, 앞으로는 여기서는 호출해준다

V5
클라이언트 요청하면 핸들러를 일단 뒤짐. gethadler -> 가지고 와서 핸들 호출 -> controller를 바꾸고 controller의 process를 호출 그럼 modelview를 반환 그다음에 viewresolver호출 

