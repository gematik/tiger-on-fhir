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

import static org.assertj.core.api.Assertions.assertThat;
import ca.uhn.fhir.validation.SingleValidationMessage;
import de.gematik.refv.SupportedValidationModule;
import de.gematik.refv.ValidationModuleFactory;
import de.gematik.refv.commons.validation.ValidationModule;
import de.gematik.refv.commons.validation.ValidationResult;
import de.gematik.test.tiger.lib.rbel.RbelMessageValidator;
import io.cucumber.core.plugin.report.Evidence;
import io.cucumber.core.plugin.report.Evidence.Type;
import io.cucumber.core.plugin.report.EvidenceRecorder;
import io.cucumber.core.plugin.report.EvidenceRecorderFactory;
import java.util.EnumMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

/**
 * Glue code for Static-FHIR validation.
 * <p>
 * It allows you to validate FHIR resources against the FHIR specification.
 */
public class StaticFhirValidation {

    private final Map<SupportedValidationModule, ValidationModule> validationModuleCache =
        new EnumMap<>(SupportedValidationModule.class);

    private static final RbelMessageValidator rbelValidator = RbelMessageValidator.instance;

    private final EvidenceRecorder evidenceRecorder = EvidenceRecorderFactory.getEvidenceRecorder();

    public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(
        final SupportedValidationModule validationType) {
        tgrCurrentRequestAtIsValidFHIRRessourceOfType("$.body", validationType);
    }


    public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(final String rbelPath,
        final SupportedValidationModule validationType) {
        final var rbelResponseBody = rbelValidator.findElementInCurrentRequest(rbelPath);
        final var responseBody = rbelResponseBody.getRawStringContent();

        final var validator = getValidationModuleFor(validationType);
        final var validationResult = validator.validateString(responseBody);

        recordEvidencesOf(validationResult);

        assertThat(validationResult.isValid())
            .withFailMessage(
                () -> "response body is not a valid FHIR resource of type " + validationType)
            .isTrue();
    }

    private void recordEvidencesOf(final ValidationResult validationResult) {
        validationResult.getValidationMessages().stream()
            .map(StaticFhirValidation::toEvidence)
            .forEach(evidenceRecorder::recordEvidence);
    }

    @NotNull
    private static Evidence toEvidence(final SingleValidationMessage it) {
        final Type type;
        switch (it.getSeverity()) {
            case INFORMATION:
                type = Type.INFO;
                break;
            case WARNING:
                type = Type.WARN;
                break;
            case ERROR:
                type = Type.ERROR;
                break;
            case FATAL:
                type = Type.FATAL;
                break;
            default:
                throw new UnsupportedOperationException(
                    "Sorry, we forgot to implement the severity " + it.getSeverity());
        }

        return new Evidence(type, it.getMessage(), it);
    }


    public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(
        final SupportedValidationModule validationType) {
        tgrCurrentResponseAtIsValidFHIRRessourceOfType("$.body", validationType);
    }


    public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(final String rbelPath,
        final SupportedValidationModule validationType) {
        final var rbelResponseBody = rbelValidator.findElementInCurrentResponse(rbelPath);
        final var responseBody = rbelResponseBody.getRawStringContent();

        final var validator = getValidationModuleFor(validationType);
        final var validationResult = validator.validateString(responseBody);

        recordEvidencesOf(validationResult);

        assertThat(validationResult.isValid())
            .withFailMessage(
                () -> "response body is not a valid FHIR resource of type " + validationType)
            .isTrue();
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
