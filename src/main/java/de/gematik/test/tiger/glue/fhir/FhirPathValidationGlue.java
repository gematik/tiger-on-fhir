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

import de.gematik.test.tiger.common.config.ConfigurationValuePrecedence;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import de.gematik.test.tiger.fhir.validation.fhirpath.FhirPathValidation;
import de.gematik.test.tiger.fhir.validation.fhirpath.NetTracer;
import io.cucumber.core.plugin.report.EvidenceRecorderFactory;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Glue code for FHIRPath validation.
 *
 * <p>It allows you fo perform boolean FHIRPath expressions on requests and responses and to check
 * if they evaluate as expected.
 */
@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
public class FhirPathValidationGlue {

  private final FhirPathValidation fhirPathValidation;

  public FhirPathValidationGlue() {
    this(new FhirPathValidation(EvidenceRecorderFactory.getEvidenceRecorder(), new NetTracer()));
  }

  @Then("FHIR current request body evaluates the FHIRPath {string}")
  @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath {string}")
  @Wenn("FHIR die aktuelle Anfrage im Body den FHIRPath {string} erfüllt")
  public void tgrCurrentRequestBodyEvaluatesTheFhirPath(final String fhirPath) {
    fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPath(
        TigerGlobalConfiguration.resolvePlaceholders(fhirPath));
  }

  @Then("FHIR current request body evaluates the FHIRPath {tigerResolvedString} with error message {tigerResolvedString}")
  @Dann(
      "FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath {tigerResolvedString} mit der Fehlermeldung"
          + " {tigerResolvedString}")
  @Wenn(
      "FHIR die aktuelle Anfrage im Body den FHIRPath {tigerResolvedString} mit der Fehlermeldung {tigerResolvedString}"
          + " erfüllt")
  public void tgrCurrentRequestBodyEvaluatesTheFhirPath(
      final String fhirPath, final String errorMessage) {
    fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPath(
        fhirPath, Optional.of(errorMessage));
  }

  @Then("FHIR current request at {string} evaluates the FHIRPath {string}")
  @Dann("FHIR prüfe aktuelle Anfrage im Knoten {string} erfüllt den FHIRPath {string}")
  @Wenn("FHIR die aktuelle Anfrage im Knoten {string} den FHIRPath {string} erfüllt")
  public void tgrCurrentRequestEvaluatesTheFhirPath(final String rbelPath, final String fhirPath) {
    fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPath(TigerGlobalConfiguration.resolvePlaceholders(rbelPath),
        TigerGlobalConfiguration.resolvePlaceholders(fhirPath));
  }

  @Then(
      "FHIR current request at {tigerResolvedString} evaluates the FHIRPath {tigerResolvedString} with error message"
          + " {tigerResolvedString}")
  @Dann(
      "FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {tigerResolvedString} den FHIRPath {tigerResolvedString} mit der"
          + " Fehlermeldung {tigerResolvedString}")
  @Wenn(
      "FHIR die aktuelle Anfrage im Knoten {tigerResolvedString} den FHIRPath {tigerResolvedString} mit der Fehlermeldung"
          + " {tigerResolvedString} erfüllt")
  public void tgrCurrentRequestEvaluatesTheFhirPath(
      final String rbelPath, final String fhirPath, final String errorMessage) {
    fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPath(
        rbelPath, fhirPath, Optional.of(errorMessage));
  }

  @Then("FHIR current request body evaluates the FHIRPaths:")
  @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body die FHIRPath Ausdrücke:")
  @Wenn("FHIR die aktuelle Anfrage im Body die FHIRPath Ausdrücke erfüllt:")
  public void tgrCurrentRequestBodyEvaluatesTheFhirPaths(final String fhirPaths) {
    fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPaths(fhirPaths);
  }

  @Then("FHIR current request at {tigerResolvedString} evaluates the FHIRPaths:")
  @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {tigerResolvedString} die FHIRPath Ausdrücke:")
  @Wenn("FHIR die aktuelle Anfrage im Knoten {tigerResolvedString} die FHIRPath Ausdrücke erfüllt:")
  public void tgrCurrentRequestEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths) {
    fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPaths);
  }

  @Then("FHIR current response body evaluates the FHIRPath {string}")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath {string}")
  @Wenn("FHIR die aktuelle Antwort im Body den FHIRPath {string} erfüllt")
  public void tgrCurrentResponseBodyEvaluatesTheFhirPath(final String fhirPath) {
    fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPath(
        TigerGlobalConfiguration.resolvePlaceholders(fhirPath));
  }

  @Then("FHIR current response body evaluates the FHIRPath {tigerResolvedString} with error message {tigerResolvedString}")
  @Dann(
      "FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath {tigerResolvedString} mit der Fehlermeldung"
          + " {tigerResolvedString}")
  @Wenn(
      "FHIR die aktuelle Antwort im Body den FHIRPath {tigerResolvedString} mit der Fehlermeldung {tigerResolvedString}"
          + " erfüllt")
  public void tgrCurrentResponseBodyEvaluatesTheFhirPath(
      final String fhirPath, final String errorMessage) {
    fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPath(
        fhirPath, Optional.of(errorMessage));
  }

  @Then("FHIR current response at {string} evaluates the FHIRPath {string}")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {string} den FHIRPath {string}")
  @Wenn("FHIR die aktuelle Antwort im Knoten {string} den FHIRPath {string} erfüllt")
  public void tgrCurrentResponseEvaluatesTheFhirPath(final String rbelPath, final String fhirPath) {
    fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPath(TigerGlobalConfiguration.resolvePlaceholders(rbelPath),
        TigerGlobalConfiguration.resolvePlaceholders(fhirPath));
  }

  @Then(
      "FHIR current response at {tigerResolvedString} evaluates the FHIRPath {tigerResolvedString} with error message"
          + " {tigerResolvedString}")
  @Dann(
      "FHIR prüfe die aktuelle Antwort erfüllt im Knoten {tigerResolvedString} den FHIRPath {tigerResolvedString} mit der"
          + " Fehlermeldung {tigerResolvedString}")
  @Wenn(
      "FHIR die aktuelle Antwort im Knoten {tigerResolvedString} den FHIRPath {tigerResolvedString} mit der Fehlermeldung"
          + " {tigerResolvedString} erfüllt")
  public void tgrCurrentResponseEvaluatesTheFhirPath(
      final String rbelPath, final String fhirPath, final String errorMessage) {
    fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPath(
        rbelPath, fhirPath, Optional.of(errorMessage));
  }

  @Then("FHIR current response body evaluates the FHIRPaths:")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body die FHIRPath Ausdrücke:")
  @Wenn("FHIR die aktuelle Antwort im Body die FHIRPath Ausdrücke erfüllt:")
  public void tgrCurrentResponseBodyEvaluatesTheFhirPaths(final String fhirPaths) {
    fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPaths(fhirPaths);
  }

  @Then("FHIR current response at {tigerResolvedString} evaluates the FHIRPaths:")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {tigerResolvedString} die FHIRPath Ausdrücke:")
  @Wenn("FHIR die aktuelle Antwort im Knoten {tigerResolvedString} die FHIRPath Ausdrücke erfüllt:")
  public void tgrCurrentResponseEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths) {
    fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPaths);
  }

  @Then("FHIR current request body fails the FHIRPath {tigerResolvedString}")
  @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body nicht den FHIRPath {tigerResolvedString}")
  @Wenn("FHIR die aktuelle Anfrage im Body nicht den FHIRPath {tigerResolvedString} erfüllt")
  public void tgrCurrentRequestBodyFailesTheFhirPath(final String fhirPath) {
    fhirPathValidation.tgrCurrentRequestBodyFailesTheFhirPath(fhirPath);
  }

  @Then("FHIR current request at {tigerResolvedString} fails the FHIRPath {tigerResolvedString}")
  @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {tigerResolvedString} nicht den FHIRPath {tigerResolvedString}")
  @Wenn("FHIR die aktuelle Anfrage im Knoten {tigerResolvedString} nicht den FHIRPath {tigerResolvedString} erfüllt")
  public void tgrCurrentRequestFailsTheFhirPath(final String rbelPath, final String fhirPath) {
    fhirPathValidation.tgrCurrentRequestFailsTheFhirPath(rbelPath, fhirPath);
  }

  @Then("FHIR current response body fails the FHIRPath {tigerResolvedString}")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body nicht den FHIRPath {tigerResolvedString}")
  @Wenn("FHIR die aktuelle Antwort im Body nicht den FHIRPath {tigerResolvedString} erfüllt")
  public void tgrCurrentResponseBodyFailsTheFhirPath(final String fhirPath) {
    fhirPathValidation.tgrCurrentResponseBodyFailsTheFhirPath(fhirPath);
  }

  @Then("FHIR current response at {tigerResolvedString} fails the FHIRPath {tigerResolvedString}")
  @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {tigerResolvedString} nicht den FHIRPath {tigerResolvedString}")
  @Wenn("FHIR die aktuelle Antwort im Knoten {tigerResolvedString} nicht den FHIRPath {tigerResolvedString} erfüllt")
  public void tgrCurrentResponseFailsTheFhirPath(final String rbelPath, final String fhirPath) {
    fhirPathValidation.tgrCurrentResponseFailsTheFhirPath(rbelPath, fhirPath);
  }

  @Then("FHIR evaluate FHIRPath {tigerResolvedString} on current response body and store first element as primitive value in variable {tigerResolvedString}")
  @Dann("FHIR prüfe die aktuelle Antwort den FHIRPath {tigerResolvedString} und speichere das erste Ergebnis als primitiven Wert in Variable {tigerResolvedString}")
  public void fhirEvaluateFHIRPathTigerResolvedStringOnCurrentResponseBodyAndStoreResultInVariableString(
      final String fhirPath, final String variable) {
    String value = fhirPathValidation.getFirstElementAsPrimitiveValueForFhirPath(fhirPath);
    if (value != null) {
      TigerGlobalConfiguration.putValue(variable, value, ConfigurationValuePrecedence.TEST_CONTEXT);
      log.info(String.format("Storing '%s' in variable '%s'", value, variable));
    }
  }
}
