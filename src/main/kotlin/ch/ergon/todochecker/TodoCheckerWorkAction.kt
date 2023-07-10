/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.logging.Logging
import org.gradle.workers.WorkAction
import java.io.IOException
import java.nio.file.Files

abstract class TodoCheckerWorkAction : WorkAction<TodoCheckerWorkParameters> {
	private val logger = Logging.getLogger(TodoCheckerWorkAction::class.java)

	override fun execute() {
		val directory = parameters.directory.asFile.get().toPath()
		val exclusions = readExclusions(parameters.exclusions)
		val inclusions = readInclusions(parameters.inclusions)
		logger.info("Scanning \"$directory\" for Todo")

		val todo = todoScannerFor(directory, exclusions, inclusions, parameters.jiraProject.get()).scan()
		val jiraRepository = JiraRepository(
			parameters.jiraUrl.get(),
			parameters.jiraUsername.get(),
			parameters.jiraPassword.get(),
			parameters.jiraResolvedStatuses.getOrElse(emptyList()),
		)

		val users = jiraRepository.getUsersForTodo(todo.keys)
		logger.info("Found ${users.size} users with Todo")
		val output = createOutput(users, todo)

		if (output.isNotBlank()) {
			logger.lifecycle(
				"""
Todo for resolved issues found:
$output
				""",
			)

			val file = parameters.outputFile.asFile.get().toPath()

			try {
				Files.writeString(file, output)
				logger.info("Todo written to file $file")
			} catch (e: IOException) {
				logger.error("Error writing to file $file", e)
			}
		} else {
			logger.lifecycle("No Todo for resolved issues found")
		}
	}

	private fun readExclusions(exclusions: RegularFileProperty): List<String> =
		exclusions.map { f -> f.asFile.useLines { it.toList() } }.getOrElse(emptyList())

	private fun readInclusions(inclusions: RegularFileProperty): List<String> =
		inclusions.map { f -> f.asFile.useLines { it.toList() } }.getOrElse(emptyList())
}