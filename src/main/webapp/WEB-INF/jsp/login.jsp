<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Login | ERM Complexity Demo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/erm.css"/>
</head>
<body>
<main class="layout">
    <section class="panel" style="max-width: 480px; margin: 4rem auto;">
        <h1>ERM Demo Login</h1>
        <p class="muted">Sign in with demo credentials to access protected pages and APIs.</p>

        <c:if test="${error}">
            <p class="legacy-callout">Invalid username or password.</p>
        </c:if>
        <c:if test="${logout}">
            <p class="muted">You have been signed out.</p>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <label for="username">Username</label>
            <input id="username" name="username" type="text" required autofocus />

            <label for="password">Password</label>
            <input id="password" name="password" type="password" required />

            <button type="submit">Sign In</button>
        </form>

        <p class="muted" style="margin-top: 1rem;">Demo users: admin, analyst, auditor, vendor (password: demo123)</p>
    </section>
</main>
</body>
</html>
