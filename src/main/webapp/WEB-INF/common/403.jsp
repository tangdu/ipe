<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>没有权限访问</title>
</head>
<body class="page-404-full-page" ryt13217="1">
<div>
    <div class="span12 page-404">
        <div class="number">
            403
        </div>
        <div class="details">
            <h3>对不起，你有没权限访问此页面.</h3>
            <p>
                <a href="<%=request.getContextPath()%>/">返回</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>