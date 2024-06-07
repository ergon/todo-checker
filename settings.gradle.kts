rootProject.name = "todo-checker"

dependencyResolutionManagement {
	repositories {
		mavenCentral()
		maven {
			url = uri("https://packages.atlassian.com/mvn/maven-external/")
		}
	}
}
