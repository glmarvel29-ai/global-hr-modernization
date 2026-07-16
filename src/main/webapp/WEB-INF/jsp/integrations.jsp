<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${pageTitle} | ERM Complexity Demo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/erm.css"/>
</head>
<body>
<header class="topbar">
    <div class="brand">
        <span class="brand-mark">ERM</span>
        <div>
            <h1>Integration Complexity</h1>
            <p class="tagline">SAP · Oracle ERP · ServiceNow · Entra ID · Okta · SIEM · IAM · Scanners · HRMS</p>
        </div>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/risks">Risk Register</a>
        <a class="active" href="${pageContext.request.contextPath}/integrations">Integrations</a>
        <a href="${pageContext.request.contextPath}/security">Security</a>
        <a href="${pageContext.request.contextPath}/legacy">Legacy Debt</a>
    </nav>
</header>

<main class="layout">
    <section class="panel warn">
        <h2>300+ production integrations — 9 simulated here</h2>
        <p class="muted">
            LEGACY PROBLEM: Each acquired product shipped its own adapter; no unified SDK.
            Okta remains a VendorSight leftover beside Microsoft Entra ID.
        </p>
    </section>
    <section class="panel">
        <table class="data">
            <thead>
            <tr><th>System</th><th>Category</th><th>Status</th><th>Legacy note</th></tr>
            </thead>
            <tbody>
            <c:forEach items="${integrations}" var="i">
                <tr>
                    <td>${i.name}</td>
                    <td>${i.category}</td>
                    <td><span class="pill ${i.status == 'CONNECTED' ? 'up' : 'degraded'}">${i.status}</span></td>
                    <td>${i.legacyNote}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</main>
<footer class="footer">ERM Complexity Demo · Integration Hub stubs</footer>
</body>
</html>
