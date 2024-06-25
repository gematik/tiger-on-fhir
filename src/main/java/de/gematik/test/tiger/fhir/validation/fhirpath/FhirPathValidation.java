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

package de.gematik.test.tiger.fhir.validation.fhirpath;

import static de.gematik.test.tiger.common.config.TigerGlobalConfiguration.resolvePlaceholders;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.IParser;
import de.gematik.test.tiger.common.config.TigerGlobalConfiguration;
import io.cucumber.core.plugin.report.Evidence;
import io.cucumber.core.plugin.report.Evidence.Type;
import io.cucumber.core.plugin.report.EvidenceRecorder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.SoftAssertions;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.jetbrains.annotations.NotNull;

/**
 * FHIRPath validation.
 *
 * <p>It allows you fo perform boolean FHIRPath expressions on requests and responses and to check
 * if they evaluate as expected.
 */
@RequiredArgsConstructor
public class FhirPathValidation {

  private static final FhirContext ctx = FhirContext.forR4();
  private static final FHIRPathEngine fhirPathEngine =
      new FHIRPathEngine(new HapiWorkerContext(ctx, new DefaultProfileValidationSupport(ctx)));

  public static final String RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER = "$.header.Content-Type";
  public static final String RBEL_SELECTOR_FOR_BODY = "$.body";

  private final EvidenceRecorder evidenceRecorder;
  private final NetTracer netTracer;

  public void tgrCurrentRequestBodyEvaluatesTheFhirPath(
      final String fhirPath, final Optional<String> errorMessage) {
    tgrCurrentRequestEvaluatesTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath, errorMessage);
  }

  public void tgrCurrentRequestBodyEvaluatesTheFhirPath(final String fhirPath) {
    tgrCurrentRequestEvaluatesTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath, Optional.empty());
  }

  public void tgrCurrentRequestEvaluatesTheFhirPath(final String rbelPath, final String fhirPath) {
    tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPath, Optional.empty());
  }

  public void tgrCurrentRequestEvaluatesTheFhirPath(
      final String rbelPath, final String fhirPath, final Optional<String> errorMessage) {
    tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPath, errorMessage);
  }

  public void tgrCurrentRequestBodyEvaluatesTheFhirPaths(final String fhirPaths) {
    tgrCurrentRequestEvaluatesTheFhirPaths(RBEL_SELECTOR_FOR_BODY, fhirPaths, Optional.empty());
  }

  public void tgrCurrentRequestEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths) {
    tgrCurrentRequestEvaluatesTheFhirPaths(rbelPath, fhirPaths, Optional.empty());
  }

  public void tgrCurrentRequestEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths, final Optional<String> errorMessage) {
    final Optional<String> fhirRessource = findElementInCurrentRequest(rbelPath);

    if (fhirRessource.isEmpty()) {
      return;
    }

    final IBaseResource ressource = parseRequestByContentType(fhirRessource.get());

    assertSoftly(
        softAsserter ->
            toCleanFhirPath(fhirPaths)
                .map(TigerGlobalConfiguration::resolvePlaceholders)
                .forEach(
                    fhirPath -> evaluate(fhirPath, (Base) ressource, softAsserter, errorMessage)));
  }

  @NotNull
  private static Stream<String> toCleanFhirPath(final String fhirPaths) {
    return fhirPaths.lines().filter(it -> !it.isBlank()).map(String::trim);
  }

  public void tgrCurrentResponseBodyEvaluatesTheFhirPath(final String fhirPath) {
    tgrCurrentResponseEvaluatesTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath, Optional.empty());
  }

  public void tgrCurrentResponseBodyEvaluatesTheFhirPath(
      final String fhirPath, final Optional<String> errorMessage) {
    tgrCurrentResponseEvaluatesTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath, errorMessage);
  }

  public void tgrCurrentResponseEvaluatesTheFhirPath(final String rbelPath, final String fhirPath) {
    tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPath, Optional.empty());
  }

  public void tgrCurrentResponseEvaluatesTheFhirPath(
      final String rbelPath, final String fhirPath, final Optional<String> errorMessage) {
    tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPath, errorMessage);
  }

  public void tgrCurrentResponseBodyEvaluatesTheFhirPaths(final String fhirPaths) {
    tgrCurrentResponseEvaluatesTheFhirPaths(RBEL_SELECTOR_FOR_BODY, fhirPaths, Optional.empty());
  }

  public void tgrCurrentResponseEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths) {
    tgrCurrentResponseEvaluatesTheFhirPaths(rbelPath, fhirPaths, Optional.empty());
  }

  public void tgrCurrentResponseEvaluatesTheFhirPaths(
      final String rbelPath, final String fhirPaths, final Optional<String> errorMessage) {
    final Optional<String> fhirRessource = findElementInCurrentResponse(rbelPath);
    if (fhirRessource.isEmpty()) {
      return;
    }

    final IBaseResource ressource = parseResponseByContentType(fhirRessource.get());

    assertSoftly(
        softAsserter ->
            toCleanFhirPath(fhirPaths)
                .map(TigerGlobalConfiguration::resolvePlaceholders)
                .forEach(
                    fhirPath -> evaluate(fhirPath, (Base) ressource, softAsserter, errorMessage)));
  }

  public void tgrCurrentRequestBodyFailesTheFhirPath(final String fhirPath) {
    tgrCurrentRequestFailsTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath);
  }

  public void tgrCurrentRequestFailsTheFhirPath(final String rbelPath, final String fhirPath) {
    final Optional<String> fhirRessource = findElementInCurrentRequest(rbelPath);
    if (fhirRessource.isEmpty()) {
      return;
    }

    final IBaseResource ressource = parseRequestByContentType(fhirRessource.get());

    assertSoftly(soft -> evaluateFail(resolvePlaceholders(fhirPath), (Base) ressource, soft));
  }

  public void tgrCurrentResponseBodyFailsTheFhirPath(final String fhirPath) {
    tgrCurrentResponseFailsTheFhirPath(RBEL_SELECTOR_FOR_BODY, fhirPath);
  }

  public void tgrCurrentResponseFailsTheFhirPath(final String rbelPath, final String fhirPath) {
    final Optional<String> fhirResource = findElementInCurrentResponse(rbelPath);
    if (fhirResource.isEmpty()) {
      return;
    }

    final IBaseResource ressource = parseResponseByContentType(fhirResource.get());

    assertSoftly(soft -> evaluateFail(resolvePlaceholders(fhirPath), (Base) ressource, soft));
  }

  private void evaluate(final String fhirPath, EvaluateOptions options) {
    final List<Base> evaluationResult;
    try {
      evaluationResult = fhirPathEngine.evaluate(options.ressource, fhirPath);
    } catch (final FHIRException e) {
      evidenceRecorder.recordEvidence(new Evidence(Type.FATAL, "invalid FHIRPath: " + fhirPath, e));

      Optional.ofNullable(options.errorMessage)
          .ifPresentOrElse(
              options.softAsserter::fail,
              () -> options.softAsserter.fail("invalid FHIRPath: " + fhirPath, e));
      return;
    }

    if (verifyEmptyResult(fhirPath, evaluationResult, options.softAsserter)) {
      return;
    }

    if (verifyNonBooleanResult(evaluationResult, fhirPath, options.softAsserter)) { // Test
      return;
    }

    final long numberOfTargetValues =
        options.expectedOutcome
            ? numberOfTrueValues(evaluationResult)
            : numberOfFalseValues(evaluationResult);
    final String targetValueString = options.expectedOutcome ? "true" : "false";

    if (numberOfTargetValues > 0) {
      evidenceRecorder.recordEvidence(
          new Evidence(
              Type.ERROR,
              numberOfTargetValues
                  + " value(s) in the evaluation results are "
                  + targetValueString
                  + " for: "
                  + fhirPath,
              evaluationResult));
      options.softAsserter.fail(
          Optional.ofNullable(options.errorMessage)
              .orElseGet(
                  () ->
                      numberOfTargetValues
                          + " value(s) in the evaluation results are "
                          + targetValueString
                          + " for: "
                          + fhirPath));
      return;
    }

    evidenceRecorder.recordEvidence(new Evidence(Type.INFO, fhirPath, evaluationResult));
  }

  private void evaluate(
      final String fhirPath,
      final Base ressource,
      final SoftAssertions softly,
      final Optional<String> errorMessage) {
    evaluate(
        fhirPath,
        EvaluateOptions.builder()
            .ressource(ressource)
            .softAsserter(softly)
            .errorMessage(errorMessage.orElse(null))
            .expectedOutcome(false)
            .build());
  }

  private void evaluateFail(
      final String fhirPath, final Base ressource, final SoftAssertions softly) {
    evaluate(
        fhirPath,
        EvaluateOptions.builder()
            .ressource(ressource)
            .softAsserter(softly)
            .expectedOutcome(true)
            .build());
  }

  private boolean verifyNonBooleanResult(
      final List<Base> evaluationResult, final String fhirPath, final SoftAssertions softly) {
    final var numberOfNonBooleans = numberOfNonBooleanResults(evaluationResult);
    if (numberOfNonBooleans > 0) {
      evidenceRecorder.recordEvidence(
          new Evidence(
              Type.ERROR,
              numberOfNonBooleans + " non boolean results for: " + fhirPath,
              evaluationResult));
      softly.fail(numberOfNonBooleans + " non boolean results for: " + fhirPath);
      return true;
    }
    return false;
  }

  private boolean verifyEmptyResult(
      final String fhirPath, final List<Base> evaluationResult, final SoftAssertions softly) {
    if (evaluationResult.isEmpty()) {
      evidenceRecorder.recordEvidence(
          new Evidence(Type.ERROR, "Empty results for: " + fhirPath, evaluationResult));
      softly.fail("Empty results for: " + fhirPath);
      return true;
    }
    return false;
  }

  private static long numberOfFalseValues(final List<Base> evaluationResult) {
    return evaluationResult.stream().filter(it -> !it.castToBoolean(it).booleanValue()).count();
  }

  private static long numberOfTrueValues(final List<Base> evaluationResult) {
    return evaluationResult.stream().filter(it -> it.castToBoolean(it).booleanValue()).count();
  }

  private static long numberOfNonBooleanResults(final List<Base> evaluationResult) {
    return evaluationResult.stream().filter(it -> !it.isBooleanPrimitive()).count();
  }

  @SuppressWarnings("java:S2259")
  private IBaseResource parseRequestByContentType(final String fhirResource) {
    return getParserByRequestContentType().parseResource(fhirResource);
  }

  private Optional<String> findElementInCurrentRequest(final String rbelPath) {
    final Optional<String> element = netTracer.getCurrentRequestsRawStringByRbelPath(rbelPath);

    if (element.isEmpty()) {
      evidenceRecorder.recordEvidence(
          new Evidence(Type.FATAL, "no element found with rbel at request"));
      fail("no element found with rbel at request");
    }
    return element;
  }

  @SuppressWarnings("java:S2259")
  private IBaseResource parseResponseByContentType(final String fhirResource) {
    return getParserByResponseContentType().parseResource(fhirResource);
  }

  private Optional<String> findElementInCurrentResponse(final String rbelPath) {
    var element = netTracer.getCurrentResponseRawStringByRbelPath(rbelPath);

    if (element.isEmpty()) {
      evidenceRecorder.recordEvidence(
          new Evidence(Type.FATAL, "no element found with rbel at response"));
      fail("no element found with rbel at response");
    }

    return element;
  }

  private IParser getParserByResponseContentType() {
    final Optional<IParser> parser =
        getResponseContentType().flatMap(this::getParserForContentTypeHeader);

    if (parser.isEmpty()) {
      evidenceRecorder.recordEvidence(new Evidence(Type.ERROR, "no Content-Type Header set"));
      fail("no Content-Type Header set for Response");
      return null; // unreachable
    }

    return parser.get();
  }

  private IParser getParserByRequestContentType() {
    final Optional<IParser> parser =
        getRequestContentType().flatMap(this::getParserForContentTypeHeader);

    if (parser.isEmpty()) {
      evidenceRecorder.recordEvidence(new Evidence(Type.ERROR, "no Content-Type Header set"));
      fail("no Content-Type Header set for Request");
      return null; // unreachable
    }

    return parser.get();
  }

  @NotNull
  private Optional<String> getRequestContentType() {
    return netTracer.getCurrentRequestsRawStringByRbelPath(RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER);
  }

  @NotNull
  private Optional<String> getResponseContentType() {
    return netTracer.getCurrentResponseRawStringByRbelPath(RBEL_SELECTOR_FOR_CONTENT_TYPE_HEADER);
  }

  private Optional<IParser> getParserForContentTypeHeader(final String contantType) {
    final String lowerCaseContentType = contantType.toLowerCase();
    if (lowerCaseContentType.contains("xml")) {
      return Optional.of(ctx.newXmlParser());
    } else {
      return Optional.of(ctx.newJsonParser());
    }
  }

  @Builder
  private static class EvaluateOptions {

    private final Base ressource;
    private final SoftAssertions softAsserter;
    private final boolean expectedOutcome;
    private final String errorMessage;
  }

  public String getFirstElementAsPrimitiveValueForFhirPath(final String fhirPath) {
    final Optional<String> fhirResource = findElementInCurrentRequest(RBEL_SELECTOR_FOR_BODY);

    if (fhirResource.isEmpty()) {
      return null;
    }

    final IBaseResource resource = parseRequestByContentType(fhirResource.get());
    String msg;
    try {
      final List<Base> evaluationResult = fhirPathEngine.evaluate((Base) resource, fhirPath);
      if (evaluationResult != null && !evaluationResult.isEmpty()) {
        if (evaluationResult.get(0).isPrimitive()) {
          return evaluationResult.get(0).primitiveValue();
        } else {
          evidenceRecorder.recordEvidence(
              new Evidence(Type.ERROR, "result is not a primitive type for FHIRPath: " + fhirPath));
          msg = "result is not a primitive type for FHIRPath: " + fhirPath;
        }
      } else {
        evidenceRecorder.recordEvidence(new Evidence(Type.ERROR, "result is null or empty for FHIRPath: " + fhirPath));
        msg = "result is null or empty for FHIRPath: " + fhirPath;
      }
    } catch (final FHIRException e) {
      evidenceRecorder.recordEvidence(new Evidence(Type.FATAL, "invalid FHIRPath: " + fhirPath, e));
      msg = "invalid FHIRPath: " + fhirPath;
    }
    fail(msg);
    return null;
  }
}
