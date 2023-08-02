import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
	`java-gradle-plugin`
	`maven-publish`
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.detekt)
	alias(libs.plugins.spotless)
}

val repositoryArtifactoryUsername: String by project
val repositoryArtifactoryApiKey: String by project

group = "ch.ergon.todochecker"

gradlePlugin {
	plugins {
		register("todoChecker") {
			id = "ch.ergon.todochecker"
			implementationClass = "ch.ergon.todochecker.TodoCheckerPlugin"
		}
	}
}

dependencies {
	compileOnly(libs.jira.rest.java.client)
}

publishing {
	repositories {
		maven {
			url = uri("https://artifacts.ergon.ch/artifactory/proj-ergon-cop-cd-releases/")
			credentials {
				username = repositoryArtifactoryUsername
				password = repositoryArtifactoryApiKey
			}
			authentication { create<BasicAuthentication>("basic") }
		}
	}
}

dependencyLocking {
	lockAllConfigurations()
}

kotlin {
	jvmToolchain(11)
}

tasks.withType<KotlinJvmCompile> {
	compilerOptions {
		allWarningsAsErrors.set(true)
	}
}

java {
	consistentResolution {
		useCompileClasspathVersions()
	}
}

detekt {
	buildUponDefaultConfig = true
}

spotless {
	kotlin {
		ktlint()
	}
}

tasks.register("resolveAndLockAll") {
	doFirst {
		require(gradle.startParameter.isWriteDependencyLocks)
	}

	doLast {
		configurations.filter { it.isCanBeResolved }.forEach { it.resolve() }
	}
}
