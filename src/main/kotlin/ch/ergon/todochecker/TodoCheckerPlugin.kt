package ch.ergon.todochecker

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

private const val EXTENSION_NAME = "todoChecker"
private const val TASK_NAME = "checkTodo"
private const val OUTPUT_FILE_NAME = "todochecker.txt"

class TodoCheckerPlugin : Plugin<Project> {
	override fun apply(project: Project) = with(project) {
		createExtension()
		createTask()
	}
}

private fun Project.createExtension() {
	val extension = extensions.create(EXTENSION_NAME, TodoCheckerExtension::class.java)
	extension.directory.convention(layout.projectDirectory)
}

private fun Project.createTask() {
	tasks.register(TASK_NAME, TodoCheckerTask::class.java) {
		it.jiraConfiguration.from(createConfiguration())
		it.outputFile.convention(outputFile())
	}
}

private fun Project.createConfiguration(): Configuration =
	configurations.create("jira") {
		it.isVisible = false
		it.isCanBeConsumed = false
		it.isCanBeResolved = true
		it.defaultDependencies { dependencies ->
			dependencies.add(project.dependencies.create("com.atlassian.jira:jira-rest-java-client-core:5.2.6"))
			dependencies.add(project.dependencies.create("io.atlassian.fugue:fugue:4.7.2"))
		}
	}

private fun Project.outputFile(): Provider<RegularFile> =
	layout.buildDirectory.file(OUTPUT_FILE_NAME)
