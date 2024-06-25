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

import de.gematik.refv.Plugin;
import de.gematik.refv.SupportedValidationModule;
import de.gematik.refv.ValidationModuleFactory;
import de.gematik.refv.commons.validation.ValidationModule;
import de.gematik.test.tiger.fhir.validation.staticv.StaticFhirValidation;
import de.gematik.test.tiger.glue.fhir.helper.RefvPluginHelper;
import io.cucumber.java.ParameterType;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Glue code for Static-FHIR validation.
 *
 * <p>It allows you to validate FHIR resources against the FHIR specification.
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class StaticFhirValidationGlue {

  private final StaticFhirValidation staticFhirValidation;
  private final RefvPluginHelper pluginHelper = new RefvPluginHelper();
  private static final Map<String, ValidationModule> validationModuleCache = new ConcurrentHashMap<>();

  public StaticFhirValidationGlue() {
    this(new StaticFhirValidation());
  }

  @SneakyThrows
  @ParameterType(".+")
  public ValidationModule supportedValidationModule(final String validationModuleId) {
    String normalizedId = validationModuleId.toLowerCase();
    ValidationModule validationModule;

    // Check cache first
    if (validationModuleCache.containsKey(normalizedId)) {
      return validationModuleCache.get(normalizedId);
    }

    // Attempt to get SupportedValidationModule as ValidationModule
    Optional<SupportedValidationModule> supportedValidationModule = SupportedValidationModule.fromString(normalizedId);
    if (supportedValidationModule.isPresent()) {
      validationModule = new ValidationModuleFactory().createValidationModule(supportedValidationModule.get());
      validationModuleCache.put(normalizedId, validationModule);
      return validationModule;
    }

    // If not found, attempt to get a Plugin as ValidationModule
    try {
      Plugin plugin = pluginHelper.getPlugin(normalizedId);

      if (plugin == null) {
        throw new IllegalArgumentException("Invalid validation module id for both SupportedValidationModule and Plugin: " + validationModuleId);
      }

      validationModule = new ValidationModuleFactory().createValidationModuleFromPlugin(plugin);
      validationModuleCache.put(normalizedId, validationModule);
      return validationModule;
    } catch (Exception e) {
      throw new IllegalArgumentException("Something went wrong while trying to get the plugin with id: " + validationModuleId, e);
    }
  }

  @Then("FHIR current request body is a valid {supportedValidationModule} resource")
  @Wenn(
          "FHIR die aktuelle Anfrage im Body eine gültige {supportedValidationModule} Ressource"
                  + " enthält")
  @Dann(
          "FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige {supportedValidationModule}"
                  + " Ressource")
  public void tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(
          final ValidationModule validationModule) {
    staticFhirValidation.tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(validationModule);
  }

  @Then(
          "FHIR current request body is a valid {supportedValidationModule} resource and conforms to"
                  + " profile {tigerResolvedString}")
  @Wenn(
          "FHIR die aktuelle Anfrage im Body eine gültige {supportedValidationModule} Ressource, die"
                  + " zum Profil {tigerResolvedString} konform ist enthält")
  @Dann(
          "FHIR prüfe die aktuelle Anfrage enthält im Body eine gültige {supportedValidationModule}"
                  + " Ressource, die zum Profil {tigerResolvedString} konform ist")
  public void tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(
          final ValidationModule validationModule, String profileUrl) {
    staticFhirValidation.tgrCurrentRequestBodyAtIsValidFHIRResourceOfType(
            validationModule, profileUrl);
  }

  @Then("FHIR current request at {tigerResolvedString} is a valid {supportedValidationModule} resource")
  @Wenn(
          "FHIR die aktuelle Anfrage im Knoten {tigerResolvedString} eine gültige {supportedValidationModule}"
                  + " Ressource enthält")
  @Dann(
          "FHIR prüfe die aktuelle Anfrage enthält im Knoten {tigerResolvedString} eine gültige"
                  + " {supportedValidationModule} Ressource")
  public void tgrCurrentRequestAtIsValidFHIRResourceOfType(
          final String rbelPath, final ValidationModule validationModule) {
    staticFhirValidation.tgrCurrentRequestAtIsValidFHIRResourceOfType(rbelPath, validationModule);
  }

  @Then(
          "FHIR current request at {tigerResolvedString} is a valid {supportedValidationModule} resource and"
                  + " conforms to profile {tigerResolvedString}")
  @Wenn(
          "FHIR die aktuelle Anfrage im Knoten {tigerResolvedString} eine gültige {supportedValidationModule}"
                  + " Ressource, die zum Profil {tigerResolvedString} konform ist enthält")
  @Dann(
          "FHIR prüfe die aktuelle Anfrage enthält im Knoten {tigerResolvedString} eine gültige"
                  + " {supportedValidationModule} Ressource, die zum Profil {tigerResolvedString} konform ist")
  public void tgrCurrentRequestAtIsValidFHIRResourceOfType(
          final String rbelPath, final ValidationModule validationModule, String profileUrl) {
    staticFhirValidation.tgrCurrentRequestAtIsValidFHIRResourceOfType(
            rbelPath, validationModule, profileUrl);
  }

  @Then("FHIR current response body is a valid {supportedValidationModule} resource")
  @Wenn(
          "FHIR die aktuelle Antwort im Body eine gültige {supportedValidationModule} Ressource"
                  + " enthält")
  @Dann(
          "FHIR prüfe die aktuelle Antwort enthält im Body eine gültige {supportedValidationModule}"
                  + " Ressource")
  public void tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(
          final ValidationModule validationModule) {
    staticFhirValidation.tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(validationModule);
  }

  @Then(
          "FHIR current response body is a valid {supportedValidationModule} resource and conforms to"
                  + " profile {tigerResolvedString}")
  @Wenn(
          "FHIR die aktuelle Antwort im Body eine gültige {supportedValidationModule} Ressource, die"
                  + " zum Profil {tigerResolvedString} konform ist enthält")
  @Dann(
          "FHIR prüfe die aktuelle Antwort enthält im Body eine gültige {supportedValidationModule}"
                  + " Ressource, die zum Profil {tigerResolvedString} konform ist")
  public void tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(
          final ValidationModule validationModule, String profileUrl) {
    staticFhirValidation.tgrCurrentResponseBodyAtIsValidFHIRResourceOfType(
            validationModule, profileUrl);
  }

  @Then("FHIR current response at {tigerResolvedString} is a valid FHIR {supportedValidationModule} resource")
  @Wenn(
          "FHIR die aktuelle Antwort im Knoten {tigerResolvedString} eine gültige {supportedValidationModule}"
                  + " Ressource enthält")
  @Dann(
          "FHIR prüfe die aktuelle Antwort enthält im Knoten {tigerResolvedString} eine gültige"
                  + " {supportedValidationModule} Ressource")
  public void tgrCurrentResponseAtIsValidFHIRResourceOfType(
          final String rbelPath, final ValidationModule validationModule) {
    staticFhirValidation.tgrCurrentResponseAtIsValidFHIRResourceOfType(rbelPath, validationModule);
  }

  @Then(
          "FHIR current response at {tigerResolvedString} is a valid FHIR {supportedValidationModule} resource and"
                  + " conforms to profile {tigerResolvedString}")
  @Wenn(
          "FHIR die aktuelle Antwort im Knoten {tigerResolvedString} eine gültige {supportedValidationModule}"
                  + " Ressource, die zum Profil {tigerResolvedString} konform ist enthält")
  @Dann(
          "FHIR prüfe die aktuelle Antwort enthält im Knoten {tigerResolvedString} eine gültige"
                  + " {supportedValidationModule} Ressource, die zum Profil {tigerResolvedString} konform ist")
  public void tgrCurrentResponseAtIsValidFHIRResourceOfType(
          final String rbelPath, final ValidationModule validationModule, String profileUrl) {
    staticFhirValidation.tgrCurrentResponseAtIsValidFHIRResourceOfType(
            rbelPath, validationModule, profileUrl);
  }
}