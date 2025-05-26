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

package de.gematik.test.tiger.fhir.validation.staticv;

import static org.assertj.core.api.Assertions.assertThat;

import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import de.gematik.rbellogger.data.RbelElement;
import de.gematik.refv.commons.validation.ValidationMessagesFilter;
import de.gematik.refv.commons.validation.ValidationModule;
import de.gematik.refv.commons.validation.ValidationOptions;
import de.gematik.refv.commons.validation.ValidationResult;
import de.gematik.test.tiger.glue.RBelValidatorGlue;
import io.cucumber.core.plugin.report.Evidence;
import io.cucumber.core.plugin.report.Evidence.Type;
import io.cucumber.core.plugin.report.EvidenceRecorder;
import io.cucumber.core.plugin.report.EvidenceRecorderFactory;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

/**
 * Glue code for Static-FHIR validation.
 *
 * <p>It allows you to validate FHIR resources against the FHIR specification.
 */
public class StaticFhirValidation {

  private static final String BODY_RBEL_PATH = "$.body";

  private final EvidenceRecorder evidenceRecorder = EvidenceRecorderFactory.getEvidenceRecorder();

  private final RBelValidatorGlue rBelValidatorGlue;

  public StaticFhirValidation() {
    rBelValidatorGlue = new RBelValidatorGlue();
  }

  public StaticFhirValidation(RBelValidatorGlue rBelValidatorGlue) {
    this.rBelValidatorGlue = rBelValidatorGlue;
  }

  public void tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(
      final ValidationModule validationModule) {
    tgrCurrentRequestAtIsValidFHIRResourceOfType(BODY_RBEL_PATH, validationModule);
  }

  public void tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(
      ValidationModule validationModule, String profileUrl) {
    tgrCurrentRequestAtIsValidFHIRResourceOfType(
        BODY_RBEL_PATH, validationModule, createValidationOptionsWithProfile(profileUrl));
  }

  public void tgrCurrentRequestAtIsValidFHIRResourceOfType(
      final String rbelPath, final ValidationModule validationModule) {
    ValidationOptions validationOptions = ValidationOptions.getDefaults();
    validationOptions.setValidationMessagesFilter(ValidationMessagesFilter.KEEP_ALL);
    tgrCurrentRequestAtIsValidFHIRResourceOfType(rbelPath, validationModule, validationOptions);
  }

  public void tgrCurrentRequestAtIsValidFHIRResourceOfType(
      String rbelPath, ValidationModule validationModule, String profileUrl) {
    tgrCurrentRequestAtIsValidFHIRResourceOfType(
        rbelPath, validationModule, createValidationOptionsWithProfile(profileUrl));
  }

  private void tgrCurrentRequestAtIsValidFHIRResourceOfType(
      String rbelPath, ValidationModule validationModule, ValidationOptions validationOptions) {
    final var validationResult =
        validateRbelElementAt(
            rBelValidatorGlue.getRbelMessageRetriever().findElementInCurrentRequest(rbelPath),
            validationModule,
            validationOptions);

    final String validationMessage = getCombinedValidationMessages(validationResult);

    assertThat(validationResult.isValid())
        .withFailMessage(
            "request body is not a valid %s resource. cause: \n%s",
            validationModule.getId(), validationMessage)
        .isTrue();
  }

  private static ValidationOptions createValidationOptionsWithProfile(String profile) {
    ValidationOptions validationOptions = ValidationOptions.getDefaults();
    validationOptions.setProfiles(List.of(profile));
    validationOptions.setValidationMessagesFilter(ValidationMessagesFilter.KEEP_ALL);
    return validationOptions;
  }

  private void recordEvidencesOf(final ValidationResult validationResult) {
    validationResult.getValidationMessages().stream()
        .map(StaticFhirValidation::toEvidence)
        .forEach(evidenceRecorder::recordEvidence);
  }

  @NotNull
  private static Evidence toEvidence(final SingleValidationMessage it) {
    final Type type =
        switch (it.getSeverity()) {
          case INFORMATION -> Type.INFO;
          case WARNING -> Type.WARN;
          case ERROR -> Type.ERROR;
          case FATAL -> Type.FATAL;
        };

    return new Evidence(type, it.getMessage(), it);
  }

  public void tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(
      final ValidationModule validationModule) {
    tgrCurrentResponseAtIsValidFHIRResourceOfType(BODY_RBEL_PATH, validationModule);
  }

  public void tgrCurrentResponseAtIsValidFHIRResourceOfType(
      final String rbelPath, final ValidationModule validationModule) {
    ValidationOptions validationOptions = ValidationOptions.getDefaults();
    validationOptions.setValidationMessagesFilter(ValidationMessagesFilter.KEEP_ALL);
    tgrCurrentResponseAtIsValidFHIRResourceOfType(rbelPath, validationModule, validationOptions);
  }

  public void tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(
      ValidationModule validationModule, String profileUrl) {
    tgrCurrentResponseAtIsValidFHIRResourceOfType(
        BODY_RBEL_PATH, validationModule, createValidationOptionsWithProfile(profileUrl));
  }

  public void tgrCurrentResponseAtIsValidFHIRResourceOfType(
      String rbelPath, ValidationModule validationModule, String profileUrl) {
    tgrCurrentResponseAtIsValidFHIRResourceOfType(
        rbelPath, validationModule, createValidationOptionsWithProfile(profileUrl));
  }

  private void tgrCurrentResponseAtIsValidFHIRResourceOfType(
      String rbelPath, ValidationModule validationModule, ValidationOptions validationOptions) {
    final var validationResult =
        validateRbelElementAt(
            rBelValidatorGlue.getRbelMessageRetriever().findElementInCurrentResponse(rbelPath),
            validationModule,
            validationOptions);

    final String validationMessage = getCombinedValidationMessages(validationResult);

    assertThat(validationResult.isValid())
        .withFailMessage(
            "response body is not a valid %s resource, cause: \n%s",
            validationModule.getId(), validationMessage)
        .isTrue();
  }

  @NotNull
  private ValidationResult validateRbelElementAt(
      RbelElement toValidate,
      ValidationModule validationModule,
      ValidationOptions validationOptions) {
    final var rawContentToValidate = toValidate.getRawStringContent();

    final var validationResult =
        validationModule.validateString(rawContentToValidate, validationOptions);

    recordEvidencesOf(validationResult);
    return validationResult;
  }

  private String getCombinedValidationMessages(ValidationResult validationResult) {
    return validationResult.getValidationMessages().stream()
        .filter(message -> message.getSeverity().equals(ResultSeverityEnum.ERROR))
        .map(message -> message.getSeverity() + " - " + message.getMessage())
        .collect(Collectors.joining("\n"));
  }
}
