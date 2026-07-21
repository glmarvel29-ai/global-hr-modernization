---
agent: task-list-agent
cli: Cursor Agent CLI
llm: auto
run_id: 20260721T093117_dzexko
generated_at: 2026-07-21T16:55:52.005Z
---

# 03 Task List - Legacy Monolith Modularization

## Phase 1: Analysis & Design Alignment

- [ ] **T-01 — Create modernization traceability matrix from analysis and design** (priority: high · complexity: medium · depends on: none)
  - **Files:** `docs/modernization/traceability-matrix.md`, `agent-runs/20260721T093117_dzexko/02-design-document.md`
  - **Description:** Build a field-by-field and decision-by-decision mapping that ties detected Java/Spring findings and validation gaps to concrete implementation points before coding starts.
  - **Acceptance criteria:** Matrix maps each field (`domain`, `severity`, `riskId`, `title`, `status`, `owner`, `description`, `region`) and each design decision (service boundaries, error envelope, security redaction) to at least one implementation artifact.
  - **Traceability:** analysis `field_summary`, `field_validations`; design §II, §III, §V, §IX.

- [ ] **T-02 — Add architecture boundary tests for modular layering** (priority: high · complexity: medium · depends on: T-01)
  - **Files:** `pom.xml`, `src/test/java/com/erm/legacy/architecture/ModuleBoundaryArchTest.java`
  - **Description:** Add architecture tests (controller -> application service -> domain service -> repository/adapter) so refactoring enforces distinct service-call boundaries rather than direct cross-layer access.
  - **Acceptance criteria:** Build fails when controllers directly invoke repositories/integration helpers; passing tests confirm allowed dependency directions only.
  - **Traceability:** design §II component boundaries, §VIII Sprint 0 guardrails.

## Phase 2: Foundations & Data Layer

- [ ] **T-03 — Introduce modularization and strict-validation feature flags** (priority: high · complexity: low · depends on: T-01)
  - **Files:** `src/main/resources/application.properties`, `src/main/resources/application-postgres.properties`, `src/main/resources/application-oracle.properties`
  - **Description:** Add configuration keys for strict request validation, health redaction, and module toggles so behavior changes can be rolled out safely across H2/PostgreSQL/Oracle profiles.
  - **Acceptance criteria:** New keys exist for `mod.validation.strict-domain`, `mod.api.redact-health`, `mod.security.require-role-posture`, and can be overridden by profile/environment.
  - **Traceability:** design §III rollback strategy, §VI config/secrets, §X risk mitigation.

- [ ] **T-04 — Implement common API envelope and global error mapper** (priority: high · complexity: medium · depends on: T-02, T-03)
  - **Files:** `src/main/java/com/erm/legacy/api/ApiEnvelope.java`, `src/main/java/com/erm/legacy/api/ApiError.java`, `src/main/java/com/erm/legacy/api/GlobalExceptionHandler.java`, `src/main/java/com/erm/legacy/controller/ApiController.java`
  - **Description:** Standardize success/error response contracts and centralized exception handling to eliminate ad-hoc map/list responses across API endpoints.
  - **Acceptance criteria:** `/api/*` endpoints return a uniform `success/data/meta` or `success=false/error` structure with request id and correct HTTP codes for validation and forbidden errors.
  - **Traceability:** analysis recommendation (error envelope standardization); design §IV envelope contract, §V gap closures.

- [ ] **T-05 — Add request DTOs and validation rules for risk/security inputs** (priority: high · complexity: medium · depends on: T-03)
  - **Files:** `src/main/java/com/erm/legacy/api/request/RiskQueryRequest.java`, `src/main/java/com/erm/legacy/api/request/SecurityPostureRequest.java`, `src/main/java/com/erm/legacy/model/RiskDomain.java`, `src/main/java/com/erm/legacy/controller/ApiController.java`
  - **Description:** Introduce validated request models for `domain`, `severity`, and conditional `region` checks, including explicit enum allow-lists and normalization rules.
  - **Acceptance criteria:** Invalid `domain`/`severity` produce `422`; strict mode disables fallback-to-findAll; missing `region` for restricted posture requests produces `403`.
  - **Traceability:** analysis validation gaps; design §III field table, §V validation matrix.

- [ ] **T-06 — Prepare data-layer hardening and additive migrations** (priority: medium · complexity: medium · depends on: T-03)
  - **Files:** `pom.xml`, `src/main/resources/db/migration/postgres/V1_1__risk_register_indexes.sql`, `src/main/resources/db/migration/oracle/V1_1__risk_register_indexes.sql`, `src/main/java/com/erm/legacy/repository/RiskItemRepository.java`, `src/main/java/com/erm/legacy/model/RiskItem.java`
  - **Description:** Add migration support and repository/query capabilities for domain/severity filtering plus planned uniqueness/check constraints while keeping rollback reversible.
  - **Acceptance criteria:** Additive indexes (`domain`, `severity`, `domain+status`) are defined per dialect; repository exposes typed severity/domain filters; rollback script/note exists.
  - **Traceability:** design §III storage/index plan, §VI CI matrix for dual dialects.

## Phase 3: Incremental Implementation

- [ ] **T-07 — Split monolithic risk service into application and domain services** (priority: high · complexity: high · depends on: T-04, T-05, T-06)
  - **Files:** `src/main/java/com/erm/legacy/service/RiskService.java`, `src/main/java/com/erm/legacy/service/application/RiskApplicationService.java`, `src/main/java/com/erm/legacy/service/domain/RiskDomainService.java`, `src/main/java/com/erm/legacy/service/application/DashboardApplicationService.java`, `src/main/java/com/erm/legacy/legacy/LegacyChallengeRegistry.java`, `src/main/java/com/erm/legacy/repository/RiskItemRepository.java`
  - **Description:** Extract orchestration from `RiskService` into explicit application/domain services while preserving existing business semantics and repository usage.
  - **Acceptance criteria:** Controllers no longer call repository-coupled logic directly; domain service owns risk filtering/count business rules; legacy challenge lookups are encapsulated behind service contracts.
  - **Traceability:** design §II component model, §VIII Sprint 1.

- [ ] **T-08 — Refactor `ApiController` to application-service contracts and strict risk APIs** (priority: high · complexity: medium · depends on: T-07)
  - **Files:** `src/main/java/com/erm/legacy/controller/ApiController.java`, `src/main/java/com/erm/legacy/service/application/RiskApplicationService.java`, `src/main/java/com/erm/legacy/service/application/IntegrationApplicationService.java`, `src/main/java/com/erm/legacy/service/application/SecurityApplicationService.java`
  - **Description:** Rewire API routes to call application services only, apply validated request DTOs, and enforce strict behavior for invalid domain/severity filters.
  - **Acceptance criteria:** `/api/risks` rejects invalid filters with `422`; `/api/domain-counts`, `/api/domains`, `/api/scale`, `/api/legacy-challenges` preserve response semantics under new service boundary.
  - **Traceability:** analysis recommendation (strict payload validation); design §IV endpoint contracts, §V rules.

- [ ] **T-09 — Refactor `PageController` to dashboard/risk application services** (priority: medium · complexity: medium · depends on: T-07)
  - **Files:** `src/main/java/com/erm/legacy/controller/PageController.java`, `src/main/java/com/erm/legacy/service/application/DashboardApplicationService.java`, `src/main/java/com/erm/legacy/service/application/RiskApplicationService.java`
  - **Description:** Remove direct dependence on mixed legacy components from page routing and route all model composition through application services.
  - **Acceptance criteria:** `/dashboard`, `/risks`, `/legacy`, `/integrations`, `/security` pages still render with same business content while controller dependencies become application-service only.
  - **Traceability:** design §II presentation boundary, §VIII Sprint 1.

- [ ] **T-10 — Introduce security/integration service-call adapters and endpoint hardening** (priority: high · complexity: medium · depends on: T-04, T-07)
  - **Files:** `src/main/java/com/erm/legacy/service/application/SecurityApplicationService.java`, `src/main/java/com/erm/legacy/service/application/IntegrationApplicationService.java`, `src/main/java/com/erm/legacy/security/AccessControlService.java`, `src/main/java/com/erm/legacy/integration/IntegrationHub.java`, `src/main/java/com/erm/legacy/controller/ApiController.java`
  - **Description:** Add internal client-style contracts for security and integration modules, redact sensitive health/posture details, and gate posture access by role/attributes.
  - **Acceptance criteria:** `/api/health` redacts sensitive topology fields when configured; `/api/security/posture` enforces role policy and returns standardized forbidden errors; integration payloads flow through application service adapters.
  - **Traceability:** analysis recommendation (health redaction); design §II, §VII, §VIII Sprint 2-3.

- [ ] **T-11 — Update AngularJS client wiring for envelope responses and validation errors** (priority: medium · complexity: medium · depends on: T-04, T-08, T-10)
  - **Files:** `src/main/resources/static/js/erm-app.js`, `src/main/webapp/WEB-INF/jsp/dashboard.jsp`, `src/main/webapp/WEB-INF/jsp/risks.jsp`, `src/main/webapp/WEB-INF/jsp/security.jsp`
  - **Description:** Adapt frontend calls to the standardized API envelope and present validation/authorization errors without breaking existing JSP+AngularJS flows.
  - **Acceptance criteria:** Controllers read `res.data.data` for success payloads, show user-facing messages on `VALIDATION_FAILED`/`FORBIDDEN`, and keep existing panel interactions intact.
  - **Traceability:** design §IV response compatibility, §VIII blast radius.

## Phase 4: Testing & Quality Control

- [ ] **T-12 — Add API web-slice tests for validation, errors, and security gating** (priority: high · complexity: medium · depends on: T-08, T-10)
  - **Files:** `src/test/java/com/erm/legacy/controller/ApiControllerWebMvcTest.java`, `src/test/java/com/erm/legacy/controller/ApiErrorContractTest.java`
  - **Description:** Add `@WebMvcTest` coverage for endpoint contracts, strict query validation, redaction behavior, and forbidden posture access.
  - **Acceptance criteria:** Tests assert status codes (`200/422/403`) and envelope shape for `/api/health`, `/api/risks`, `/api/security/posture`; invalid domain no longer returns full list.
  - **Traceability:** design §IV contracts, §V validation matrix, §VI test strategy.

- [ ] **T-13 — Add domain/repository tests for rule enforcement and query behavior** (priority: high · complexity: medium · depends on: T-06, T-07)
  - **Files:** `src/test/java/com/erm/legacy/service/domain/RiskDomainServiceTest.java`, `src/test/java/com/erm/legacy/repository/RiskItemRepositoryTest.java`, `src/test/java/com/erm/legacy/security/AccessControlServiceTest.java`
  - **Description:** Cover field-level and rule-level behavior for mandatory/default/invalid/boundary cases, including status lifecycle checks and repository filtering.
  - **Acceptance criteria:** Tests verify field constraints (`riskId`, `title`, `status`, `owner`, `description`), severity/domain filtering, and ABAC conditional region logic.
  - **Traceability:** analysis `field_validations`; design §III field table, §V validation matrix.

- [ ] **T-14 — Add regression and quality-gate execution task in CI** (priority: medium · complexity: low · depends on: T-11, T-12, T-13)
  - **Files:** `pom.xml`, `README.md`, `.github/workflows/ci.yml`
  - **Description:** Wire lint/format/build/test verification and define regression scope for unchanged page routes and APIs affected by service-layer refactor.
  - **Acceptance criteria:** CI runs `mvn clean verify`; pipeline fails on architecture or API-contract regressions; README documents local verification commands.
  - **Traceability:** design §VI quality gates, §VIII impact analysis.

## Phase 5: Handoff Summary

- [ ] **T-15 — Publish release, migration/rollback, and ticket roll-up notes** (priority: medium · complexity: low · depends on: T-14)
  - **Files:** `docs/modernization/release-handoff.md`, `docs/modernization/migration-rollback.md`, `agent-runs/20260721T093117_dzexko/tasks.json`
  - **Description:** Document compatibility assumptions, phased rollout toggles, rollback steps, and manual-review checkpoints for high-risk business/security rules.
  - **Acceptance criteria:** Handoff includes rollback procedure for feature flags + DB migrations, unresolved-risk checklist, and statement that all tasks roll up under one parent modernization story unless PM tooling dictates otherwise.
  - **Traceability:** design §III rollback, §VII security controls, §X risks/open questions.
