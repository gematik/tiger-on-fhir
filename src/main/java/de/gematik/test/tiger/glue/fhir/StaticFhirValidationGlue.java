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

import de.gematik.refv.SupportedValidationModule;
import de.gematik.test.tiger.fhir.validation.staticv.StaticFhirValidation;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

/**
 * Glue code for Static-FHIR validation.
 * <p>
 * It allows you to validate FHIR resources against the FHIR specification.
 */
@RequiredArgsConstructor
public class StaticFhirValidationGlue {

  private final StaticFhirValidation staticFhirValidation;

  public StaticFhirValidationGlue() {
    this(new StaticFhirValidation());
  }

  @ParameterType(".+")
  public SupportedValidationModule supportedValidationModule(final String enumName) {
    return SupportedValidationModule.fromString(enumName)
        .orElseGet(() -> SupportedValidationModule.valueOf(enumName));
  }

  @Then("TGR current request body is valid FHIR resource of type {supportedValidationModule}")
  public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(validationType);
  }

  @Then("TGR current request {string} is valid FHIR resource of type {supportedValidationModule}")
  public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(final String rbelPath,
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentRequestAtIsValidFHIRRessourceOfType(rbelPath, validationType);
  }

  @Then("TGR current response body is valid FHIR resource of type {supportedValidationModule}")
  public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(validationType);
  }

  @Then("TGR current response {string} is valid FHIR resource of type {supportedValidationModule}")
  public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(final String rbelPath,
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentResponseAtIsValidFHIRRessourceOfType(rbelPath, validationType);
  }
}
