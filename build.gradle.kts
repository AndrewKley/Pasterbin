plugins {
	java
	id("org.springframework.boot") version "3.1.8-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.4"
	id("org.liquibase.gradle") version "2.2.0"
}

group = "com.note.core"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_18
	targetCompatibility = JavaVersion.VERSION_18
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

val postgresqlVersion = "42.7.1"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql:${postgresqlVersion}")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	liquibaseRuntime("org.liquibase:liquibase-core:4.25.1")
	liquibaseRuntime("info.picocli:picocli:4.7.5")
	liquibaseRuntime("org.postgresql:postgresql:${postgresqlVersion}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

apply {
	plugin("org.liquibase.gradle")
}

liquibase {
	activities.register("main") {
		var dbUrl by project.extra.properties
		var dbSchemaName by project.extra.properties
		var dbUser by project.extra.properties
		val dbPass by project.extra.properties
		this.arguments = mapOf(
			"changeLogFile" to "src/main/resources/db/changelog/db.changelog-master.yaml",
			"url" to dbUrl,
			"username" to dbUser,
			"password" to dbPass,
			"defaultSchemaName" to dbSchemaName
		)
	}
	runList = "main"
}
