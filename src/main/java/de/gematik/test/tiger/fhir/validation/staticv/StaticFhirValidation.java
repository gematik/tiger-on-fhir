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

package de.gematik.test.tiger.fhir.validation.staticv;

import ca.uhn.fhir.validation.SingleValidationMessage;
import de.gematik.rbellogger.data.RbelElement;
import de.gematik.refv.SupportedValidationModule;
import de.gematik.refv.ValidationModuleFactory;
import de.gematik.refv.commons.validation.ValidationModule;
import de.gematik.refv.commons.validation.ValidationOptions;
import de.gematik.refv.commons.validation.ValidationResult;
import de.gematik.test.tiger.lib.rbel.RbelMessageValidator;
import io.cucumber.core.plugin.report.Evidence;
import io.cucumber.core.plugin.report.Evidence.Type;
import io.cucumber.core.plugin.report.EvidenceRecorder;
import io.cucumber.core.plugin.report.EvidenceRecorderFactory;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Glue code for Static-FHIR validation.
 * <p>
 * It allows you to validate FHIR resources against the FHIR specification.
 */
public class StaticFhirValidation {

    private static final String BODY_RBEL_PATH = "$.body";
    private final Map<SupportedValidationModule, ValidationModule> validationModuleCache =
            new EnumMap<>(SupportedValidationModule.class);

    private static final RbelMessageValidator rbelValidator = RbelMessageValidator.instance;

    private final EvidenceRecorder evidenceRecorder = EvidenceRecorderFactory.getEvidenceRecorder();

    public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(
            final SupportedValidationModule validationType) {
        tgrCurrentRequestAtIsValidFHIRRessourceOfType(BODY_RBEL_PATH, validationType);
    }

    public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(SupportedValidationModule validationType, String profileUrl) {
        tgrCurrentRequestAtIsValidFHIRRessourceOfType(BODY_RBEL_PATH, validationType, createValidationOptionsWithProfile(profileUrl));
    }

    public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(final String rbelPath,
                                                              final SupportedValidationModule validationType) {
        tgrCurrentRequestAtIsValidFHIRRessourceOfType(rbelPath, validationType, ValidationOptions.getDefaults());
    }

    public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(String rbelPath, SupportedValidationModule validationType, String profileUrl) {
        tgrCurrentRequestAtIsValidFHIRRessourceOfType(rbelPath, validationType, createValidationOptionsWithProfile(profileUrl));
    }

    private void tgrCurrentRequestAtIsValidFHIRRessourceOfType(String rbelPath, SupportedValidationModule validationType,
                                                               ValidationOptions validationOptions) {
        final var validationResult = validateRbelElementAt(rbelValidator.findElementInCurrentRequest(rbelPath), validationType, validationOptions);

        assertThat(validationResult.isValid())
                .withFailMessage(
                        () -> "request body is not a valid %s resource".formatted(validationType))
                .isTrue();
    }

    private static ValidationOptions createValidationOptionsWithProfile(String profile) {
        var validationOptions = ValidationOptions.getDefaults();
        validationOptions.setProfiles(List.of(profile));
        return validationOptions;
    }

    private void recordEvidencesOf(final ValidationResult validationResult) {
        validationResult.getValidationMessages().stream()
                .map(StaticFhirValidation::toEvidence)
                .forEach(evidenceRecorder::recordEvidence);
    }

    @NotNull
    private static Evidence toEvidence(final SingleValidationMessage it) {
        final Type type = switch (it.getSeverity()) {
            case INFORMATION -> Type.INFO;
            case WARNING -> Type.WARN;
            case ERROR -> Type.ERROR;
            case FATAL -> Type.FATAL;
        };

        return new Evidence(type, it.getMessage(), it);
    }


    public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(
            final SupportedValidationModule validationType) {
        tgrCurrentResponseAtIsValidFHIRRessourceOfType(BODY_RBEL_PATH, validationType);
    }

    public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(final String rbelPath,
                                                               final SupportedValidationModule validationType) {
        tgrCurrentResponseAtIsValidFHIRRessourceOfType(rbelPath, validationType, ValidationOptions.getDefaults());
    }

    public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(SupportedValidationModule validationType, String profileUrl) {
        tgrCurrentResponseAtIsValidFHIRRessourceOfType(BODY_RBEL_PATH, validationType, createValidationOptionsWithProfile(profileUrl));
    }

    public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(String rbelPath, SupportedValidationModule validationType, String profileUrl) {
        tgrCurrentResponseAtIsValidFHIRRessourceOfType(rbelPath, validationType, createValidationOptionsWithProfile(profileUrl));
    }

    private void tgrCurrentResponseAtIsValidFHIRRessourceOfType(String rbelPath, SupportedValidationModule validationType, ValidationOptions validationOptions) {
        final var validationResult = validateRbelElementAt(rbelValidator.findElementInCurrentResponse(rbelPath), validationType, validationOptions);

        assertThat(validationResult.isValid())
                .withFailMessage(
                        () -> "response body is not a valid %s resource".formatted(validationType))
                .isTrue();
    }

    @NotNull
    private ValidationResult validateRbelElementAt(RbelElement toValidate, SupportedValidationModule validationType, ValidationOptions validationOptions) {
        final var rawContentToValidate = toValidate.getRawStringContent();

        final var validator = getValidationModuleFor(validationType);
        final var validationResult = validator.validateString(rawContentToValidate, validationOptions);

        recordEvidencesOf(validationResult);
        return validationResult;
    }


    private ValidationModule getValidationModuleFor(final SupportedValidationModule validationType) {
        return validationModuleCache.computeIfAbsent(validationType,
                StaticFhirValidation::getNewValidationModule);
    }

    @SneakyThrows
    private static ValidationModule getNewValidationModule(
            final SupportedValidationModule validationType) {
        return new ValidationModuleFactory()
                .createValidationModule(validationType);
    }
}
