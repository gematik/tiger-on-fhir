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

import static de.gematik.test.tiger.glue.fhir.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.LIST;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import de.gematik.test.tiger.fhir.validation.fhirpath.FhirPathValidation;
import de.gematik.test.tiger.fhir.validation.fhirpath.NetTracer;
import io.cucumber.core.plugin.report.Evidence;
import io.cucumber.core.plugin.report.Evidence.Type;
import io.cucumber.core.plugin.report.EvidenceRecorder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.utils.FHIRLexer.FHIRLexerException;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

/**
 * Tests for {@link FhirPathValidationGlue}.
 *
 * <p>All positive evaluations are tested here in Detail. In addition, the happy paths of positive
 * as well as negative parts are testet in {@link features/fancy.feature}.
 */
class FhirPathValidationGlueTest {

  private FhirPathValidationGlue underTest;

  private final NetTracer netTracer = mock(NetTracer.class);
  private final EvidenceRecorder evidenceRecorder = mock(EvidenceRecorder.class);
  private final ArgumentCaptor<Evidence> evidenceCaptor = ArgumentCaptor.forClass(Evidence.class);

  @BeforeEach
  void setUp() {
    underTest = new FhirPathValidationGlue(new FhirPathValidation(evidenceRecorder, netTracer));
  }

  @Test
  @DisplayName("Empty request body should serve an appropriate error")
  void emptyRequestBodyShouldServeAnAppropriateError() {
    // Arrange
    when(netTracer.getCurrentRequestsRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.empty());
    when(netTracer.getCurrentRequestsRawStringByRbelPath("$.headers.content-type"))
        .thenReturn(Optional.of("application/fhir+json"));

    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath("true"));

    // Assert
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContaining("no element found with rbel at request");
    verify(evidenceRecorder, atLeastOnce()).recordEvidence(evidenceCaptor.capture());
    assertThat(evidenceCaptor.getValue())
        .hasType(Type.FATAL)
        .hasTitle("no element found with rbel at request");
  }

  @Test
  @DisplayName("Empty response body should serve an appropriate error")
  void emptyResponseBodyShouldServeAnAppropriateError() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.empty());
    when(netTracer.getCurrentResponseRawStringByRbelPath("$.headers.content-type"))
        .thenReturn(Optional.of("application/fhir+json"));

    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentResponseBodyEvaluatesTheFhirPath("true"));

    // Assert
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContaining("no element found with rbel at response");
    verify(evidenceRecorder, atLeastOnce()).recordEvidence(evidenceCaptor.capture());
    assertThat(evidenceCaptor.getValue())
        .hasType(Type.FATAL)
        .hasTitle("no element found with rbel at response");
  }

  @Test
  @DisplayName("Missing content header in request should serve an appropriate error")
  @SneakyThrows
  void fhirPathValidationJsonRequest_MissingContentHeaderInResponseShouldServeAnAppropriateError() {
    // Arrange
    when(netTracer.getCurrentRequestsRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    final String fhirPath = getTruthyResultFhirPath();

    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath));

    // Assert
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContaining("no Content-Type Header set for Request");
  }

  @Test
  @DisplayName("Missing content header in response should serve an appropriate error")
  @SneakyThrows
  void
      fhirPathValidationJsonResponse_MissingContentHeaderInResponseShouldServeAnAppropriateError() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.empty());
    final String fhirPath = getTruthyResultFhirPath();

    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentResponseBodyEvaluatesTheFhirPath(fhirPath));

    // Assert
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContaining("no Content-Type Header set for Response");
  }

  @ParameterizedTest
  @MethodSource("provideFhirResource")
  @DisplayName("at json Request should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationJsonRequest_ShouldEvaluateValidFhirPathExpressions(String fhirResource) {
    // Arrange
    requestServesValidJsonRessources(fhirResource);
    final String fhirPath = getTruthyResultFhirPath();

    // Act
    underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertTrue(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("at xml Request should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationXmlRequest_ShouldEvaluateValidFhirPathExpressions(String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);
    final String fhirPath = getTruthyResultFhirPath();

    // Act
    underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertTrue(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideISIP1_ISIK1FhirResource")
  @DisplayName("at xml Request should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationXmlRequest_ShouldEvaluateValidFhirPathExpressionsForISIP_ISIK(
      String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);
    final String fhirPath = getISIPResultFhirPath();

    // Act
    underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertTrue(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Request should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationXmlRequest_FailStepShouldEvaluateValidFhirPathExpressions(
      String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    // Act
    underTest.tgrCurrentRequestBodyFailesTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertFalse(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationXmlResponse_FailStepShouldEvaluateValidFhirPathExpressions(
      String fhirResource) {
    // Arrange
    responseServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    // Act
    underTest.tgrCurrentResponseBodyFailsTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertFalse(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void
      fhirPathValidationXmlResponseBody_FailStepShouldEvaluateValidFhirPathExpressionsAndShowErrorMessage(
          String fhirResource) {
    // Arrange
    responseServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    assertThatThrownBy(
            () ->
                underTest.tgrCurrentResponseBodyEvaluatesTheFhirPath(
                    fhirPath, "Achtung diese Fehlermeldung wird gezeigt"))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Achtung diese Fehlermeldung wird gezeigt");
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void
      fhirPathValidationXmlResponse_FailStepShouldEvaluateValidFhirPathExpressionsAndShowErrorMessage(
          String fhirResource) {
    // Arrange
    responseServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    assertThatThrownBy(
            () ->
                underTest.tgrCurrentResponseEvaluatesTheFhirPath(
                    FhirPathValidation.RBEL_SELECTOR_FOR_BODY,
                    fhirPath,
                    "Achtung diese Fehlermeldung wird gezeigt"))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Achtung diese Fehlermeldung wird gezeigt");
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void
      fhirPathValidationXmlRequestBody_FailStepShouldEvaluateValidFhirPathExpressionsAndShowErrorMessage(
          String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    assertThatThrownBy(
            () ->
                underTest.tgrCurrentRequestBodyEvaluatesTheFhirPath(
                    fhirPath, "Achtung diese Fehlermeldung wird gezeigt"))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Achtung diese Fehlermeldung wird gezeigt");
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("fail step at xml Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void
      fhirPathValidationXmlRequest_FailStepShouldEvaluateValidFhirPathExpressionsAndShowErrorMessage(
          String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);
    final String fhirPath = getFalsyResultFhirPath();

    assertThatThrownBy(
            () ->
                underTest.tgrCurrentRequestEvaluatesTheFhirPath(
                    FhirPathValidation.RBEL_SELECTOR_FOR_BODY,
                    fhirPath,
                    "Achtung diese Fehlermeldung wird gezeigt"))
        .isInstanceOf(AssertionError.class)
        .hasMessageContaining("Achtung diese Fehlermeldung wird gezeigt");
  }

  @ParameterizedTest
  @MethodSource("provideXMLFhirResource")
  @DisplayName(
      "at xml Request should log appropriate evidences on falsy boolean result of a fhir path"
          + " expression")
  @SneakyThrows
  void
      fhirPathValidationXmlRequest_ShouldLogAppropriateEvidencesOnFalsyBooleanResultOfAFhirPathExpression(
          String fhirResource) {
    // Arrange
    requestServesValidXmlRessources(fhirResource);

    // Act
    final String fhirPaths =
        getTruthyResultFhirPath()
            + "\n"
            + getFalsyResultFhirPath()
            + "\n"
            + getNonBooleanResultFhirPath()
            + "\n"
            + getInvalidFhirPath()
            + "\n"
            + "\n"
            + // empty line
            getEmptyResultFhirPath()
            + "\n"
            + getTruthyResultFhirPath()
            + "\n";
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentRequestBodyEvaluatesTheFhirPaths(fhirPaths));

    // Assert
    final String falsyErrorMessage =
        "1 value(s) in the evaluation results are false for: " + getFalsyResultFhirPath();
    final String nonBooleanErrorMessage =
        "7 non boolean results for: " + getNonBooleanResultFhirPath();
    final String invalidFhirPathErrorMessage = "invalid FHIRPath: " + getInvalidFhirPath();
    final String emptyErrorMessage = "Empty results for: " + getEmptyResultFhirPath();
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContainingAll(falsyErrorMessage, invalidFhirPathErrorMessage, emptyErrorMessage);

    verify(evidenceRecorder, atLeastOnce()).recordEvidence(evidenceCaptor.capture());

    org.assertj.core.api.Assertions.assertThat(evidenceCaptor.getAllValues())
        .satisfiesExactly(
            evidence ->
                assertThat(evidence)
                    .as("valid")
                    .hasType(Type.INFO)
                    .hasTitle(getTruthyResultFhirPath())
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertTrue(((BooleanType) it).booleanValue()))),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.ERROR)
                    .hasTitle(falsyErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertFalse(((BooleanType) it).booleanValue()))),
            evidence ->
                assertThat(evidence)
                    .as("Non Boolean")
                    .hasType(Type.ERROR)
                    .hasTitle(nonBooleanErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(Bundle.BundleEntryComponent.class)
                    .hasSize(7)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .isNotEmpty()
                                .allSatisfy(
                                    it ->
                                        org.assertj.core.api.Assertions.assertThat(it)
                                            .isInstanceOf(Bundle.BundleEntryComponent.class))),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.FATAL)
                    .hasTitle(invalidFhirPathErrorMessage)
                    .extracting(Evidence::getDetails)
                    .isInstanceOf(FHIRLexerException.class),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.ERROR)
                    .hasTitle(emptyErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .isEmpty(),
            evidence ->
                assertThat(evidence)
                    .as("valid")
                    .hasType(Type.INFO)
                    .hasTitle(getTruthyResultFhirPath())
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertTrue(((BooleanType) it).booleanValue()))));
  }

  @ParameterizedTest
  @MethodSource("provideXMLWithEauAndIsikFhirResource")
  @DisplayName("at XML Response should evaluate valid fhir path expressions")
  @SneakyThrows
  void fhirPathValidationXmlResponse_ShouldEvaluateValidFhirPathExpressions(String fhirResource) {
    // Arrange
    responseServesValidXmlRessources(fhirResource);
    final String fhirPath = getTruthyResultFhirPath();

    // Act
    underTest.tgrCurrentResponseBodyEvaluatesTheFhirPath(fhirPath);

    // Assert
    verify(evidenceRecorder).recordEvidence(evidenceCaptor.capture());

    final Evidence value = evidenceCaptor.getValue();
    assertThat(value)
        .hasType(Type.INFO)
        .hasTitle(fhirPath)
        .extracting(Evidence::getDetails)
        .asInstanceOf(LIST)
        .hasOnlyElementsOfTypes(BooleanType.class)
        .hasSize(1)
        .satisfies(
            details ->
                org.assertj.core.api.Assertions.assertThat(details)
                    .satisfiesExactly(it -> assertTrue(((BooleanType) it).booleanValue())));
  }

  @ParameterizedTest
  @MethodSource("provideXMLFhirResource")
  @DisplayName(
      "at XML Response should log appropriate evidences on falsy boolean result of a fhir path"
          + " expression")
  @SneakyThrows
  void
      fhirPathValidationXmlResponse_ShouldLogAppropriateEvidencesOnFalsyBooleanResultOfAFhirPathExpression(
          String fhirResource) {
    // Arrange
    responseServesValidXmlRessources(fhirResource);

    // Act
    final String fhirPaths =
        getTruthyResultFhirPath()
            + "\n"
            + getFalsyResultFhirPath()
            + "\n"
            + getNonBooleanResultFhirPath()
            + "\n"
            + getInvalidFhirPath()
            + "\n"
            + "\n"
            + // empty line
            getEmptyResultFhirPath()
            + "\n"
            + getTruthyResultFhirPath()
            + "\n";
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () -> underTest.tgrCurrentResponseBodyEvaluatesTheFhirPaths(fhirPaths));

    // Assert
    final String falsyErrorMessage =
        "1 value(s) in the evaluation results are false for: " + getFalsyResultFhirPath();
    final String nonBooleanErrorMessage =
        "7 non boolean results for: " + getNonBooleanResultFhirPath();
    final String invalidFhirPathErrorMessage = "invalid FHIRPath: " + getInvalidFhirPath();
    final String emptyErrorMessage = "Empty results for: " + getEmptyResultFhirPath();
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContainingAll(
            falsyErrorMessage,
            nonBooleanErrorMessage,
            invalidFhirPathErrorMessage,
            emptyErrorMessage);

    verify(evidenceRecorder, atLeastOnce()).recordEvidence(evidenceCaptor.capture());

    org.assertj.core.api.Assertions.assertThat(evidenceCaptor.getAllValues())
        .satisfiesExactly(
            evidence ->
                assertThat(evidence)
                    .as("valid")
                    .hasType(Type.INFO)
                    .hasTitle(getTruthyResultFhirPath())
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertTrue(((BooleanType) it).booleanValue()))),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.ERROR)
                    .hasTitle(falsyErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertFalse(((BooleanType) it).booleanValue()))),
            evidence ->
                assertThat(evidence)
                    .as("Non Boolean")
                    .hasType(Type.ERROR)
                    .hasTitle(nonBooleanErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(Bundle.BundleEntryComponent.class)
                    .hasSize(7)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .isNotEmpty()
                                .allSatisfy(
                                    it ->
                                        org.assertj.core.api.Assertions.assertThat(it)
                                            .isInstanceOf(Bundle.BundleEntryComponent.class))),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.FATAL)
                    .hasTitle(invalidFhirPathErrorMessage)
                    .extracting(Evidence::getDetails)
                    .isInstanceOf(FHIRLexerException.class),
            evidence ->
                assertThat(evidence)
                    .as("Falsy")
                    .hasType(Type.ERROR)
                    .hasTitle(emptyErrorMessage)
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .isEmpty(),
            evidence ->
                assertThat(evidence)
                    .as("valid")
                    .hasType(Type.INFO)
                    .hasTitle(getTruthyResultFhirPath())
                    .extracting(Evidence::getDetails)
                    .asInstanceOf(LIST)
                    .hasOnlyElementsOfTypes(BooleanType.class)
                    .hasSize(1)
                    .satisfies(
                        details ->
                            org.assertj.core.api.Assertions.assertThat(details)
                                .satisfiesExactly(
                                    it -> assertTrue(((BooleanType) it).booleanValue()))));
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " one boolean result")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldEvaluateOneValidFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getTruthyResultFhirPath();

    String variable = "keyVar";
    // Act
    underTest
        .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
            fhirPath, variable);
    // Assert
    org.assertj.core.api.Assertions.assertThat(TigerGlobalConfiguration.readString(variable))
        .isEqualTo("true");
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " one int result")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldEvaluateOneIntValidFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getCountResultFhirPath();

    String variable = "keyVar";
    // Act
    underTest
        .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
            fhirPath, variable);
    // Assert
    org.assertj.core.api.Assertions.assertThat(TigerGlobalConfiguration.readString(variable))
        .isEqualTo("1");
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " one dateAndTime result")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldEvaluateOneDateValidFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getDateResultFhirPath();

    String variable = "keyVar";
    // Act
    underTest
        .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
            fhirPath, variable);
    // Assert
    org.assertj.core.api.Assertions.assertThat(TigerGlobalConfiguration.readString(variable))
        .isEqualTo("2022-05-20T08:00:00Z");
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " two results")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldEvaluateTwoResultsValidFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getMoreResultFhirPath();

    String variable = "keyVar";
    // Act
    underTest
        .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
            fhirPath, variable);
    // Assert
    org.assertj.core.api.Assertions.assertThat(TigerGlobalConfiguration.readString(variable))
        .isEqualTo("Practitioner");
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " complex result")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldEvaluateComplexFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getComplexResultFhirPath();

    String variable = "keyVar";
    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () ->
                underTest
                    .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
                        fhirPath, variable));

    // Assert
    final String complexFhirPathErrorMessage =
        "result is not a primitive type for FHIRPath: " + getComplexResultFhirPath();
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContainingAll(complexFhirPathErrorMessage);
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " invalid FHIRPath")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldFailWithInvalidFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getInvalidFhirPath();

    String variable = "keyVar";
    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () ->
                underTest
                    .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
                        fhirPath, variable));

    // Assert
    final String invalidFhirPathErrorMessage = "invalid FHIRPath: " + getInvalidFhirPath();
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContainingAll(invalidFhirPathErrorMessage);
  }

  @Test
  @DisplayName(
      "at JSON Response should evaluate valid fhir path expressions and store it to a variable -"
          + " empty result")
  @SneakyThrows
  void
      fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString_ShouldFailWithEmptyFhirPathExpressions() {
    // Arrange
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(getFhirRessourceAsJson()));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
    final String fhirPath = getEmptyResultFhirPath();

    String variable = "";
    // Act
    final var thrownException =
        assertThrows(
            AssertionError.class,
            () ->
                underTest
                    .fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
                        fhirPath, variable));

    // Assert
    final String emptyFhirPathErrorMessage =
        "result is null or empty for FHIRPath: " + getEmptyResultFhirPath();
    org.assertj.core.api.Assertions.assertThat(thrownException)
        .hasMessageContainingAll(emptyFhirPathErrorMessage);
  }

  @NotNull
  private static String getInvalidFhirPath() {
    return "%&/()=?";
  }

  @NotNull
  private static String getEmptyResultFhirPath() {
    return "Bundle.entry.wurst";
  }

  @NotNull
  private static String getNonBooleanResultFhirPath() {
    return "Bundle.entry";
  }

  @NotNull
  private static String getFalsyResultFhirPath() {
    return "Bundle.entry.resource.author.type.where(value = \"Dinosaur\").exists()";
  }

  private void requestServesValidXmlRessources(String fhirResource) {
    when(netTracer.getCurrentRequestsRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(fhirResource));
    when(netTracer.getCurrentRequestsRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/xml"));
  }

  private void requestServesValidJsonRessources(String fhirResource) {
    when(netTracer.getCurrentRequestsRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(fhirResource));
    when(netTracer.getCurrentRequestsRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/json"));
  }

  private void responseServesValidXmlRessources(String fhirResource) {
    when(netTracer.getCurrentResponseRawStringByRbelPath(FhirPathValidation.RBEL_SELECTOR_FOR_BODY))
        .thenReturn(Optional.of(fhirResource));
    when(netTracer.getCurrentResponseRawStringByRbelPath(
            FhirPathValidation.RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER))
        .thenReturn(Optional.of("application/xml"));
  }

  @NotNull
  private static String getTruthyResultFhirPath() {
    return "Bundle.entry.resource.author.type.where(value = \"Device\").exists()";
  }

  @NotNull
  private static String getCountResultFhirPath() {
    return "Bundle.entry.resource.author.type.where(value = \"Device\").count()";
  }

  @NotNull
  private static String getMoreResultFhirPath() {
    return "Bundle.entry.resource.author.type.where(value != \"Practitioner\" or value !="
        + " \"Device\")";
  }

  @NotNull
  private static String getDateResultFhirPath() {
    return "Bundle.entry.resource.date";
  }

  @NotNull
  private static String getComplexResultFhirPath() {
    return "Bundle.entry.resource.address";
  }

  @NotNull
  private static String getISIPResultFhirPath() {
    return "entry.resource.all(address.where(city contains \"Musterhausen\").exists())";
  }

  private static Stream<Arguments> provideISIP1_ISIK1FhirResource() {
    return Stream.of(
        Arguments.of(getISIK1RessourceAsXml()), Arguments.of(getISIP1RessourceAsXml()));
  }

  private static Stream<Arguments> provideFhirResource() {
    return Stream.of(
        Arguments.of(getFhirRessourceAsJson()),
        Arguments.of(getEAURessourceAsJson()),
        Arguments.of(getISIK2RessourceAsJson()));
  }

  private static Stream<Arguments> provideXMLFhirResource() {
    return Stream.of(Arguments.of(getFhirRessourceAsXml()));
  }

  private static Stream<Arguments> provideXMLWithEauAndIsikFhirResource() {
    return Stream.of(
        Arguments.of(getFhirRessourceAsXml()),
        Arguments.of(getEAURessourceAsXml()),
        Arguments.of(getISIK2RessourceAsXml()));
  }

  private static String getFhirRessourceAsXml() {
    return readRessourceToString("testFhirRessource.xml");
  }

  private static String getFhirRessourceAsJson() {
    return readRessourceToString("testFhirRessource.json");
  }

  private static String getEAURessourceAsJson() {
    return readRessourceToString("eau-test.json");
  }

  private static String getISIK2RessourceAsJson() {
    return readRessourceToString("isik2-test.json");
  }

  private static String getISIK1RessourceAsXml() {
    return readRessourceToString("isik1-test.xml");
  }

  private static String getEAURessourceAsXml() {
    return readRessourceToString("eau-test.xml");
  }

  private static String getISIK2RessourceAsXml() {
    return readRessourceToString("isik2-test.xml");
  }

  private static String getISIP1RessourceAsXml() {
    return readRessourceToString("isip1-test.xml");
  }

  @SneakyThrows
  public static String readRessourceToString(String ressourceName) {
    return Files.readString(
        Path.of(
            Objects.requireNonNull(FhirPathValidationGlueTest.class.getResource(ressourceName))
                .toURI()));
  }
}
