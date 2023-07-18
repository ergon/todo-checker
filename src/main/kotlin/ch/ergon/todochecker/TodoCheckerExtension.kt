/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import java.net.URI

interface TodoCheckerExtension {
	/**
	 * The directory to be scanned by the checker.
	 */
	val directory: DirectoryProperty

	/**
	 * The config file for exclusions.
	 */
	val exclusions: RegularFileProperty

	/**
	 * The config file for inclusions.
	 */
	val inclusions: RegularFileProperty

	/**
	 * The Jira URL that the plugin is connected to.
	 */
	val jiraUrl: Property<URI>
	val jiraUsername: Property<String>
	val jiraPassword: Property<String>
	val jiraPersonalAccessToken: Property<String>

	/**
	 * The Jira project key.
	 */
	val jiraProject: Property<String>

	/**
	 * The Jira statuses considered for reporting.
	 */
	val jiraResolvedStatuses: ListProperty<String>
}
