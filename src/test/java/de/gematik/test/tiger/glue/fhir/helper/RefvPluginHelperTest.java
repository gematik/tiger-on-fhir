/*
 *
 * Copyright 2023-2024 gematik GmbH
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

package de.gematik.test.tiger.glue.fhir.helper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import de.gematik.refv.Plugin;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class RefvPluginHelperTest {

  private final RefvPluginHelper pluginHelper = new RefvPluginHelper();

  @Test
  @SneakyThrows
  void testGetPlugin() {
    Plugin plugin = (Plugin) pluginHelper.getPlugin("minimal");

    assertThat(plugin).isNotNull();
    assertThat(plugin.getId()).isEqualTo("minimal");
  }

  @Test
  @SneakyThrows
  void testGetPluginThrowsException() {
    assertThatThrownBy(() -> pluginHelper.getPlugin("non-existent"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("non-existent");
  }
}
