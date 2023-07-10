rootProject.name = "todo-checker"

dependencyResolutionManagement {
	repositories {
		mavenCentral()
		maven {
			url = uri("https://artifacts.ergon.ch/artifactory/proxy-atlassian/")
		}
	}
}
