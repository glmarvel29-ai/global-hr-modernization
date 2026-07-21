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
<body ng-controller="RisksCtrl as vm" ng-init="vm.boot()">
<header class="topbar">
    <div class="brand">
        <span class="brand-mark">ERM</span>
        <div>
            <h1>Enterprise Risk Register</h1>
            <p class="tagline">Business Complexity — risk registers across legacy domains</p>
        </div>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a class="active" href="${pageContext.request.contextPath}/risks">Risk Register</a>
        <a href="${pageContext.request.contextPath}/integrations">Integrations</a>
        <a href="${pageContext.request.contextPath}/security">Security</a>
        <a href="${pageContext.request.contextPath}/legacy">Legacy Debt</a>
    </nav>
</header>

<main class="layout">
    <section class="panel">
        <h2>Filter by Legacy Domain</h2>
        <div class="filter-row">
            <a class="chip" href="${pageContext.request.contextPath}/risks">All</a>
            <c:forEach items="${domains}" var="d">
                <a class="chip" href="${pageContext.request.contextPath}/risks?domainCode=${d.key}">${d.code} — ${d.label}</a>
            </c:forEach>
        </div>
        <c:if test="${not empty domainError}">
            <p class="muted legacy-callout">${domainError}</p>
        </c:if>
        <p class="muted legacy-callout">
            LEGACY PROBLEM: Risk IDs keep acquired-product prefixes —
            <code>RG-</code> RiskGuard, <code>AP-</code> AuditPro, <code>VS-</code> VendorSight.
        </p>
    </section>

    <section class="panel">
        <h3>Server-rendered register (JSP / JSTL)</h3>
        <table class="data">
            <thead>
            <tr>
                <th>Risk ID</th><th>Title</th><th>Domain</th><th>Severity</th>
                <th>Status</th><th>Owner</th><th>Description</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${risks}" var="r">
                <tr>
                    <td>${r.riskId}</td>
                    <td>${r.title}</td>
                    <td>${r.domain.label}</td>
                    <td><span class="sev sev-${r.severity}">${r.severity}</span></td>
                    <td>${r.status}</td>
                    <td>${r.owner}</td>
                    <td>${r.description}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>

    <section class="panel" ng-cloak>
        <h3>Client filter (AngularJS)</h3>
        <p class="muted" ng-if="vm.errorMessage">{{vm.errorMessage}}</p>
        <label>Severity
            <select ng-model="vm.severityFilter">
                <option value="">All</option>
                <option value="CRITICAL">CRITICAL</option>
                <option value="HIGH">HIGH</option>
                <option value="MEDIUM">MEDIUM</option>
                <option value="LOW">LOW</option>
            </select>
        </label>
        <table class="data">
            <thead>
            <tr><th>ID</th><th>Title</th><th>Severity</th><th>Status</th></tr>
            </thead>
            <tbody>
            <tr ng-repeat="r in vm.risks | filter:vm.bySeverity">
                <td>{{r.riskId}}</td>
                <td>{{r.title}}</td>
                <td>{{r.severity}}</td>
                <td>{{r.status}}</td>
            </tr>
            </tbody>
        </table>
    </section>
</main>
<footer class="footer">ERM Complexity Demo · Spring MVC + JSP + AngularJS</footer>
</body>
</html>
