# Online Quiz Management System

A beginner-friendly B.Tech CSE web application for creating quizzes, taking timed multiple-choice tests, and reviewing results. It uses server-rendered Thymeleaf pages and a layered Spring JDBC architecture backed by MySQL.

## Features

- Session-based login/logout and BCrypt password hashing.
- Three roles: administrator, quiz creator, and participant.
- Creators can create quizzes, add/edit/delete questions, publish/unpublish quizzes, and view participant scores.
- Participants can browse published quizzes, take timed attempts, submit answers, view results, and review attempt history.
- Administrators can view user accounts, quizzes, and attempt reports.
- Parameterized `JdbcTemplate` SQL operations for CRUD and reports.
- Quiz submission writes the attempt answers, final score/status, and result row inside one `@Transactional` service method. A database failure rolls the transaction back.
- A background `ScheduledExecutorService` tracks open attempts in a `ConcurrentHashMap`. When a timer expires it safely changes an in-progress attempt to `EXPIRED`. The browser timer submits the form when time runs out; server timer expiration still applies when a browser is closed. The scheduler is shut down when the application stops.

## Technology stack

- Java 21, Spring Boot 3.3.5, Maven
- Spring MVC, Thymeleaf, HTML, CSS, minimal JavaScript
- Spring JDBC (`JdbcTemplate`) and MySQL Connector/J
- MySQL 8+
- JUnit 5 / Spring Boot Test

The app intentionally does not use React, Spring Data JPA, Hibernate, MongoDB, Firebase, or Supabase.

## Architecture

`Thymeleaf page → Controller → Service → DAO → JdbcTemplate → MySQL`

- `controller`: HTTP routes, form handling, and role-aware pages.
- `service`: authentication, quiz workflow, and scoring rules.
- `dao`: parameterized SQL and row mapping.
- `model`: encapsulated quiz/user domain objects.
- `thread`: safe expiration scheduling for timed attempts.
- `config`: password encoder bean.
- `util`: session role checks.
- `exception`: meaningful domain exceptions.

### OOP and Java concepts used

- `User` is abstract. `Admin`, `QuizCreator`, and `Participant` inherit identity fields and override `getRole()`. DAO row mapping creates the appropriate subtype; controllers use polymorphism to select dashboards.
- `QuizService` and `ScoringService` define contracts with concrete service implementations.
- Models keep fields private and expose constructors, getters, and setters.
- `List<Question>`, `List<Quiz>`, and `Map<Long, String>` represent quiz contents and question-id-to-choice submissions. DAOs return typed lists; submitted choices are validated before scoring.
- Parameterized queries pass user input separately from SQL text (`?` placeholders). This prevents SQL injection by keeping data separate from executable SQL.
- `@Transactional` groups answer inserts, score update, and result insert into one all-or-nothing database transaction.
- `QuizAttemptTimerService` uses a single scheduled worker and `ConcurrentHashMap` so active timeout tasks can be registered/cancelled safely. A synchronized expiration operation avoids races for a given timer callback; the SQL status condition also ensures only in-progress attempts expire.

## Database setup

Install and start MySQL Server, then open MySQL Workbench and connect to your local server.

1. Choose **File → Open SQL Script…** and open `database/schema.sql` from this project.
2. Click the lightning-bolt **Execute** button. This creates the `quiz_platform` database and its six tables: `users`, `quizzes`, `questions`, `quiz_attempts`, `answers`, and `results`.
3. Open `database/seed.sql` the same way and execute it. It creates sample accounts, one Java/OOP quiz, and four questions. It can be run again without duplicating those rows.
4. In Workbench, refresh the **SCHEMAS** panel and confirm `quiz_platform` is present.

Set `QUIZ_DB_URL` to the JDBC URL for that schema. Use a MySQL account with permission to read/write the tables. All three database settings are required; the application does not contain fallback credentials. A suitable URL is `jdbc:mysql://localhost:3306/quiz_platform?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`.

### Windows environment variables

In PowerShell, for the current terminal session (replace the sample password with your MySQL user's password):

```powershell
$env:QUIZ_DB_URL = "jdbc:mysql://localhost:3306/quiz_platform?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:QUIZ_DB_USER = "root"
$env:QUIZ_DB_PASSWORD = "your_mysql_password"
```

To save them for future terminals, use Windows Command Prompt as your user account:

```bat
setx QUIZ_DB_URL "jdbc:mysql://localhost:3306/quiz_platform?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
setx QUIZ_DB_USER "root"
setx QUIZ_DB_PASSWORD "your_mysql_password"
```

Open a new terminal after `setx`. Avoid committing real credentials. The `.gitignore` excludes `.env` and common IDE/build files.

## Run the application

Install Maven 3.9+ and confirm `mvn -version` works. From the project folder, with the three environment variables set in that terminal, run:

```powershell
mvn spring-boot:run
```

Open **http://localhost:8080** in your browser.

To run tests and package a jar:

```powershell
mvn test
mvn clean package
```

## Sample accounts

All three seeded accounts use the demo password **`password`**. The database stores a BCrypt hash, not the plain password. Change demo credentials before any real deployment.

| Role | Email |
|---|---|
| Admin | `admin@quizplatform.local` |
| Quiz creator | `creator@quizplatform.local` |
| Participant | `participant@quizplatform.local` |

The password is deliberately simple for a local classroom demo. The register page requires new user passwords to be at least eight characters.

## Project structure

```text
online-quiz-platform/
├── database/
│   ├── schema.sql
│   └── seed.sql
├── src/
│   ├── main/java/com/quizplatform/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── dao/
│   │   ├── service/
│   │   ├── util/
│   │   ├── exception/
│   │   ├── thread/
│   │   └── config/
│   ├── main/resources/
│   │   ├── static/css/
│   │   ├── static/js/
│   │   ├── templates/
│   │   └── application.properties
│   └── test/java/com/quizplatform/
├── pom.xml
└── README.md
```

## Testing checklist

- Run `mvn test`; scoring and role-polymorphism unit tests should pass.
- Start MySQL, execute both SQL scripts, set environment variables, and launch with `mvn spring-boot:run`.
- Log in as each sample role and verify its dashboard and allowed actions.
- As a creator, create a quiz, add/edit/delete questions, publish it, and review attempt reports.
- As a participant, take the sample quiz, submit correct/incorrect answers, and review score/history.
- Leave a quiz open until its limit expires; verify it is marked `EXPIRED` in `quiz_attempts`.
- Try opening creator/admin routes as a participant and verify access is refused.
- Stop MySQL during a submission in a local test database and check that submission writes roll back together.

## Common problems and fixes

- **`mvn` is not recognized:** install Apache Maven, add its `bin` directory to `PATH`, open a new terminal, and check `mvn -version`.
- **Communications link failure:** start MySQL Server and confirm host/port in `QUIZ_DB_URL`.
- **Access denied for user:** verify `QUIZ_DB_USER` / `QUIZ_DB_PASSWORD` and MySQL grants.
- **Unknown database/table:** execute `schema.sql` first and `seed.sql` second in Workbench.
- **Login fails for seed accounts:** use the documented lowercase demo password `password`; rerun `seed.sql` if the seed hash was changed.
- **Port 8080 already in use:** stop the other process or change `server.port` in `application.properties`.
- **Java version error:** install/select JDK 21 and check `java -version`.

## Viva explanation points

1. Explain MVC layers and how a browser request flows through controller, service, DAO, JDBC, and MySQL.
2. Show prepared/parameterized SQL in a DAO and explain its role in SQL injection prevention.
3. Explain `User` inheritance and `getRole()` overriding, and the service interfaces.
4. Explain how answer choices are represented as `Map<Long, String>` and quiz records as typed `List` collections.
5. Explain the timer worker, `ConcurrentHashMap`, cancellation on submission, and safe expiration update.
6. Explain how `@Transactional` keeps answer rows, attempt totals/status, and result rows consistent.
7. Explain BCrypt password hashing and session-based role checks.
