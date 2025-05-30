/*
 *
 * Copyright 2023-2025 gematik GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * *******
 *
 * For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
 */

package de.gematik.test.tiger.glue.fhir;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Helper {

  @SneakyThrows
  @Then(
      "in {tigerResolvedString} exists a file matching {tigerResolvedString} containing all of the"
          + " following lines:")
  public void inExistsAFileMatchingContainingAllOfTheFollowingLines(
      final String reportDir, final String reportFilePattern, final String expectedLines) {

    try (final var files = Files.walk(Paths.get(reportDir))) {
      var report =
          files
              .filter(it -> it.getFileName().toString().matches(reportFilePattern))
              .sorted(byModificationTime().reversed())
              .limit(1)
              .findFirst();

      final var lines = expectedLines.lines().map(it -> (CharSequence) it).toList();

      if (report.isEmpty()) {
        log.error("No report matching filename regex found in folder {}", Paths.get(reportDir).toFile().getAbsolutePath());
        throw new AssertionError("No report matching filename regex found in folder " + Paths.get(reportDir).toFile().getAbsolutePath());
      } else {
        log.info("Checking report file {} for content", report.get().toFile().getAbsolutePath());
      }
      assertThat(Helper.toFileContent(report.get()))
              .contains(lines);
    }
  }

  @SneakyThrows
  @Then(
      "in {tigerResolvedString} exists no file matching {tigerResolvedString} containing any of the"
          + " following lines:")
  public void inExistsNoFileMatchingContainingAnyOfTheFollowingLines(
      final String reportDir, final String reportFilePattern, final String expectedLines) {

    try (final var files = Files.walk(Paths.get(reportDir))) {
      var report =
          files
              .filter(it -> it.getFileName().toString().matches(reportFilePattern))
              .sorted(byModificationTime().reversed())
              .limit(1)
              .map(Helper::toFileContent)
              .findFirst();

      final var lines = expectedLines.lines().map(it -> (CharSequence) it).toList();

      assertThat(report)
          .isPresent()
          .hasValueSatisfying(reportString -> assertThat(reportString).doesNotContain(lines));
    }
  }

  private static Comparator<Path> byModificationTime() {
    return Comparator.comparing(it -> it.getFileName().toString());
  }

  @SneakyThrows
  private static String toFileContent(final Path path) {
    return Files.readString(path);
  }
}
