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

package de.gematik.test.tiger.glue.fhir.helper;

import de.gematik.refv.Plugin;
import de.gematik.refv.SupportedValidationModule;
import de.gematik.test.tiger.fhir.validation.plugins.PluginLoader;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class RefvPluginHelper {

  private static final String PLUGINS_DIR = "refv-plugins";
  private final PluginLoader pluginLoader = new PluginLoader();

  public Plugin getPlugin(String validationModuleId) {
    String pluginsDirectory = getPluginsDirectory();
    Plugin plugin = null;

    if (new File(pluginsDirectory).exists()) {
      var plugins = pluginLoader.loadPlugins(pluginsDirectory);
      plugin = plugins.get(validationModuleId);
      if (plugin == null) {
        var supportedValidationModules =
            Stream.concat(
                    plugins.keySet().stream(), Arrays.stream(SupportedValidationModule.values()))
                .map(Object::toString)
                .toList();
        throw new IllegalArgumentException(
            String.format(
                "Validation module [%s] unsupported. Supported validation modules: %s",
                validationModuleId, supportedValidationModules));
      }
    }

    return plugin;
  }

  private String getPluginsDirectory() {
    String rootDirectory = Paths.get("").toAbsolutePath().toString(); // get the root directory
    return rootDirectory + File.separator + PLUGINS_DIR;
  }
}
