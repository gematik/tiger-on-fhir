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

package de.gematik.test.tiger.glue.fhir;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import de.gematik.refv.commons.validation.ValidationModule;
import de.gematik.test.tiger.LocalProxyRbelMessageListener;
import de.gematik.test.tiger.fhir.validation.staticv.StaticFhirValidation;
import de.gematik.test.tiger.glue.RBelValidatorGlue;
import de.gematik.test.tiger.lib.TigerDirector;
import de.gematik.test.tiger.lib.rbel.RbelMessageRetriever;
import de.gematik.test.tiger.proxy.TigerProxy;
import de.gematik.test.tiger.testenvmgr.TigerTestEnvMgr;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class StaticFhirValidationGlueTest {

  private StaticFhirValidationGlue staticFhirValidationGlue;
  private final TigerTestEnvMgr tigerTestEnvMgr = mock(TigerTestEnvMgr.class);
  private final TigerProxy tigerProxy = mock(TigerProxy.class);

  @BeforeEach
  void setUp() {
    try (MockedStatic<TigerDirector> tigerDirectorMockedStatic =
        Mockito.mockStatic(TigerDirector.class)) {
      tigerDirectorMockedStatic.when(TigerDirector::getTigerTestEnvMgr).thenReturn(tigerTestEnvMgr);
      staticFhirValidationGlue =
          new StaticFhirValidationGlue(
              new StaticFhirValidation(
                  new RBelValidatorGlue(
                      new RbelMessageRetriever(
                          tigerTestEnvMgr, tigerProxy, new LocalProxyRbelMessageListener()))));
    }
  }

  @Test
  void testSupportedValidationModuleWithSupportedValidationModule() {
    String validationModuleId = "erp";
    ValidationModule validationModule =
        staticFhirValidationGlue.supportedValidationModule(validationModuleId);

    assertThat(validationModule).isNotNull();
    assertThat(validationModule.getId()).isEqualTo(validationModuleId);
  }

  @Test
  void testSupportedValidationModuleWithPlugin() {
    String validationModuleId = "minimal";
    ValidationModule validationModule =
        staticFhirValidationGlue.supportedValidationModule(validationModuleId);

    assertThat(validationModule).isNotNull();
    assertThat(validationModule.getId()).isEqualTo(validationModuleId);
  }

  @Test
  void testSupportedValidationModuleThrowsIllegalArgumentException() {
    String validationModuleId = "non-existent";
    assertThatThrownBy(() -> staticFhirValidationGlue.supportedValidationModule(validationModuleId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining(
            "Something went wrong while trying to get the plugin with id: " + validationModuleId);
  }
}
