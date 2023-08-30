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
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

/**
 * Glue code for Static-FHIR validation.
 * <p>
 * It allows you to validate FHIR resources against the FHIR specification.
 */
@SuppressWarnings("unused")
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

  @Then("FHIR current request body is a valid {supportedValidationModule} resource")
  @Then("TGR current request body is a valid {supportedValidationModule} resource")
  @Wenn("FHIR die aktuelle Anfrage im Body eine gültige {supportedValidationModule} Ressource enthält")
  @Dann("FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige {supportedValidationModule} Ressource")
  public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(
          final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(validationType);
  }

  @Then("TGR current request body is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Then("FHIR current request body is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Wenn("FHIR die aktuelle Anfrage im Body eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist enthält")
  @Dann("FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist")
  public void tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(
          final SupportedValidationModule validationType, String profileUrl) {
    staticFhirValidation.tgrCurrentRequestBodyAtIsValidFHIRRessourceOfType(validationType, profileUrl);
  }

  @Then("TGR current request {string} is a valid {supportedValidationModule} resource")
  @Then("FHIR current request at {string} is a valid {supportedValidationModule} resource")
  @Wenn("FHIR die aktuelle Anfrage im Knoten {string} eine gültige {supportedValidationModule} Ressource enthält")
  @Dann("FHIR prüfe die aktuelle Anfrage enthält im Knoten {string} eine gültige {supportedValidationModule} Ressource")
  public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(final String rbelPath,
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentRequestAtIsValidFHIRRessourceOfType(rbelPath, validationType);
  }

  @Then("TGR current request {string} is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Then("FHIR current request at {string} is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Wenn("FHIR die aktuelle Anfrage im Knoten {string} eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist enthält")
  @Dann("FHIR prüfe die aktuelle Anfrage enthält im Knoten {string} eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist")
  public void tgrCurrentRequestAtIsValidFHIRRessourceOfType(final String rbelPath,
                                                            final SupportedValidationModule validationType, String profileUrl) {
    staticFhirValidation.tgrCurrentRequestAtIsValidFHIRRessourceOfType(rbelPath, validationType, profileUrl);
  }



  @Then("TGR current response body is a valid {supportedValidationModule} resource")
  @Then("FHIR current response body is a valid {supportedValidationModule} resource")
  @Wenn("FHIR die aktuelle Antwort im Body eine gültige {supportedValidationModule} Ressource enthält")
  @Dann("FHIR prüfe die aktuelle Antwort enthält im Body eine gültige {supportedValidationModule} Ressource")
  public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(validationType);
  }

  @Then("TGR current response body is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Then("FHIR current response body is a valid {supportedValidationModule} resource and conforms to profile {string}")
  @Wenn("FHIR die aktuelle Antwort im Body eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist enthält")
  @Dann("FHIR prüfe die aktuelle Antwort enthält im Body eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist")
  public void tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(
          final SupportedValidationModule validationType, String profileUrl) {
    staticFhirValidation.tgrCurrentResponseBodyAtIsValidFHIRRessourceOfType(validationType, profileUrl);
  }

  @Then("TGR current response {string} is a valid FHIR {supportedValidationModule} resource")
  @Then("FHIR current response at {string} is a valid FHIR {supportedValidationModule} resource")
  @Wenn("FHIR die aktuelle Antwort im Knoten {string} eine gültige {supportedValidationModule} Ressource enthält")
  @Dann("FHIR prüfe die aktuelle Antwort enthält im Knoten {string} eine gültige {supportedValidationModule} Ressource")
  public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(final String rbelPath,
      final SupportedValidationModule validationType) {
    staticFhirValidation.tgrCurrentResponseAtIsValidFHIRRessourceOfType(rbelPath, validationType);
  }

  @Then("TGR current response {string} is a valid FHIR {supportedValidationModule} resource and conforms to profile {string}")
  @Then("FHIR current response at {string} is a valid FHIR {supportedValidationModule} resource and conforms to profile {string}")
  @Wenn("FHIR die aktuelle Antwort im Knoten {string} eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist enthält")
  @Dann("FHIR prüfe die aktuelle Antwort enthält im Knoten {string} eine gültige {supportedValidationModule} Ressource, die zum Profil {string} konform ist")
  public void tgrCurrentResponseAtIsValidFHIRRessourceOfType(final String rbelPath,
                                                             final SupportedValidationModule validationType, String profileUrl) {
    staticFhirValidation.tgrCurrentResponseAtIsValidFHIRRessourceOfType(rbelPath, validationType, profileUrl);
  }
}
