rootProject.name = "todo-checker"

dependencyResolutionManagement {
	repositories {
		maven {
			url = uri("https://artifacts.ergon.ch/artifactory/proxy-maven-central/")
		}
		maven {
			url = uri("https://artifacts.ergon.ch/artifactory/proxy-atlassian/")
		}
	}
}
