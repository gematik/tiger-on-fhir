/*
Copyright 2023 gematik GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package de.gematik.test.tiger.glue.fhir;

import io.cucumber.java.en.Then;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Helper {

    @SneakyThrows
    @Then("in {string} exists a file matching {string} containing all of the following lines:")
    public void inExistsAFileMatchingContainingAllOfTheFollowingLines(
        final String reportDir,
        final String reportFilePattern, final String expectedLines) {

        try (final var files = Files.walk(Paths.get(reportDir))) {
            var report = files.filter(it -> it.getFileName().toString().matches(reportFilePattern))
                .sorted(byModificationTime().reversed())
                .limit(1)
                .map(Helper::toFileContent)
                .findFirst();

            final var lines = expectedLines.lines()
                .map(it -> (CharSequence) it)
                .collect(Collectors.toList());

            assertThat(report)
                .isPresent()
                .hasValueSatisfying(reportString ->
                    assertThat(reportString)
                        .contains(lines));
        }
    }

    @SneakyThrows
    @Then("in {string} exists no file matching {string} containing any of the following lines:")
    public void inExistsNoFileMatchingContainingAnyOfTheFollowingLines(
            final String reportDir,
            final String reportFilePattern, final String expectedLines) {

        try (final var files = Files.walk(Paths.get(reportDir))) {
            var report = files.filter(it -> it.getFileName().toString().matches(reportFilePattern))
                    .sorted(byModificationTime().reversed())
                    .limit(1)
                    .map(Helper::toFileContent)
                    .findFirst();

            final var lines = expectedLines.lines()
                    .map(it -> (CharSequence) it)
                    .collect(Collectors.toList());

            assertThat(report)
                    .isPresent()
                    .hasValueSatisfying(reportString ->
                            assertThat(reportString)
                                    .doesNotContain(lines));
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
