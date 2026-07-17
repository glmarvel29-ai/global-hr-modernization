# Discovery Executive Summary

**Project:** discovery12345 · **Generated:** 7/17/2026, 1:53:54 PM

> **Executive Summary**
>
> This report consolidates the overall ratings, key findings, and recommended actions from the 4 discovery analyses run across this codebase (frontend and backend). Each section below reproduces that analysis's executive view; full evidence and diagrams live in the individual reports.

## Portfolio Overview

| # | Analysis | Overall Rating | Hotspot Score |
|---|---|---|---|
| 1 | Architecture & Design Analysis | <span class="rating rating-high-risk">High Risk</span> | — |
| 2 | Code Quality & Complexity Analysis | <span class="rating rating-high-risk">High Risk</span> | 76 / 100 — High Risk |
| 3 | Frontend Modernization Analysis | <span class="rating rating-high-risk">High Risk</span> | — |
| 4 | Backend Modernization Analysis | <span class="rating rating-high-risk">High Risk</span> | — |

---

## 1. Architecture & Design Analysis

<div class="overall-rating overall-rating--high-risk"><div class="overall-rating-label">Overall Codebase Rating — Architecture &amp; Design</div><div class="overall-rating-value">High Risk</div><div class="overall-rating-note">Driven by High-Risk cross-domain coupling (H8), legacy React Router v5 patterns (F5), and duplicated domain logic in view components (H10).</div></div>

> **Executive Summary**
>
> This TARGET_WORKSPACE is a **frontend-only** social-network SPA (`social-media-react`) with **90 source files** (12 pages, 45 components, 11 services, 4 Redux modules). **No backend/server layer exists in the repository** — all persistence goes through an external REST API at `localhost:3030/api/` (production: `/api/`). Architectural health is **mixed**: HTTP access is partially centralized via `httpService` and domain service modules, but **presentation components bypass Redux** in 16 files, **business rules are duplicated** across post/comment/reply components, and **four feature domains** (user, post/comment, chat, activity) are tightly coupled through shared Redux state, socket orchestration in `Main.jsx`, and direct cross-service calls. The dominant risks are **legacy React Router v5 patterns (20 files)**, **cross-domain coupling (8 access points)**, and **inconsistent data-flow conventions** that will amplify change cost as features grow.

## 1.1 Benchmark Ratings Summary

| # | Hotspot | Primary KPI | <span class="rating rating-good">Good</span> | <span class="rating rating-moderate">Moderate</span> | <span class="rating rating-high-risk">High Risk</span> | Measured | Rating |
|---|---|---|---|---|---|---|---|
| H1 | Fat Controllers | Avg LOC per controller | <150 | 150–300 | >300 | N/A (no backend) | <span class="rating rating-good">Good</span> |
| H2 | Missing Service Layer | Controllers accessing repos/models | <10 | 10–20 | >20 | 0 backend handlers | <span class="rating rating-good">Good</span> |
| H3 | Missing Repository Pattern | Direct DB access points | <10 | 10–20 | >20 | 0 (external API) | <span class="rating rating-good">Good</span> |
| H4 | Circular Dependencies | Dependency cycles | 0 | 1–3 | >3 | 0 cycles (88 files, 137 edges) | <span class="rating rating-good">Good</span> |
| H5 | Shared Utility Abuse | Utility files w/ business logic | 0 | 1–5 | >5 | 0 (`utilService` is generic) | <span class="rating rating-good">Good</span> |
| H6 | Direct SQL in Controllers | ORM compliance % | >90% | 60–90% | <60% | N/A (no backend) | <span class="rating rating-good">Good</span> |
| H7 | God Classes | Classes >1000 LOC | 0 | 1–3 | >3 | 0 (max: `postActions.js` 210 LOC) | <span class="rating rating-good">Good</span> |
| H8 | Domain Boundary Violations | Cross-domain access points | 0 | 1–5 | >5 | 8 | <span class="rating rating-high-risk">High Risk</span> |
| H9 | Shared Database Coupling | Tables shared across domains | <10% | 10–30% | >30% | N/A (schema external) | <span class="rating rating-good">Good</span> |
| F1 | Business Logic in Components | Avg LOC per component | <150 | 150–300 | >300 | 78.6 avg (58 `.jsx` files) | <span class="rating rating-good">Good</span> |
| F2 | Missing Frontend Service/Data Layer | Components w/ inline API calls | <10 | 10–20 | >20 | 16 | <span class="rating rating-moderate">Moderate</span> |
| F3 | God / Oversized Components | Components >400 LOC | 0 | 1–3 | >3 | 0 (max: `Message.jsx` 250 LOC) | <span class="rating rating-good">Good</span> |
| F4 | Prop Drilling / Global State Abuse | Max prop-drilling depth | ≤2 | 3–4 | >4 | 2 levels; 71 `useSelector` hooks | <span class="rating rating-moderate">Moderate</span> |
| F5 | Legacy / Inconsistent Component Patterns | Legacy-pattern components | 0 | 1–10 | >10 | 20 | <span class="rating rating-high-risk">High Risk</span> |
| H10 | Duplicated Domain Logic in Views (additional) | Components duplicating reaction/connection rules | 0 | 1–3 | >3 | 5 | <span class="rating rating-moderate">Moderate</span> |

## 1.4 Actions Required

| Hotspot | Action | Rating | Priority |
|---|---|---|---|
| H8 | Split cross-domain imports: move comment actions out of `postActions.js`, add `activityFacade` for notification DTO assembly, decentralize socket subscriptions from `Main.jsx` | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-critical">Critical</span> |
| F5 | Migrate React Router v5 (`HashRouter`, `component=`, `useHistory`) to v6 across 20 files; standardize service naming | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| F2 | Eliminate 16 direct service imports from components — route all reads through Redux thunks and selectors | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-high">High</span> |
| F4 | Introduce `AuthContext` + memoized selectors; document single data-access convention | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H10 | Extract shared `toggleReaction` and `addConnection` domain helpers; remove duplicated logic from 5 view files | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |

## 1.5 Expected Outcomes

- **Bounded feature modules** — user, post, comment, chat, and activity can evolve independently with explicit facades instead of cross-imports.
- **Single data-flow path** — components consume selectors/thunks only, eliminating dual Redux + direct-service paths and stale cache bugs in `userService.usersCash`.
- **Testable domain rules** — extracted reaction/connection helpers become unit-testable without mounting React components.
- **Lower migration cost** — React Router v6 upgrade unlocks modern layouts, error boundaries, and code-splitting patterns.
- **Immutable, predictable state** — normalized Redux store removes embedded comment graphs and direct prop mutation anti-patterns.

---

## 2. Code Quality & Complexity Analysis

<div class="overall-rating overall-rating--high-risk"><div class="overall-rating-label">Overall Codebase Rating — Code Quality &amp; Complexity</div><div class="overall-rating-value">High Risk</div><div class="overall-rating-note">Driven by H1 High Cyclomatic Complexity (max CC 41) and H5 Duplicate Code (~12.7%).</div></div>

> **Executive Summary**
>
> This analysis covered **96 frontend source files** across two React applications (`social-media-react` at 87 files, `workbench-demo` at 9 files). No backend/server application layer exists in TARGET_WORKSPACE. Overall code quality is **High Risk**, driven by **high cyclomatic complexity** in notification and messaging components (max CC **41**) and **general duplicate code** estimated at **~12.7%** of normalized frontend LOC. File and class sizes remain within good bounds (largest file **212 LOC**; no files exceed 1000 LOC). Redux action modules repeat identical async thunk boilerplate across four files (**42 instances**). Git churn, defect-prone file, and ownership metrics could not be computed because target application sources are **untracked** in the parent repository history.

## 2.1 Benchmark Ratings Summary

Manual cyclomatic inspection (branch/loop/`&&`/`||`/`?:` counting) was used; no ESLint `complexity` rule or Sonar config is present in either project. Duplicate-code percentage derived from normalized 5-line block matching across `social-media-react/src` and `workbench-demo/src`.

| # | Hotspot | Primary KPI | <span class="rating rating-good">Good</span> | <span class="rating rating-moderate">Moderate</span> | <span class="rating rating-high-risk">High Risk</span> | Measured | Rating |
|---|---|---|---|---|---|---|---|
| H1 | High Cyclomatic Complexity | Max complexity per method | <10 | 10–20 | >20 | 41 (NotificaitonPreview) | <span class="rating rating-high-risk">High Risk</span> |
| H2 | Large Classes | Largest class LOC | <300 | 300–1000 | >1000 | 212 (LoginPage.test.tsx / Message.jsx) | <span class="rating rating-good">Good</span> |
| H3 | Large Functions | Largest function LOC | <50 | 50–200 | >200 | 190 (Signup.jsx `Signup`) | <span class="rating rating-moderate">Moderate</span> |
| H4 | Business Logic Duplication | Duplicated business logic % | <5% | 5–10% | >10% | ~8% (Redux thunk + auth-form patterns) | <span class="rating rating-moderate">Moderate</span> |
| H5 | Duplicate Code (general) | Overall duplicate code % | <5% | 5–10% | >10% | ~12.7% (5-line block analysis) | <span class="rating rating-high-risk">High Risk</span> |
| H6 | High Churn Areas | Monthly changes (top files) | <5 | 5–10 | >10 | n/a (source untracked in git) | <span class="rating rating-good">Good</span> |
| H7 | Defect-Prone Files | Fix commits (hottest file) | 1–3 | 4–5 | >5 | n/a (source untracked in git) | <span class="rating rating-good">Good</span> |
| H8 | Ownership Issues | Top-author ownership % | >80% | 60–80% | <60% | n/a (source untracked in git) | <span class="rating rating-good">Good</span> |
| H9 | Fragmented useEffect Chains (additional) | Max useEffect count per component (target ≤2) | ≤2 | 3 | ≥4 | 3 (NotificaitonPreview.jsx) | <span class="rating rating-moderate">Moderate</span> |

**No additional hotspots beyond H9 were observed.**

### Hotspot Score breakdown

| Component | Weight | Sub-score (0–100) | Weighted |
|---|---|---|---|
| Cyclomatic Complexity | 25% → 50% | 88 | 44.0 |
| Code Churn | 25% | n/a | n/a |
| Defect Density | 20% | n/a | n/a |
| Class/Function Size | 15% → 30% | 55 | 16.5 |
| Business Logic Duplication | 10% → 20% | 75 | 15.0 |
| Developer Ownership Risk | 5% | n/a | n/a |
| **Hotspot Score** | **100%** | | **76 / 100** |

## 2.5 Actions Required

| Hotspot | Action | Rating | Priority |
|---|---|---|---|
| H1 High Cyclomatic Complexity | Extract `ActivityMessageStrategy` for notification types; split `useChat` hook in `Message.jsx`; enable ESLint `complexity` rule (threshold 15) | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-critical">Critical</span> |
| H3 Large Functions | Extract `AuthFormFields` from `Home.jsx`/`Signup.jsx`; decompose `CommentPreview` handlers into service helpers | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H4 Business Logic Duplication | Introduce shared Redux async action factory; consolidate activity copy into `buildActivityMessage()` | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H5 Duplicate Code (general) | Create shared `FormInputGroup` component; add `test-utils.tsx` helpers in workbench-demo | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| H9 Fragmented useEffect Chains | Replace 3-effect chains in `NotificaitonPreview.jsx` with `useNotificationPreview` hook or React Query | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |

## 2.6 Expected Outcomes

- Lower defect rate in notifications and messaging flows by isolating activity-type logic behind Strategy handlers (CC target <15 per function).
- Faster code reviews and safer refactors as shared form components and Redux thunk factories eliminate ~8–13% duplicated LOC.
- Improved testability via smaller extracted hooks (`useChat*`, `useNotificationPreview`) that can be unit-tested independently of page components.
- Reduced stale-closure and double-fetch bugs from consolidated effect management in notification and search components.
- Future discovery runs can track churn and ownership once frontend projects are committed to git, enabling proactive maintenance of high-change files.

---

Full report saved to `target/docs/discovery/02-code-quality-complexity.md` (321 lines). Pipeline summary: `agent-runs/20260717T132536_tkr1f1/02-code-quality-complexity-summary.md`.

---

## 3. Frontend Modernization Analysis

<div class="overall-rating overall-rating--high-risk"><div class="overall-rating-label">Overall Codebase Rating — Frontend Modernization</div><div class="overall-rating-value">High Risk</div><div class="overall-rating-note">Driven by H1 UI component duplication (~15% of scanned files replicate loading/preview patterns without a shared library) and H6 legacy Redux store architecture (0% RTK adoption).</div></div>

> **Executive Summary**
>
> The `target/` workspace contains two React single-page applications. The primary surface is `social-media-react` (React 18.2.0, legacy Redux `createStore` + thunks, React Router v5), comprising 58 JSX view units plus 3 TypeScript components in `workbench-demo` (React 18.3.1, hooks-only, Tailwind). All 61 scanned components are functional — no class-based components were found. The most severe modernization gaps are duplicated UI patterns (inline loading spinners in 9 files, repeated like-toggle logic in 4 preview components) and legacy global Redux wiring read by 49% of components. `Message.jsx` (250 LOC) mixes chat orchestration, socket side-effects, and view composition in one file, and the messaging feature drills 10+ props through four component layers without a domain composable or context.

## 3.1 Benchmark Ratings Summary

| # | Hotspot | Primary KPI | <span class="rating rating-good">Good</span> | <span class="rating rating-moderate">Moderate</span> | <span class="rating rating-high-risk">High Risk</span> | Measured | Rating |
|---|---|---|---|---|---|---|---|
| H1 | UI Component Duplication | Duplicate components % | <5% | 5–10% | >10% | ~15% (9/61 files) | <span class="rating rating-high-risk">High Risk</span> |
| H2 | Legacy Class-Based Components | Modern component adoption % | >90% | 70–90% | <70% | 100% (61/61) | <span class="rating rating-good">Good</span> |
| H3 | Massive Components | Largest component LOC | <200 | 200–500 | >500 | 250 (`Message.jsx`) | <span class="rating rating-moderate">Moderate</span> |
| H4 | Global State Dependencies | Components reading global state % | <30% | 30–60% | >60% | 49% (30/61) | <span class="rating rating-moderate">Moderate</span> |
| H5 | Complex State Management | Max prop-drilling depth | <3 | 3–5 | >5 | 4 levels (Message → Messaging → ListMsg → MsgPreview) | <span class="rating rating-moderate">Moderate</span> |
| H6 | Legacy Redux Store (additional) | RTK/modern store adoption % | >90% | 70–90% | <70% | 0% (0/4 modules use RTK) | <span class="rating rating-high-risk">High Risk</span> |
| H7 | Direct DOM in React Views (additional) | Components using imperative DOM APIs | 0 files | 1–2 files | >2 files | 2 files | <span class="rating rating-moderate">Moderate</span> |

## 3.5 Actions Required

| Hotspot | Action | Rating | Priority |
|---|---|---|---|
| H1 UI Component Duplication | Create `src/cmps/shared/` with `LoadingSpinner`, `UserAvatarCard`, and `useLikeReaction`; replace 9 loading blocks and 4 duplicate like handlers | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| H3 Massive Components | Extract `useChat` hook from `Message.jsx`; split `CommentPreview.jsx` and `CreatePostModal.jsx` into subcomponents under 200 LOC each | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H4 Global State Dependencies | Migrate `social-media-react/src/store/` to Redux Toolkit slices; add memoized selectors to reduce ad-hoc `useSelector` inline lambdas | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-high">High</span> |
| H5 Complex State Management | Introduce `ChatContext` / `useMessagingState` composable to eliminate 4-level prop drilling in messaging and comment save chains | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H6 Legacy Redux Store | Add `@reduxjs/toolkit`, convert four reducer modules to slices, adopt RTK Query for `userService`/`postService` | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-critical">Critical</span> |
| H7 Direct DOM in React | Refactor `InputFilter.jsx` to controlled autocomplete list; replace `MessageThread.jsx` scroll with `useRef` | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |

## 3.6 Expected Outcomes

- A shared component library (`LoadingSpinner`, `UserAvatarCard`, `ReactionButton`) reduces duplicated markup from ~15% of files to near zero and enforces consistent loading and interaction UX.
- Extracting `useChat`, `useLikeReaction`, and RTK slices improves unit-test coverage for chat and reaction flows without mounting full page trees.
- Redux Toolkit migration eliminates ~872 LOC of manual boilerplate and provides typed selectors, reducing the 49% global-store coupling through scoped domain hooks.
- `ChatContext` collapses 10-prop drilling chains in messaging to single-hook consumption, simplifying addition of typing indicators and read receipts.
- Replacing imperative DOM calls in `InputFilter` and `MessageThread` restores React-idiomatic rendering and prevents event-listener leaks during hot reload.

---

## 4. Backend Modernization Analysis

<div class="overall-rating overall-rating--high-risk"><div class="overall-rating-label">Overall Codebase Rating — Backend Modernization</div><div class="overall-rating-value">High Risk</div><div class="overall-rating-note">Driven by H6 (0% documented/governed REST surface) and H7 (0% API governance compliance: no OpenAPI, versioning, or contract tests).</div></div>

> **Executive Summary**
>
> The **ERM Complexity Demo** backend is a compact Spring Boot 2.7.18 monolith (17 Java source files, &lt;5K LOC) with a deliberate legacy-debt surface: static shared-business facades, duplicated controller logic, and dual Oracle/PostgreSQL dialect routing. Layering is partially sound—JPA access is confined to `RiskService` and `DemoDataLoader`, and controllers inject services rather than repositories—but modernization gaps remain in API governance and static coupling. Eight read-only REST endpoints under `/api` serve AngularJS panels with **no OpenAPI spec, no versioning, and no contract tests** (0% governance compliance). Dynamic-variable-from-input patterns were not observed; however, `SharedBusinessServices` static methods, a Spring singleton holding mutable RBAC state, N+1 repository calls in domain aggregation, and plaintext database passwords in profile properties require remediation before production hardening.

## 4.1 Benchmark Ratings Summary

| # | Hotspot | Primary KPI | <span class="rating rating-good">Good</span> | <span class="rating rating-moderate">Moderate</span> | <span class="rating rating-high-risk">High Risk</span> | Measured | Rating |
|---|---|---|---|---|---|---|---|
| H1 | Dynamic Variable Creation | Dynamic-var-from-input occurrences | 0 | 1–10 | >10 | 0 | <span class="rating rating-good">Good</span> |
| H2 | Global Mutable State | Globals / mutable static state | 0 | 1–5 | >5 | 1 | <span class="rating rating-moderate">Moderate</span> |
| H3 | Direct SQL Outside Data Layer | Data-layer compliance % | >90% | 60–90% | <60% | 100% | <span class="rating rating-good">Good</span> |
| H4 | Static / Singleton Abuse | Business-logic static/singleton classes | 0 | 1–5 | >5 | 1 | <span class="rating rating-moderate">Moderate</span> |
| H5 | Missing Service Layer | Handlers with inline business logic | <10 | 10–20 | >20 | 2 | <span class="rating rating-good">Good</span> |
| H6 | API Sprawl | Documented & governed endpoints % | >90% | 80–90% | <80% | 0% | <span class="rating rating-high-risk">High Risk</span> |
| H7 | Missing API Governance | Governance compliance % | 100% | 90–99% | <90% | 0% | <span class="rating rating-high-risk">High Risk</span> |
| H8 | N+1 Repository Calls (additional) | Service methods with per-item repository loops | 0 | 1–2 | >2 | 1 | <span class="rating rating-moderate">Moderate</span> |
| H9 | Hardcoded Credentials (additional) | Plaintext secrets in committed config files | 0 | 1–5 | >5 | 2 | <span class="rating rating-moderate">Moderate</span> |

## 4.5 Actions Required

| Hotspot | Action | Rating | Priority |
|---|---|---|---|
| H6 API Sprawl | Consolidate risk reads under versioned `/api/v1/risks`; deprecate duplicate MVC JSON paths; publish OpenAPI listing all 8 endpoints | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| H7 Missing API Governance | Add springdoc-openapi, `/api/v1` prefix on `ApiController`, and MockMvc contract tests for `/health` and `/risks` response shapes | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-critical">Critical</span> |
| H2 Global Mutable State | Externalize `AccessControlService` RBAC matrix to immutable `@ConfigurationProperties` or policy service; inject via `PermissionPolicyPort` | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H4 Static / Singleton Abuse | Replace `SharedBusinessServices` static methods with injectable `TenantDialectService` and `RiskIdComposer` beans | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H8 N+1 Repository Calls | Refactor `RiskService.countsByDomain()` to a single `@Query` GROUP BY or `countByDomain` repository method | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| H9 Hardcoded Credentials | Move Postgres/Oracle passwords from `application-*.properties` to environment variables or Spring Cloud Config | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-high">High</span> |

## 4.6 Expected Outcomes

- OpenAPI-backed `/api/v1` endpoints give AngularJS and future consumers a stable, versioned contract and prevent silent breaking changes to `RiskItem` JSON shapes.
- Extracting `SharedBusinessServices` into injectable services enables per-region dialect routing via Spring profiles and unit-test isolation without static coupling.
- Externalizing RBAC policy and database credentials removes mutable singleton state and plaintext secrets from committed configuration.
- A single `RiskService.findByDomainCode()` eliminates duplicated controller logic and reduces API sprawl between MVC and REST entry points.
- Replacing N+1 domain count queries with aggregated repository methods improves dashboard load time as the risk register scales beyond demo seed data.

---

Full report saved to `target/docs/discovery/04-backend-modernization.md` (406 lines). Verified against **Java 8 / Spring Boot 2.7.18** backend in `glmarvel29-ai/global-hr-modernization`: 17 Java files, 2 controllers, 8 REST endpoints, 0 test files, 0% API governance.