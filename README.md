Todo Checker
============

The Todo Checker plugin will look for the pattern `(?i)(TODO|FIXME)\\s+($jiraProject-\\d+)` (e.g. "TODO ALSAAS-123: some
comment") in text files. If any of the referenced Jira issues are resolved, it will report the file with the issue owner
or reporter. There should be no TODO markers in the source code for resolved issues.

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

| Parameter               | Default value                                         | Required | Description                                                                                                                                                 |
|-------------------------|-------------------------------------------------------|----------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| directory               | project directory                                     | no       | The directory to scan for TODO markers.                                                                                                                     |
| exclusions              | empty list                                            | no       | A file containing a Java Glob per line for files that the plugin should NOT scan.                                                                           |
| inclusions              | empty list                                            | no       | A file containing a Java Glob per line for files that the plugin should always scan.<br/>Otherwise, files that don't match will be tested for text content. |
| jiraUrl                 | -                                                     | yes      | The Jira URL.                                                                                                                                               |
| jiraUsername            | -                                                     | yes      | The username to connect to Jira (use this OR jiraPersonalAccessToken)                                                                                       |
| jiraPassword            | -                                                     | yes      | The password to connect to Jira (use this OR jiraPersonalAccessToken)                                                                                       |
| jiraPersonalAccessToken | -                                                     | no       | Personal access token (may be used instead of username+password)                                                                                            |
| jiraProject             | -                                                     | yes      | The Jira project key to match TODO markers.                                                                                                                 |
| jiraResolvedStatuses    | "Done" status category                                | no       | A list of statuses in which an issue is considered resolved.<br/>If not set, all issues that have a "Done" status category are considered resolved.         |
| todoRegex               | ```(?i)(TODO\|FIXME)\s+(?<ticket>$jiraProject-\d+)``` | no       | The regex used for searching TODOs and recognizing associated JIRA tickets.  Must contain a named capture group "ticket" which is used to extract the matched JIRA ticket. (e.g.                                                                          |

An exclusion file looks as follows.

```text
**/.gradle/**
.git/**
.idea/**
**/node_modules/**
**/build/**
```

An inclusion file could look like this.

```text
**.java
**.jsp
**.js
**.kt
**.kts
**.ts
**.gradle
**.properties
**.xml
**.go
```
