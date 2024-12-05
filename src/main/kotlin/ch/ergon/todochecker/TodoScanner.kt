package ch.ergon.todochecker

import org.gradle.api.logging.Logging
import java.io.IOException
import java.io.UncheckedIOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Function
import java.util.function.Predicate
import java.util.regex.Pattern
import java.util.stream.Collectors

internal fun todoScannerFor(
	directory: Path,
	exclusions: List<String>,
	inclusions: List<String>,
	jiraProject: String,
	todoRegex: String?,
): TodoScanner =
	TodoScanner(directory, exclusions, inclusions, jiraProject, todoRegex)

internal class TodoScanner internal constructor(
	private val directory: Path,
	exclusions: List<String>,
	inclusions: List<String>,
	private val jiraProject: String,
	private val todoRegex: String?,
) {
	private val logger = Logging.getLogger(TodoScanner::class.java)
	private val exclusions: String
	private val inclusions: String

	init {
		this.exclusions = "glob:{" + exclusions.joinToString(separator = ",") + "}"
		this.inclusions = "glob:{" + inclusions.joinToString(separator = ",") + "}"
	}

	/**
	 * Walks over all files in the directory and returns a mapping between Jira issues and files.
	 */
	fun scan(): Map<JiraIssueKey, Set<Path>> =
		Files.walk(directory).use { stream ->
			stream
				.filter { path -> Files.isRegularFile(path) }
				.filter { path -> notExcluded(path, directory) }
				.filter(isIncluded(directory).or(hasTextContent()))
				.flatMap { path -> getTodoForFile(path).entries.stream() }
				.collect(
					Collectors.groupingBy(
						{ (key) -> key },
						Collectors.mapping(
							{ (_, path) -> directory.relativize(path) },
							Collectors.toSet(),
						),
					),
				)
		}

	private fun notExcluded(file: Path, directory: Path): Boolean {
		val relativizedPath = directory.relativize(file)
		val excluded = FileSystems.getDefault()
			.getPathMatcher(exclusions)
			.matches(relativizedPath)
		if (excluded) {
			logger.info("Skipping file \"$relativizedPath\": Excluded")
		}
		return !excluded
	}

	private fun isIncluded(directory: Path): Predicate<Path> =
		Predicate { path ->
			val relativizedPath = directory.relativize(path)
			FileSystems.getDefault()
				.getPathMatcher(inclusions)
				.matches(relativizedPath)
		}

	private fun hasTextContent(): Predicate<Path> =
		Predicate { path: Path ->
			val contentType = Files.probeContentType(path)
			when {
				contentType == null -> {
					logger.warn("Skipping file \"$path\": Unknown content type")
					false
				}

				!contentType.startsWith("text/") -> {
					logger.warn("Skipping file \"$path\": Not a text file ($contentType)")
					false
				}

				else -> true
			}
		}

	/**
	 * Gets all Todo for a specific file and returns a Map<"ABC-123", Filename>.
	 */
	private fun getTodoForFile(file: Path): Map<JiraIssueKey, Path> =
		try {
			Files.lines(file).use {
				it.flatMap { line -> getTodoForLine(line).stream() }
					.collect(
						Collectors.toMap(
							Function.identity(),
							{ file },
							{ existing, _ -> existing },
						),
					)
			}
		} catch (e: IOException) {
			logger.warn("Skipping file \"$file\": ${e.message}")
			mapOf()
		} catch (e: UncheckedIOException) {
			logger.warn("Skipping file \"$file\": ${e.message}")
			mapOf()
		}

	/**
	 * Finds all lines containing our common issue format which is enforced by the ide.
	 * The format is case insensitive and starts with either TODO or FIXME
	 * followed by at least one whitespace character and <issue key>.
	 */
	private fun getTodoForLine(line: String): List<JiraIssueKey> {
		val pattern = Pattern.compile(todoRegex ?: "(?i)(TODO|FIXME)\\s+(?<ticket>$jiraProject-\\d+)")
		val matcher = pattern.matcher(line)
		val todo: MutableList<JiraIssueKey> = ArrayList()

		while (matcher.find()) {
			todo.add(JiraIssueKey(matcher.group("ticket")))
		}

		return todo
	}
}
