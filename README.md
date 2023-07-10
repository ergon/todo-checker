Todo Checker
============

Apply the Todo Checker plugin to a project.

```kotlin
plugins {
	id("ch.ergon.todochecker")
}
```

Configure required settings for the Todo Checker plugin.

```kotlin
todoChecker {
	jiraUrl.set(URI.create("https://jira.airlock.com"))
	jiraUsername.set(providers.environmentVariable("TODOCHECKER_USERNAME"))
	jiraPassword.set(providers.environmentVariable("TODOCHECKER_PASSWORD"))
	jiraProject.set("ALSAAS")
}
```

You can use the following settings to configure the plugin.

| Parameter            | Default value          | Required | Description                                                                                                                                         |
|----------------------|------------------------|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| directory            | project directory      | no       | The directory to scan for TODO markers.                                                                                                             |
| exclusions           | empty list             | no       | A file containing a Java Glob per line for file that the plugin should NOT scan.                                                                    |
| inclusions           | empty list             | no       | A file containing a Java Glob per line for file that the plugin should scan.                                                                        |
| jiraUrl              | -                      | yes      | The Jira URL.                                                                                                                                       |
| jiraUsername         | -                      | yes      | The username to connect to Jira.                                                                                                                    |
| jiraPassword         | -                      | yes      | The password to connect to Jira.                                                                                                                    |
| jiraProject          | -                      | yes      | The project key to match TODO markers.                                                                                                              |
| jiraResolvedStatuses | "Done" status category | no       | A list of statuses in which an issue is considered resolved.<br/>If not set, all issues that have a "Done" status category are considered resolved. |

