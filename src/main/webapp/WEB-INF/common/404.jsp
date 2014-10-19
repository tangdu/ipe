<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>404 打不到该页面</title>
    <link href="<%=request.getContextPath()%>/resources/images/error.css" rel="stylesheet">
</head>
<body class="page-404-full-page" ryt13217="1">
<div>
    <div class="span12 page-404">
        <div class="number">
            404
        </div>
        <div class="details">
            <h3>哦, You're lost.</h3>
            <p>
                我们找不到你要查看的页面.<br>
                <a href="<%=request.getContextPath()%>/">返回</a>
            </p>
        </div>
    </div>
</div>
</body>
</html>