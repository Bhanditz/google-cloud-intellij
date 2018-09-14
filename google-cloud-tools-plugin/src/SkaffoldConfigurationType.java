/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.cloud.tools.intellij.GoogleCloudCoreIcons;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import javax.swing.Icon;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class SkaffoldConfigurationType implements ConfigurationType {

  private static final String ID = "gct-skaffold-run-config";

  private SkaffoldRunConfigurationFactory skaffoldRunConfigurationFactory =
      new SkaffoldRunConfigurationFactory(this);
  private SkaffoldDevConfigurationFactory skaffoldDevConfigurationFactory =
      new SkaffoldDevConfigurationFactory(this);

  @Nls
  @Override
  public String getDisplayName() {
    return "Skaffold";
  }

  @Nls
  @Override
  public String getConfigurationTypeDescription() {
    return "Skaffold Run";
  }

  @Override
  public Icon getIcon() {
    return GoogleCloudCoreIcons.CLOUD;
  }

  @NotNull
  @Override
  public String getId() {
    return ID;
  }

  @Override
  public ConfigurationFactory[] getConfigurationFactories() {
    return new ConfigurationFactory[] {skaffoldRunConfigurationFactory, skaffoldDevConfigurationFactory};
  }

  private static class SkaffoldRunConfigurationFactory extends ConfigurationFactory {

    SkaffoldRunConfigurationFactory(@NotNull ConfigurationType type) {
      super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      return new SkaffoldRunConfiguration(project, this, "Skaffold Single Run");
    }

    @Nls
    @Override
    public String getName() {
      return "Skaffold Single Run";
    }
  }

  private static class SkaffoldDevConfigurationFactory extends ConfigurationFactory {

    SkaffoldDevConfigurationFactory(@NotNull ConfigurationType type) {
      super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      return new SkaffoldRunConfiguration(project, this, "Skaffold Continuous");
    }

    @Nls
    @Override
    public String getName() {
      return "Skaffold Continuous";
    }
  }
}
