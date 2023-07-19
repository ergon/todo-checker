/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

abstract class TodoCheckerTask @Inject constructor(
	private val workerExecutor: WorkerExecutor,
) : DefaultTask() {
	init {
		// We always want to run this task
		outputs.upToDateWhen { false }
	}

	@get:InputFiles
	abstract val jiraConfiguration: ConfigurableFileCollection

	@get:OutputFile
	abstract val outputFile: RegularFileProperty

	@TaskAction
	fun checkTodo() {
		val extension = project.extensions.getByType(TodoCheckerExtension::class.java)
		val workQueue = workerExecutor.classLoaderIsolation {
			it.classpath.from(jiraConfiguration)
		}

		workQueue.submit(TodoCheckerWorkAction::class.java) {
			it.outputFile.set(this@TodoCheckerTask.outputFile)
			it.directory.set(extension.directory)
			it.exclusions.set(extension.exclusions)
			it.inclusions.set(extension.inclusions)
			it.jiraUrl.set(extension.jiraUrl)
			it.jiraCredentials.set(getCredentials(project, extension))
			it.jiraProject.set(extension.jiraProject)
			it.jiraResolvedStatuses.set(extension.jiraResolvedStatuses)
			it.todoRegex.set(extension.todoRegex)
		}
	}

	private fun getCredentials(project: Project, extension: TodoCheckerExtension): JiraCredentials {
		val token = project.findProperty("jiraPersonalAccessToken") as String?
			?: extension.jiraPersonalAccessToken.getOrNull()
		return if (token != null) {
			JiraCredentials.PersonalAccessToken(token)
		} else {
			JiraCredentials.UsernamePassword(
				username = project.findProperty("jiraUsername") as String? ?: extension.jiraUsername.get(),
				password = project.findProperty("jiraPassword") as String? ?: extension.jiraPassword.get(),
			)
		}
	}
}
