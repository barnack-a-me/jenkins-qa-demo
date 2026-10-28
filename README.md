# jenkins-qa-demo

Proyecto de practica CI/CD con Jenkins: Java 17 + Maven + JUnit 5 + Mockito + JaCoCo + PostgreSQL (Docker).

- Pruebas:        mvn test
- Cobertura:      mvn test jacoco:check   (reporte en target/site/jacoco/index.html)
- Infraestructura: docker compose up -d --build   (Jenkins en :8080, PostgreSQL en :5432)
