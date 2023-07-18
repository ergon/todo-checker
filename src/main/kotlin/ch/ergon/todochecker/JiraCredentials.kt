/*
 * Copyright (c) Ergon Informatik AG, Switzerland
 */

package ch.ergon.todochecker

import java.io.Serializable

sealed interface JiraCredentials {
	data class UsernamePassword(val username: String, val password: String) : JiraCredentials, Serializable {
		companion object {
			private const val serialVersionUID: Long = 1
		}
	}
	data class PersonalAccessToken(val token: String) : JiraCredentials, Serializable {
		companion object {
			private const val serialVersionUID: Long = 1
		}
	}
}
