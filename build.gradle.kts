import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
	`java-gradle-plugin`
	alias(libs.plugins.detekt)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.spotless)
}

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
