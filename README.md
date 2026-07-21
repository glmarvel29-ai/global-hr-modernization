# ERM Technical & Business Complexity Demo

Miniaturized **Enterprise Risk Management (ERM)** prototype that mirrors a 15+ year hybrid monolith/microservices estate — scaled to **&lt; 5,000 LOC** (vs production ~2.2M).

This demo encodes the technical debt, dual-database reality, integration sprawl, and security posture typical of long-lived enterprise risk platforms acquired and patched over many years.

---

## Tech stack

| Layer | Technology |
|-------|------------|
| Backend | Java 8 · Spring Boot 2.7 · Spring MVC · Spring Security |
| Frontend | JSP · AngularJS 1.8 · JSTL |
| Database | **H2** (default local) · PostgreSQL · Oracle profiles |
| Packaging | WAR · Embedded Tomcat (Jasper) |

---

## What this project demonstrates

- **9 legacy risk domains** — Operational Risk, Internal Audit, Policy, Compliance, IT/Cyber Risk, Third-Party & Vendor Risk, and more
- **Seeded risk register** with acquired-product ID prefixes (`RG-` / `AP-` / `VS-`)
- **Integration hub stubs** — SAP, Oracle ERP, ServiceNow, Entra ID, Okta, SIEM, IAM, scanners, HRMS
- **Security posture surface** — Zero Trust, MFA, RBAC/ABAC, encryption, secrets, monitoring
- **Explicit legacy debt markers** in code and UI (`/legacy`)

---

## Authentication

All pages and APIs except `/api/health`, `/login`, and static assets require authentication.

Demo users:

| Username | Role |
|----------|------|
| `admin` | `ADMIN` |
| `analyst` | `RISK_ANALYST` |
| `auditor` | `INTERNAL_AUDIT` |
| `vendor` | `VENDOR_ANALYST` |

Password for all demo users: `demo123`

---

## Prerequisites

| Tool | Required | Notes |
|------|----------|-------|
| JDK 8–17 | Yes | Source/target is Java 8 |
| Maven 3.8+ | Yes | Builds WAR and runs Spring Boot |
| Modern browser | Yes | Chrome / Edge / Firefox |
| PostgreSQL / Oracle | No | Optional profiles only |

---

## Quick start

```bash
# Build
mvn -q package -DskipTests

# Run (H2 in-memory — recommended for local viewing)
mvn spring-boot:run
```

Wait for `Started ErmComplexityApplication`, then open:

| Page / API | URL |
|------------|-----|
| Login | http://localhost:8090/login |
| Dashboard | http://localhost:8090/dashboard |
| Risk Register | http://localhost:8090/risks |
| Integrations | http://localhost:8090/integrations |
| Security | http://localhost:8090/security |
| Legacy Debt | http://localhost:8090/legacy |
| Health API | http://localhost:8090/api/health |

Default port is **8090** (configured because 8080 is often busy).

### Optional database profiles

```bash
# H2 console enabled only in local-dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=local-dev

# Production-like profile examples (credentials from env vars)
export ERM_DB_USER=erm
export ERM_DB_PASSWORD=change-me
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
mvn spring-boot:run -Dspring-boot.run.profiles=oracle
```

---

## Project structure

```text
├── docs/
│   ├── START_TEST_AND_ISSUES.md   # Start, test & coverage guide
│   └── COMPLEXITY_ISSUES.md       # Category × issues matrix
├── src/main/java/com/erm/legacy/
│   ├── controller/                # Page + REST controllers
│   ├── integration/               # Integration hub stubs
│   ├── legacy/                    # Explicit debt catalog & shared facade
│   ├── model/                     # Risk, scale, challenge models
│   ├── repository/
│   ├── security/                  # Access-control posture + auth config
│   ├── service/
│   └── web/                       # API/page validation + exception handlers
├── src/main/webapp/WEB-INF/jsp/   # Login, dashboard, risks, integrations, …
├── src/main/resources/
│   ├── application.properties           # H2 default (secure baseline)
│   ├── application-local-dev.properties # local debug profile (H2 console)
│   ├── application-postgres.properties
│   └── application-oracle.properties
└── pom.xml
```

---

## Legacy challenges called out in code

1. Multiple acquired products (`RG-` / `AP-` / `VS-` risk IDs)
2. Hybrid monolithic + microservices architecture
3. Shared business services facade
4. High code dependencies (JSP ↔ AngularJS ↔ Spring MVC)
5. Multiple deployment models / multi-cloud dialect notes
6. Legacy frameworks (Java 8, Spring MVC, JSP, AngularJS)

---

## Documentation

| Doc | Purpose |
|-----|---------|
| [docs/START_TEST_AND_ISSUES.md](docs/START_TEST_AND_ISSUES.md) | Step-by-step start, dependencies, frontend & backend testing |
| [docs/COMPLEXITY_ISSUES.md](docs/COMPLEXITY_ISSUES.md) | Full Complexity-table category → issue mapping |

---

## License

Internal demo / educational sample — not intended for production use.
