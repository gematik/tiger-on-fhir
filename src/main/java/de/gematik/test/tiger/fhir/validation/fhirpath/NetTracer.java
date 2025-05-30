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

package de.gematik.test.tiger.fhir.validation.fhirpath;

import de.gematik.rbellogger.data.RbelElement;
import de.gematik.test.tiger.glue.RBelValidatorGlue;
import java.util.Optional;

/** Simplified access to traced requests and responses. */
public class NetTracer {

  private final RBelValidatorGlue rBelValidatorGlue = new RBelValidatorGlue();

  public Optional<String> getCurrentRequestsRawStringByRbelPath(final String rbelPath) {
    return Optional.ofNullable(
            rBelValidatorGlue.getRbelMessageRetriever().findElementInCurrentRequest(rbelPath))
        .map(RbelElement::getRawStringContent);
  }

  public Optional<String> getCurrentResponseRawStringByRbelPath(final String rbelPath) {
    return Optional.ofNullable(
            rBelValidatorGlue.getRbelMessageRetriever().findElementInCurrentResponse(rbelPath))
        .map(RbelElement::getRawStringContent);
  }
}
