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

package de.gematik.test.tiger.glue.fhir.validation.plugins;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.gematik.test.tiger.fhir.validation.plugins.PluginLoader;
import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class PluginLoaderTest {

  private static final String PLUGIN_PATH =
      Paths.get("").toAbsolutePath() + File.separator + "refv-plugins";

  @Test
  void testLoadPlugins() {
    PluginLoader pluginLoader = new PluginLoader();

    var plugins = pluginLoader.loadPlugins(PLUGIN_PATH);

    assertFalse(plugins.isEmpty());
    assertTrue(plugins.containsKey("minimal"));
    assertEquals(1, plugins.keySet().size(), "More plugins were loaded than expected");
  }

  @Test
  void testLoadPluginsThrowsIllegalArgumentException() {
    PluginLoader pluginLoader = new PluginLoader();

    assertThatThrownBy(() -> pluginLoader.loadPlugins("nonexistent/path/"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Plugins directory is missing. No such file or directory");
  }

  @Test
  void testLoadPluginsReturnsEmpty() {
    PluginLoader pluginLoader = new PluginLoader();

    var plugins = pluginLoader.loadPlugins("src/test/resources/de/");

    assertTrue(plugins.isEmpty());
  }

  @Test
  void testLoadPluginsNoConfigThrowsException() {
    PluginLoader pluginLoader = new PluginLoader();

    assertThatThrownBy(() -> pluginLoader.loadPlugins("src/test/resources/plugins/"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("No config file found");
  }
}
