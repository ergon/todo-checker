/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import com.atlassian.jira.rest.client.api.IssueRestClient
import com.atlassian.jira.rest.client.api.domain.Issue
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory
import org.codehaus.jettison.json.JSONObject
import java.net.URI

internal class JiraRepository(
	private val url: URI,
	private val username: String,
	private val password: String,
	private val resolvedStatuses: List<String>,
) {
	/**
	 * For a set of issues (specified by key) finds the associated users (either owner or reporter).
	 * Returns a mapping from user to their set of issue. If an issue has no owner or reporter, the key of the map is null.
	 */
	fun getUsersForTodo(issues: Set<JiraIssueKey>): Map<JiraUser?, Set<Issue>> =
		AsynchronousJiraRestClientFactory()
			.createWithBasicHttpAuthentication(url, username, password)
			.use { client ->
				issues.asSequence()
					.map(client.issueClient::fetchIssue)
					.filter(::isResolved)
					.groupByTo(mutableMapOf(), Issue::getOwnerOrReporter)
					.mapValues { it.value.toSet() }
			}

	private fun isResolved(issue: Issue): Boolean =
		if (resolvedStatuses.isEmpty()) {
			issue.status.statusCategory.name == "Done"
		} else {
			resolvedStatuses.any { it == issue.status.name }
		}
}

private fun IssueRestClient.fetchIssue(key: JiraIssueKey): Issue =
	getIssue(key.value).claim()

private fun Issue.getOwnerOrReporter(): JiraUser? {
	val owner = getFieldByName("Owner")?.value?.let {
		JiraUser((it as JSONObject).displayName)
	}
	return owner ?: reporter?.displayName?.let {
		JiraUser(it)
	}
}

private val JSONObject.displayName: String
	get() = getString("displayName")
