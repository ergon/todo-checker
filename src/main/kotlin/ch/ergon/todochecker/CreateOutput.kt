/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import com.atlassian.jira.rest.client.api.domain.Issue
import java.nio.file.Path

internal fun createOutput(
	users: Map<JiraUser?, Set<Issue>>,
	todos: Map<JiraIssueKey, Set<Path>>,
): String =
	users.entries.asSequence()
		.map { entry -> createOutputForUser(entry, todos) }
		.joinToString(System.lineSeparator() + System.lineSeparator())

private fun createOutputForUser(
	entry: Map.Entry<JiraUser?, Set<Issue>>,
	todos: Map<JiraIssueKey, Set<Path>>,
): String =
	createUsername(entry.key) +
		System.lineSeparator() +
		createIssueList(entry.value, todos)

private fun createUsername(user: JiraUser?): String =
	user?.run { "$displayName:" } ?: "Unknown User:"

private fun createIssueList(
	issues: Set<Issue>,
	todos: Map<JiraIssueKey, Set<Path>>,
): String =
	issues.asSequence()
		.map { issue -> createOutputForIssue(issue, todos) }
		.joinToString(System.lineSeparator())

private fun createOutputForIssue(
	issue: Issue,
	todos: Map<JiraIssueKey, Set<Path>>,
): String =
	createIssueKey(issue) +
		System.lineSeparator() +
		createFileList(issue, todos)

private fun createIssueKey(issue: Issue): String = "- ${issue.key}"

private fun createFileList(
	issue: Issue,
	todos: Map<JiraIssueKey, Set<Path>>,
): String =
	todos[JiraIssueKey(issue.key)].orEmpty().asSequence()
		.map(::createOutputForFile)
		.joinToString(System.lineSeparator())

private fun createOutputForFile(path: Path): String = "  - $path"
