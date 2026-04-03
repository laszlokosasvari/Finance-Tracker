🛡️ Finance User Auth Service

A production-ready Identity Provider (IdP) built with Spring Boot 3 and Java 17. This service manages user lifecycles, handles secure authentication via RSA-signed JWTs, and implements advanced security patterns like Silent Authentication and Brute Force Protection.
🚀 Core Features
1. Asymmetric JWT Authentication (RSA)

Unlike standard symmetric HS256 setups, this service uses RS256 (RSA).

    Private Key: Stored securely in the Auth Service to sign tokens.

    Public Key: Shared via a JWKS endpoint, allowing the Finance Service to verify tokens without needing a shared secret.

2. Intelligent "Silent" Authentication

To balance user experience with security, we implemented a dual-layer check:

    Stateless Check: Validates the accessToken in-memory for speed.

    Stateful Recovery: if the accessToken is expired, the service automatically checks the refreshToken against the database to allow seamless session extension.

3. Brute Force Protection

The service automatically defends against password-guessing attacks:

    Failure Tracking: Monitors failed_attempts per user.

    Account Locking: Automatically sets account_locked to true after 5 failed attempts.

    Temporary Cooldown: Implements a mandatory 15-minute lock period recorded via lock_time before allowing a retry.

4. Secure Data Handling

   Password Hashing: Uses BCryptPasswordEncoder for high-entropy storage.

   Validation: Enforces strict password complexity (Uppercase, Lowercase, Number) at the DTO level.

   CORS Policy: Restricted to authorized frontend origins (e.g., localhost:3000).

🛠️ Tech Stack

    Java 17

    Spring Boot 3.4.x

    Spring Security 6 (Stateless/JWT policy)

    JJWT (0.12.x): For modern, non-deprecated RSA signing

    PostgreSQL: With Flyway/SQL migrations for schema versioning

## 📡 API Endpoints

| Method | Endpoint | Body / Payload | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/registration` | `SignUpRequest` | Registers a new user. Enforces email format and password complexity (8+ chars, Upper, Lower, Number). |
| `POST` | `/api/v1/auth/login` | `SignInRequest` | Authenticates user. Returns `accessToken`, `refreshToken`, and `userId`. Tracks `failed_attempts` for brute-force protection. |
| `POST` | `/api/v1/auth/silent` | `UserContext` | **Gatekeeper:** Validates `accessToken` (200 OK). If expired, checks DB for `refreshToken` to allow session recovery (202 Accepted). |
| `PUT` | `/api/v1/auth/refresh` | `UserContext` | **Token Rotation:** Revokes old tokens and issues a new pair. Requires a valid, non-expired `refreshToken` in the database. |
| `DELETE` | `/api/v1/auth/logout` | `UserContext` | **Session Termination:** Deletes the user's `refreshToken` from the database to prevent further access. |
| `GET` | `/api/v1/auth/me` | `Bearer Token` | **Protected:** Extracts user identity from the JWT and returns the current user's UUID. |
| `GET` | `/api/v1/auth/.well-known/jwks.json` | None | **Public:** Exposes the RSA Public Key in JWKS format. Used by other microservices to verify tokens offline. |

🔐 Security Configuration

The service operates on a Stateless session policy. It utilizes a custom JwtAuthenticationFilter placed before the standard UsernamePasswordAuthenticationFilter to intercept and validate Bearer tokens.