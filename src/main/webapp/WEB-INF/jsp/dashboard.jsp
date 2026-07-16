<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" ng-app="ermApp">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>${pageTitle} | ERM Complexity Demo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/erm.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.8.3/angular.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/js/erm-app.js"></script>
</head>
<body ng-controller="DashboardCtrl as vm" ng-init="vm.boot()">
<header class="topbar">
    <div class="brand">
        <span class="brand-mark">ERM</span>
        <div>
            <h1>Technical &amp; Business Complexity</h1>
            <p class="tagline">Java 8 · Spring MVC · JSP · AngularJS · Oracle / PostgreSQL — demo &lt;5K LOC</p>
        </div>
    </div>
    <nav>
        <a class="active" href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/risks">Risk Register</a>
        <a href="${pageContext.request.contextPath}/integrations">Integrations</a>
        <a href="${pageContext.request.contextPath}/security">Security</a>
        <a href="${pageContext.request.contextPath}/legacy">Legacy Debt</a>
    </nav>
</header>

<main class="layout">
    <section class="hero panel">
        <h2>Miniaturized Enterprise Risk Platform</h2>
        <p>
            Stand-in for a <strong>15+ year</strong> hybrid monolith/microservices estate
            (~2.2M LOC / 250+ services / 4,500+ APIs). This runnable demo is capped at
            <strong>&lt;5,000 LOC</strong> and keeps the Complexity-table stack and legacy problems visible.
        </p>
        <div class="pill-row" ng-cloak>
            <span class="pill" ng-class="{'up': vm.health.status === 'UP'}">API {{vm.health.status || '…'}}</span>
            <span class="pill">Architecture: hybrid monolith + microservices</span>
            <span class="pill">Dual DB dialects ready</span>
        </div>
    </section>

    <section class="grid-2">
        <article class="panel">
            <h3>Application Scale</h3>
            <table class="data">
                <tr><th>Product Age</th><td>${scale.productAge}</td></tr>
                <tr><th>Demo LOC</th><td>${scale.linesOfCodeDemo}</td></tr>
                <tr><th>Production LOC</th><td>${scale.linesOfCodeProduction}</td></tr>
                <tr><th>Modules</th><td>${scale.productModules} (demo) / 30+ (prod)</td></tr>
                <tr><th>Microservices</th><td>${scale.microservicesNote}</td></tr>
                <tr><th>APIs</th><td>${scale.apisNote}</td></tr>
                <tr><th>DB Tables</th><td>${scale.tablesNote}</td></tr>
                <tr><th>Integrations</th><td>${scale.integrationsNote}</td></tr>
                <tr><th>Regions</th><td>${scale.regionsNote}</td></tr>
            </table>
        </article>

        <article class="panel">
            <h3>Application Complexity (Tech Stack)</h3>
            <ul class="stack-list">
                <c:forEach items="${techStack}" var="t">
                    <li>${t}</li>
                </c:forEach>
            </ul>
            <h3 class="mt">Legacy Technology Domains</h3>
            <div class="domain-grid">
                <c:forEach items="${domains}" var="d">
                    <a class="domain-card" href="${pageContext.request.contextPath}/risks?domain=${d.code}">
                        <span class="code">${d.code}</span>
                        <span>${d.label}</span>
                        <em>${domainCounts[d.label]} open</em>
                    </a>
                </c:forEach>
            </div>
        </article>
    </section>

    <section class="panel warn">
        <h3>Technical Challenges (Legacy Problems)</h3>
        <p class="muted">Surfaced in code under <code>com.erm.legacy.legacy</code> and on the Legacy Debt page.</p>
        <ul class="challenge-list">
            <c:forEach items="${challenges}" var="c">
                <li>
                    <span class="sev sev-${c.severity}">${c.severity}</span>
                    <strong>${c.title}</strong>
                    <span>${c.detail}</span>
                </li>
            </c:forEach>
        </ul>
    </section>

    <section class="grid-3">
        <article class="panel">
            <h3>Operational Complexity</h3>
            <ul>
                <c:forEach items="${ops}" var="o"><li>${o}</li></c:forEach>
            </ul>
        </article>
        <article class="panel">
            <h3>Business Complexity</h3>
            <ul>
                <c:forEach items="${business}" var="b"><li>${b}</li></c:forEach>
            </ul>
        </article>
        <article class="panel">
            <h3>Service-Level Expectations</h3>
            <ul>
                <c:forEach items="${sla}" var="s"><li>${s}</li></c:forEach>
            </ul>
        </article>
    </section>

    <section class="panel" ng-cloak>
        <h3>Live Risk Feed (AngularJS)</h3>
        <p class="muted">Loaded via <code>/api/risks</code> — Spring MVC REST + AngularJS 1.8.</p>
        <table class="data" ng-if="vm.risks.length">
            <thead>
            <tr><th>ID</th><th>Title</th><th>Domain</th><th>Severity</th><th>Status</th></tr>
            </thead>
            <tbody>
            <tr ng-repeat="r in vm.risks">
                <td>{{r.riskId}}</td>
                <td>{{r.title}}</td>
                <td>{{r.domain}}</td>
                <td><span class="sev" ng-class="'sev-' + r.severity">{{r.severity}}</span></td>
                <td>{{r.status}}</td>
            </tr>
            </tbody>
        </table>
    </section>
</main>

<footer class="footer">
    ERM Complexity Demo · hybrid architecture · acquired-product debt markers intentional · not for production
</footer>
</body>
</html>
