/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

sealed interface JiraCredentials {
	data class UsernamePassword(val username: String, val password: String) : JiraCredentials
	data class PersonalAccessToken(val token: String) : JiraCredentials
}
