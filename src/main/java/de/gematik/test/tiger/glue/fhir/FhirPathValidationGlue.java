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

import de.gematik.test.tiger.fhir.validation.fhirpath.FhirPathValidation;
import de.gematik.test.tiger.fhir.validation.fhirpath.NetTracer;
import io.cucumber.core.plugin.report.EvidenceRecorderFactory;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Wenn;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Glue code for FHIRPath validation.
 * <p>
 * It allows you fo perform boolean FHIRPath expressions on requests and responses and to check if
 * they evaluate as expected.
 */
@SuppressWarnings("unused")
@RequiredArgsConstructor
public class FhirPathValidationGlue {

    private final FhirPathValidation fhirPathValidation;

    public FhirPathValidationGlue() {
        this(new FhirPathValidation(EvidenceRecorderFactory.getEvidenceRecorder(), new NetTracer()));
    }

    @Then("TGR current request body evaluates the FHIRPath {string}")
    @Then("FHIR current request body evaluates the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Anfrage im Body den FHIRPath {string} erfüllt")
    public void tgrCurrentRequestBodyEvaluatesTheFhirPath(final String fhirPath) {
        fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath);
    }

    @Then("TGR current request body evaluates the FHIRPath {string} with error message {string}")
    @Then("FHIR current request body evaluates the FHIRPath {string} with error message {string}")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body den FHIRPath {string} mit der Fehlermeldung {string}")
    @Wenn("FHIR die aktuelle Anfrage im Body den FHIRPath {string} mit der Fehlermeldung {string} erfüllt")
    public void tgrCurrentRequestBodyEvaluatesTheFhirPath(final String fhirPath, final String errorMessage) {
        fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPath(fhirPath, Optional.of(errorMessage));
    }

    /**
     *
     * @param rbelPath
     * @param fhirPath
     * @deprecated v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPath {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPath {string}")
    @Then("TGR current request {string} evaluates the FHIRPath {string}")
    public void deprecated01(final String rbelPath,
                             final String fhirPath) {
        tgrCurrentRequestEvaluatesTheFhirPath(rbelPath, fhirPath);
    }

    @Then("FHIR current request at {string} evaluates the FHIRPath {string}")
    @Dann("FHIR prüfe aktuelle Anfrage im Knoten {string} erfüllt den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Anfrage im Knoten {string} den FHIRPath {string} erfüllt")
    public void tgrCurrentRequestEvaluatesTheFhirPath(final String rbelPath,
        final String fhirPath) {
        fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPath(rbelPath, fhirPath);
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPath {string} with error message {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPath {string} with error message {string}")
    @Then("TGR current request {string} evaluates the FHIRPath {string} with error message {string}")
    public void deprecated02(final String rbelPath,
                             final String fhirPath, final String errorMessage) {
        tgrCurrentRequestEvaluatesTheFhirPath(rbelPath, fhirPath, errorMessage);
    }

    @Then("FHIR current request at {string} evaluates the FHIRPath {string} with error message {string}")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {string} den FHIRPath {string} mit der Fehlermeldung {string}")
    @Wenn("FHIR die aktuelle Anfrage im Knoten {string} den FHIRPath {string} mit der Fehlermeldung {string} erfüllt")
    public void tgrCurrentRequestEvaluatesTheFhirPath(final String rbelPath,
        final String fhirPath, final String errorMessage) {
        fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPath(rbelPath, fhirPath, Optional.of(errorMessage));
    }

    @Then("TGR current request body evaluates the FHIRPaths:")
    @Then("FHIR current request body evaluates the FHIRPaths:")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body die FHIRPath Ausdrücke:")
    @Wenn("FHIR die aktuelle Anfrage im Body die FHIRPath Ausdrücke erfüllt:")
    public void tgrCurrentRequestBodyEvaluatesTheFhirPaths(final String fhirPaths) {
        fhirPathValidation.tgrCurrentRequestBodyEvaluatesTheFhirPaths(fhirPaths);
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPaths:
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current request at {string} evaluates the FHIRPaths:")
    @Then("TGR current request {string} evaluates the FHIRPaths:")
    public void deprecated03(final String rbelPath,
                             final String fhirPaths) {
        tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPaths);
    }

    @Then("FHIR current request at {string} evaluates the FHIRPaths:")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {string} die FHIRPath Ausdrücke:")
    @Wenn("FHIR die aktuelle Anfrage im Knoten {string} die FHIRPath Ausdrücke erfüllt:")
    public void tgrCurrentRequestEvaluatesTheFhirPaths(final String rbelPath,
        final String fhirPaths) {
        fhirPathValidation.tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPaths);
    }

    @Then("TGR current response body evaluates the FHIRPath {string}")
    @Then("FHIR current response body evaluates the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Antwort im Body den FHIRPath {string} erfüllt")
    public void tgrCurrentResponseBodyEvaluatesTheFhirPath(final String fhirPath) {
        fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPath(fhirPath);
    }

    @Then("TGR current response body evaluates the FHIRPath {string} with error message {string}")
    @Then("FHIR current response body evaluates the FHIRPath {string} with error message {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body den FHIRPath {string} mit der Fehlermeldung {string}")
    @Wenn("FHIR die aktuelle Antwort im Body den FHIRPath {string} mit der Fehlermeldung {string} erfüllt")
    public void tgrCurrentResponseBodyEvaluatesTheFhirPath(final String fhirPath, final String errorMessage) {
        fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPath(fhirPath, Optional.of(errorMessage));
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPath {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPath {string}")
    @Then("TGR current response {string} evaluates the FHIRPaths {string}")
    public void deprecated04(final String rbelPath,
                             final String fhirPath) {
        tgrCurrentResponseEvaluatesTheFhirPath(rbelPath, fhirPath);
    }

    @Then("FHIR current response at {string} evaluates the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {string} den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Antwort im Knoten {string} den FHIRPath {string} erfüllt")
    public void tgrCurrentResponseEvaluatesTheFhirPath(final String rbelPath,
        final String fhirPath) {
        fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPath(rbelPath, fhirPath);
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPath {string} with error message {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPath {string} with error message {string}")
    @Then("TGR current response {string} evaluates the FHIRPath {string} with error message {string}")
    public void deprecated05(final String rbelPath,
                             final String fhirPath, final String errorMessage) {
        tgrCurrentResponseEvaluatesTheFhirPath(rbelPath, fhirPath, errorMessage);
    }

    @Then("FHIR current response at {string} evaluates the FHIRPath {string} with error message {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {string} den FHIRPath {string} mit der Fehlermeldung {string}")
    @Wenn("FHIR die aktuelle Antwort im Knoten {string} den FHIRPath {string} mit der Fehlermeldung {string} erfüllt")
    public void tgrCurrentResponseEvaluatesTheFhirPath(final String rbelPath,
        final String fhirPath, final String errorMessage) {
        fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPath(rbelPath, fhirPath, Optional.of(errorMessage));
    }

    @Then("TGR current response body evaluates the FHIRPaths:")
    @Then("FHIR current response body evaluates the FHIRPaths:")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body die FHIRPath Ausdrücke:")
    @Wenn("FHIR die aktuelle Antwort im Body die FHIRPath Ausdrücke erfüllt:")
    public void tgrCurrentResponseBodyEvaluatesTheFhirPaths(
        final String fhirPaths) {
        fhirPathValidation.tgrCurrentResponseBodyEvaluatesTheFhirPaths(fhirPaths);
    }


    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPaths:
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current response at {string} evaluates the FHIRPaths:")
    @Then("TGR current response {string} evaluates the FHIRPaths:")
    public void deprecated06(final String rbelPath,
                             final String fhirPaths) {
        tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPaths);
    }

    @Then("FHIR current response at {string} evaluates the FHIRPaths:")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {string} die FHIRPath Ausdrücke:")
    @Wenn("FHIR die aktuelle Antwort im Knoten {string} die FHIRPath Ausdrücke erfüllt:")
    public void tgrCurrentResponseEvaluatesTheFhirPaths(final String rbelPath,
        final String fhirPaths) {
        fhirPathValidation.tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPaths);
    }

    @Then("TGR current request body fails the FHIRPath {string}")
    @Then("FHIR current request body fails the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Body nicht den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Anfrage im Body nicht den FHIRPath {string} erfüllt")
    public void tgrCurrentRequestBodyFailesTheFhirPath(final String fhirPath) {
        fhirPathValidation.tgrCurrentRequestBodyFailesTheFhirPath(fhirPath);
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current request at {string} fails the FHIRPath {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current request at {string} fails the FHIRPath {string}")
    @Then("TGR current request {string} fails the FHIRPath {string}")
    public void deprecated07(final String rbelPath,
                             final String fhirPath) {
        tgrCurrentRequestFailsTheFhirPath(rbelPath, fhirPath);
    }

    @Then("FHIR current request at {string} fails the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Anfrage erfüllt im Knoten {string} nicht den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Anfrage im Knoten {string} nicht den FHIRPath {string} erfüllt")
    public void tgrCurrentRequestFailsTheFhirPath(final String rbelPath,
        final String fhirPath) {
        fhirPathValidation.tgrCurrentRequestFailsTheFhirPath(rbelPath, fhirPath);
    }

    @Then("TGR current response body fails the FHIRPath {string}")
    @Then("FHIR current response body fails the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Body nicht den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Antwort im Body nicht den FHIRPath {string} erfüllt")
    public void tgrCurrentResponseBodyFailsTheFhirPath(final String fhirPath) {
        fhirPathValidation.tgrCurrentResponseBodyFailsTheFhirPath(fhirPath);
    }

    /**
     *
     * @deprecated v1.1.0 Please replace with FHIR current response at {string} fails the FHIRPath {string}
     */
    @Deprecated(since = "v1.1.0 Please replace with FHIR current response at {string} fails the FHIRPath {string}")
    @Then("TGR current response {string} fails the FHIRPath {string}")
    public void deprecated08(final String rbelPath,
                             final String fhirPath) {
        tgrCurrentResponseFailsTheFhirPath(rbelPath, fhirPath);
    }

    @Then("FHIR current response at {string} fails the FHIRPath {string}")
    @Dann("FHIR prüfe die aktuelle Antwort erfüllt im Knoten {string} nicht den FHIRPath {string}")
    @Wenn("FHIR die aktuelle Antwort im Knoten {string} nicht den FHIRPath {string} erfüllt")
    public void tgrCurrentResponseFailsTheFhirPath(final String rbelPath,
        final String fhirPath) {
        fhirPathValidation.tgrCurrentResponseFailsTheFhirPath(rbelPath, fhirPath);
    }
}
