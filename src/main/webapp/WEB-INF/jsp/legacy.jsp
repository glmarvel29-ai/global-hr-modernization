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
            <h1>Legacy Technical Challenges</h1>
            <p class="tagline">Documented in <code>com.erm.legacy.legacy.*</code> — intentional debt markers</p>
        </div>
    </div>
    <nav>
        <a href="${pageContext.request.contextPath}/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/risks">Risk Register</a>
        <a href="${pageContext.request.contextPath}/integrations">Integrations</a>
        <a href="${pageContext.request.contextPath}/security">Security</a>
        <a class="active" href="${pageContext.request.contextPath}/legacy">Legacy Debt</a>
    </nav>
</header>

<main class="layout">
    <section class="panel warn">
        <h2>Why this page exists</h2>
        <p>
            The Complexity table lists six technical challenges. This demo encodes them as
            named constants, a shared-business-services facade, dual-DB profiles, and hybrid
            WAR boot — so debt is <em>visible while the app runs</em>, not only in docs.
        </p>
        <p>
            Regional dialect resolution (shared services smell):
            US-EAST → <strong>${dialectUs}</strong>,
            EU-WEST → <strong>${dialectEu}</strong>
        </p>
    </section>

    <section class="panel">
        <ol class="challenge-list numbered">
            <c:forEach items="${challenges}" var="c" varStatus="st">
                <li>
                    <span class="sev sev-${c.severity}">${c.severity}</span>
                    <strong>${st.index + 1}. ${c.title}</strong>
                    <p>${c.detail}</p>
                </li>
            </c:forEach>
        </ol>
    </section>

    <section class="panel">
        <h3>Code anchors</h3>
        <ul>
            <li><code>legacy/LegacyDebtCatalog.java</code> — challenge text constants</li>
            <li><code>legacy/SharedBusinessServices.java</code> — shared-jar smell</li>
            <li><code>application-oracle.properties</code> / <code>application-postgres.properties</code> — dual DB</li>
            <li><code>ErmComplexityApplication</code> — hybrid WAR / Spring Boot sidecar note</li>
        </ul>
    </section>
</main>
<footer class="footer">ERM Complexity Demo · &lt;5K LOC intentional miniaturization</footer>
</body>
</html>
