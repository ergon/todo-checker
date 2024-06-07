package ch.ergon.todochecker

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkParameters
import java.net.URI

internal interface TodoCheckerWorkParameters : WorkParameters {
	val outputFile: RegularFileProperty
	val directory: DirectoryProperty
	val exclusions: RegularFileProperty
	val inclusions: RegularFileProperty
	val jiraUrl: Property<URI>
	val jiraCredentials: Property<JiraCredentials>
	val jiraProject: Property<String>
	val jiraResolvedStatuses: ListProperty<String>
	val todoRegex: Property<String>
	val failOnResolvedTodos: Property<Boolean>
}
