import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
	`java-gradle-plugin`
	alias(libs.plugins.detekt)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.spotless)
	alias(libs.plugins.plugin.publish)
}

group = "ch.ergon.todochecker"

gradlePlugin {
	website = "https://github.com/ergon/todo-checker"
	vcsUrl = "https://github.com/ergon/todo-checker"
	plugins {
		create("todoChecker") {
			id = "ch.ergon.todochecker"
			implementationClass = "ch.ergon.todochecker.TodoCheckerPlugin"
			displayName = "ToDo Checker Plug-In for JIRA"
			description = "The Plug-In checks the source code for dangling TODO/FIXME-comments that refer to resolved JIRA tickets."
			tags = listOf("kotlin", "java", "jira", "todo", "fixme")
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
