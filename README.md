# Football API Test Suite

TestNG‑ and Rest‑Assured–based automated tests for the Football “player” API, with Allure reporting and quality checks.

---

## Application Under Test

We are testing the following application and its Player controller:
[Swagger UI – Player Controller](http://3.68.165.45/swagger-ui.html#/player-controller)

---

## Covered Endpoints

All endpoints live under the `/player/` root path:

| Operation       | HTTP Method | Path                           | Test helper                          |
|-----------------|-------------|--------------------------------|--------------------------------------|
| Create player   | **GET**     | `/player/create/{editor}`      | `PlayerAPI#createPlayer(...)`        |
| Update player   | **PATCH**   | `/player/update/{editor}/{id}` | `PlayerAPI#updatePlayer(...)`        |
| Read single     | **POST**    | `/player/get`                  | `PlayerAPI#readPlayer(...)`          |
| Read all        | **GET**     | `/player/get/all`              | `PlayerAPI#readPlayers()`            |
| Delete player   | **DELETE**  | `/player/delete/{editor}`      | `PlayerAPI#deletePlayer(...)`        |

---

## How to Run the Tests

1. **Clone repository**
   ```bash
   git clone https://github.com/mkgerasimenko/football.git
   cd football
   ```

2. **Prerequisites**
    - Java 17

3. **Execute tests**
   ```bash
   ./gradlew clean test
   ```
   By default TestNG uses the suite file `src/test/resources/{API_SUITE_NAME}.xml`, falling back to `smoke-suite-api.xml`.

---

## Generating Allure Reports

After running tests, generate and view your Allure report:

```bash
# generate static HTML report
./gradlew allureReport --depends-on-tests

# serve report locally at http://localhost:xxxx
./gradlew allureServe
```

- Raw results (JSON) are in `build/allure-results/`
- Final HTML is in `build/reports/allure-report/allureReport/index.html`

---

## Configuration

All runtime settings live in `APIConfig.java`:

```java
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface APIConfig extends Config {
    APIConfig CONFIG = ConfigCache.getOrCreate(APIConfig.class, System.getenv(), System.getProperties());

    @DefaultValue("http://3.68.165.45")
    String apiUrl();

    @DefaultValue("6")
    int randomCharactersAmount();

    @DefaultValue("17")
    int minAge();

    @DefaultValue("59")
    int maxAge();
}
```

Override via environment variables or JVM properties:

```bash
export apiUrl="https://my.api.host"
export randomCharactersAmount=8
export minAge=18
export maxAge=60

# or
./gradlew test -DapiUrl=https://my.api.host -DrandomCharactersAmount=8
```

Optional runner variables:

- `API_SUITE_NAME` – TestNG XML suite name (in `src/test/resources/`)
- `SHOW_STANDARD_STREAMS` – `true` to include stdout/stderr in test logs

---

## Known Issues

1. **Inconsistent HTTP verbs**
    - **Create** uses `GET` (should be `POST` for REST best practices)
    - **Read single** uses `POST` (should be `GET`)
    - **Read all** correctly uses `GET`

2. **No error message in response**  
   On validation or “not found” errors, the API returns only an HTTP status code (e.g. `400`, `403`) with an empty body—no `{ "message": ... }` field—making debugging harder.

---

## Possible Bugs & Gaps

- **Side‑effect in GET**: Creating via `GET` may be cached by proxies or violate idempotency.
- **Error‑case coverage**: No negative tests for invalid roles, malformed JSON, or unexpected HTTP methods (`PUT`, `OPTIONS`).
- **Boundary testing**: No tests for minimum/maximum age values or zero/negative IDs.

---

## Critical Issues

Based on the Swagger definition, the following critical API issues have been identified:

- **No authentication/authorization**: All Player endpoints are publicly accessible without any API key or token.
- **Lack of pagination**: The `/player/get/all` endpoint returns all players in one response, risk of large payloads and performance degradation.
- **Non‑RESTful use of HTTP verbs**: Resource creation via `GET` breaks idempotency and caching semantics.
- **Unclear update semantics**: `/player/update/{editor}/{id}` uses `PATCH` but no schema for partial vs full update is defined.

---

## How to Contribute

1. **Fork** the repo and create a feature branch.
2. Add or update tests under `src/test/java/com/football/apitests`.
3. Update data providers in `PlayerDataProviders` if new scenarios need parameters.
4. Run `./gradlew clean test allureReport` and verify the Allure report.
5. Open a Pull Request with a clear description of changes.

---
