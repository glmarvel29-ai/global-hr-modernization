<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" ng-app="ermApp">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${pageTitle} | ERM Complexity Demo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/erm.css"/>
    <script src="${pageContext.request.contextPath}/static/js/vendor/angular.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/erm-app.js"></script>
</head>
<body ng-controller="SecurityCtrl as vm" ng-init="vm.boot()">
<header class="topbar">
    <div class="brand">
        <span class="brand-mark">ERM</span>
        <div>
            <h1>Security Complexity</h1>
            <p class="tagline">Zero Trust · MFA · RBAC · ABAC · Encryption · Secrets · SIEM · Vuln Mgmt</p>
        </div>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/risks">Risk Register</a>
        <a href="${pageContext.request.contextPath}/integrations">Integrations</a>
        <a class="active" href="${pageContext.request.contextPath}/security">Security</a>
        <a href="${pageContext.request.contextPath}/legacy">Legacy Debt</a>
    </nav>
</header>

<main class="layout">
    <section class="grid-2">
        <article class="panel">
            <h2>Security Posture (server)</h2>
            <ul class="stack-list">
                <li>Zero Trust Architecture: <strong>ON</strong></li>
                <li>MFA Required: <strong>ON</strong></li>
                <li>RBAC: ADMIN write = <strong>${rbacAdmin}</strong></li>
                <li>ABAC sample (VENDOR_ANALYST + RESTRICTED): <strong>${abacSample}</strong> (expected false)</li>
                <li>Encryption at rest &amp; in transit: <strong>ON</strong></li>
                <li>Secrets: ${posture.secretsManagement}</li>
                <li>Incident monitoring: ${posture.incidentMonitoring}</li>
                <li>Vulnerability mgmt: ${posture.vulnerabilityManagement}</li>
            </ul>
            <p class="muted legacy-callout">${posture.legacyNote}</p>
        </article>
        <article class="panel" ng-cloak>
            <h2>Posture via AngularJS API</h2>
            <p class="muted" ng-if="vm.errorMessage">{{vm.errorMessage}}</p>
            <pre class="codeblock">{{vm.posture | json}}</pre>
        </article>
    </section>
</main>
<footer class="footer">ERM Complexity Demo · AccessControlService stubs</footer>
</body>
</html>
