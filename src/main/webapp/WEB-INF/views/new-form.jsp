<%--
  Created by IntelliJ IDEA.
  User: 안성준
  Date: 2022-01-17
  Time: 오후 6:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!--상대경로 사용(/없이 시작), [현재 URL이 속한 계층 경로 + /save]-->
<form action="save" method="post">
    username: <input type="text" name="username" />
    age:      <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
