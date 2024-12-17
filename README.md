Todo Checker
============

The Todo Checker plugin looks for the pattern `(?i)(TODO|FIXME)\\s+($jiraProject-\\d+)` (e.g. "TODO MYPROJECT-123: some
comment") in text files. If any of the referenced Jira issues are resolved, it will report the file together with the
issue owner or reporter. There should be no TODO markers in the source code for resolved issues.

## Configure Todo Checker in Gradle

Update `settings.gradle.kts` to include Atlassian dependency:

```kotlin
dependencyResolutionManagement {
	repositories {
		maven {
			url = uri("https://packages.atlassian.com/mvn/maven-external/")
		}
	}
}
```

Configure Todo Checker in `build.gradle.kts`:

```kotlin
todoChecker {
    jiraUrl.set(URI.create("https://jira.mycompany.com"))
    jiraUsername.set(providers.environmentVariable("TODOCHECKER_USERNAME"))
    jiraPassword.set(providers.environmentVariable("TODOCHECKER_PASSWORD"))
    jiraProject.set("MYPROJECT")
}
```

## Execute the task

```shell
./gradlew checkTodo
```

## All options

You can use the following options to configure the plugin.

| Parameter               | Default value                                         | Required | Description                                                                                                                                                                |
|-------------------------|-------------------------------------------------------|----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| directory               | project directory                                     | no       | The directory to scan for TODO markers.                                                                                                                                    |
| exclusions              | empty list                                            | no       | A file containing a Java Glob per line for files that the plugin should NOT scan.                                                                                          |
| inclusions              | empty list                                            | no       | A file containing a Java Glob per line for files that the plugin should always scan.<br/>Otherwise, files that don't match will be tested for text content.                |
| jiraUrl                 | -                                                     | yes      | The Jira URL.                                                                                                                                                              |
| jiraUsername            | -                                                     | no       | The username to connect to Jira (use this OR jiraPersonalAccessToken). May also be passed as gradle property, e.g. set in gradle.properties.                               |
| jiraPassword            | -                                                     | no       | The password to connect to Jira (use this OR jiraPersonalAccessToken). May also be passed as gradle property, e.g. set in gradle.properties.                               |
| jiraPersonalAccessToken | -                                                     | no       | Personal access token (may be used instead of username+password). May also be passed as gradle property, e.g. set in gradle.properties.                                    |
| jiraProject             | -                                                     | yes      | The Jira project key to match TODO markers.                                                                                                                                |
| jiraResolvedStatuses    | "Done" status category                                | no       | A list of statuses in which an issue is considered resolved.<br/>If not set, all issues that have a "Done" status category are considered resolved.                        |
| todoRegex               | ```(?i)(TODO\|FIXME)\s+(?<ticket>$jiraProject-\d+)``` | no       | The regex used for searching TODOs and recognizing associated JIRA tickets.  Must contain a named capture group "ticket" which is used to extract the matched JIRA ticket. |

### Exclusion file example

An exclusion file looks as follows.

```text
**/.gradle/**
.git/**
.idea/**
**/node_modules/**
**/build/**
```

### Inclusion file example

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

License
-------
Todo Checker is released under the [MIT License](LICENSE).
