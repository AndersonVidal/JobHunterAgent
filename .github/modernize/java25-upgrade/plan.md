# Modernization Plan: Java 25 LTS Upgrade

**Project**: JobHunterAgent

## Technical Framework

- **Language**: Java; Maven currently targets release 17.
- **Framework**: Spring Boot 3.5.0, declared in the existing working-tree change to `pom.xml`.
- **Build Tool**: Maven, using the root `pom.xml`; no Maven wrapper is present.
- **Database**: None identified in the repository.
- **Key Dependencies**: Spring Boot Web and Spring Boot Test; JUnit Jupiter 5.11.0.

## Overview

Move the Java build target and build/runtime environment to the explicitly requested Java 25 LTS while preserving the existing application and all pre-existing working-tree changes. Keep this request limited to the Java version; do not silently upgrade Spring Boot or other dependencies. Confirm application runtime compatibility separately before representing the application as supported on Java 25.

## Migration Impact Summary

| Surface | Current state | Target state | Scope |
|---|---|---|---|
| Maven Java properties | `maven.compiler.release=17` | `java.version=25` and `maven.compiler.release=25` | `pom.xml` |
| Build/runtime JDK | Not pinned in repository | Maven runs with JDK 25 | Environment used for validation |
| Maven config files | `.mvn/jvm.config` and `.mvn/maven.config` are empty | Leave unchanged unless evidence requires otherwise | No repository change planned |
| Compiler/test plugins | Compiler 3.13.0; Surefire 3.3.0 managed in the POM | Retain unless Java 25 validation proves an incompatibility | No speculative plugin upgrades |

## Compatibility Gate

The Java-upgrade guideline available to this plan lists Spring Boot 3.x as compatible only through Java 21 and Spring Boot 4.x as the Java 25-compatible line. This repository declares Spring Boot 3.5.0 in the already-modified `pom.xml`. A Java 25 compiler target alone does not establish that the application can run on Java 25. The requested task must not upgrade Spring Boot or Jakarta APIs without a separate decision; resolve the question below before claiming supported Java 25 runtime behavior.

## Safe Execution and Validation Order

1. **Protect the existing workspace.** Recheck `git status --short --untracked-files=all` immediately before execution. At plan creation, `pom.xml` was modified; the prior `src/main/java/com/example/App.java` and `src/test/java/com/example/AppTest.java` were deleted; and replacement files under `src/main/java/com/andersonvidal/jobhunteragent/` and `src/test/java/com/andersonvidal/jobhunteragent/` were untracked. Preserve these changes exactly: do not reset, restore, stage, or rewrite unrelated files. Make only the narrowly scoped Java-property edits in `pom.xml`.
2. **Preserve generated output.** `target/job-hunter-agent-1.0-SNAPSHOT.jar` already exists. Before a Maven build or clean operation, preserve it or validate in an isolated copy of the current working tree. Do not run `mvn clean` in the original workspace if it would remove user data in `target/`.
3. **Record the baseline before editing.** Capture `mvn -version` and run the existing test/build baseline before changing Java properties. The current untracked `HomeControllerTest.java` appears incomplete (including unresolved test symbols and a nested class with the same name as its enclosing class); record its baseline result and do not repair or replace that user file as part of this Java-only task.
4. **Select the JDK.** Configure the execution environment so `mvn -version` reports Java 25. The repository has no Maven wrapper or checked-in Java-version/toolchain pin; do not add one unless the user separately asks for repository-wide toolchain pinning.
5. **Update only the Java version properties.** In the current `pom.xml`, set both `<java.version>25</java.version>` and `<maven.compiler.release>25</maven.compiler.release>`. This aligns the Spring Boot parent property and compiler release. Do not add independent `source`/`target` values, change the Spring Boot parent, update unrelated dependencies, or otherwise reformat the user's modified POM.
6. **Run narrow build validation.** With the existing output protected, run `mvn -Dmaven.test.skip=true package` to verify production compilation and packaging with JDK 25. Confirm the effective compiler release is 25 and inspect the resulting artifact. Do not treat skipped tests as a passing test result.
7. **Run the test gate and compare with baseline.** Run `mvn test` and compare the result with the pre-change baseline. If the existing test-source errors remain, report unit-test validation as blocked by those pre-existing errors; do not make out-of-scope test edits. If tests pass at baseline and after the change, record that result.
8. **Resolve runtime compatibility.** Do not claim a Java 25 application-startup/runtime pass while Spring Boot 3.5.0 remains in place under the compatibility guidance above. If a Boot 4.x/framework migration is authorized separately, plan and validate it as a separate task.
9. **Review the final diff.** Confirm only the intended Java-version properties changed as part of the Java task and that all initially modified, deleted, and untracked user files remain intact. Do not execute this plan as part of plan generation.

## Open Questions & Questionnaire

- [ ] Do you want a separate Spring Boot 4.x/Spring Framework 7 compatibility upgrade so the application can claim supported Java 25 runtime behavior? The current request is Java-only, so that framework change is not included.

