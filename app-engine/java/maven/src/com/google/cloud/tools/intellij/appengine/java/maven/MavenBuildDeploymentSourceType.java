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

package com.google.cloud.tools.intellij.appengine.java.maven;

import com.google.cloud.tools.intellij.appengine.java.cloud.BuildDeploymentSourceType;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.intellij.execution.BeforeRunTask;
import com.intellij.execution.RunManagerEx;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModulePointerManager;
import com.intellij.openapi.project.Project;
import com.intellij.remoteServer.configuration.deployment.ModuleDeploymentSource;
import java.util.Collection;
import java.util.List;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.maven.project.MavenProject;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.tasks.MavenBeforeRunTask;
import org.jetbrains.idea.maven.tasks.MavenBeforeRunTasksProvider;

/** A Maven build deployment source type providing an auto configured pre-deploy build step. */
public class MavenBuildDeploymentSourceType extends BuildDeploymentSourceType {

  private static final String MAVEN_TASK_PACKAGE = "package";
  private static final String SOURCE_TYPE_ID = "maven-build-source";
  private static final String NAME_ATTRIBUTE = "name";

  public MavenBuildDeploymentSourceType() {
    super(SOURCE_TYPE_ID);
  }

  @NotNull
  @Override
  protected List<? extends BeforeRunTask> getBuildTasks(
      RunManagerEx runManager, RunConfiguration configuration) {
    return runManager.getBeforeRunTasks(configuration, MavenBeforeRunTasksProvider.ID);
  }

  @Nullable
  @Override
  protected BeforeRunTask createBuildTask(@NotNull Module module) {
    String mavenModulePath = getMavenModulePath(module);

    if (mavenModulePath != null) {
      MavenBeforeRunTask task = new MavenBeforeRunTask();

      task.setProjectPath(mavenModulePath);
      task.setGoal(MAVEN_TASK_PACKAGE);
      task.setEnabled(true);

      return task;
    } else {
      return null;
    }
  }

  @NotNull
  @Override
  public MavenBuildDeploymentSource load(@NotNull Element tag, @NotNull Project project) {
    final String moduleName = tag.getAttributeValue(NAME_ATTRIBUTE);

    MavenBuildDeploymentSource source =
        new MavenBuildDeploymentSource(
            ModulePointerManager.getInstance(project).create(moduleName), project);
    source.setProjectName(tag.getAttributeValue(CLOUD_PROJECT_ATTRIBUTE));
    source.setVersion(tag.getAttributeValue(APP_ENGINE_VERSION_ATTRIBUTE));

    return source;
  }

  @Override
  public void save(@NotNull ModuleDeploymentSource deploymentSource, @NotNull Element tag) {
    tag.setAttribute(NAME_ATTRIBUTE, deploymentSource.getModulePointer().getModuleName());

    super.save(deploymentSource, tag);
  }

  @Override
  protected boolean hasBuildTaskForModule(
      Collection<? extends BeforeRunTask> beforeRunTasks, final Module module) {
    return !Collections2.filter(
            beforeRunTasks,
            new Predicate<BeforeRunTask>() {
              @Override
              public boolean apply(@Nullable BeforeRunTask beforeRunTask) {
                return beforeRunTask != null
                    && beforeRunTask instanceof MavenBeforeRunTask
                    && MAVEN_TASK_PACKAGE.equals(((MavenBeforeRunTask) beforeRunTask).getGoal())
                    && ((MavenBeforeRunTask) beforeRunTask)
                        .getProjectPath()
                        .equals(getMavenModulePath(module));
              }
            })
        .isEmpty();
  }

  @Nullable
  private String getMavenModulePath(Module module) {
    MavenProject mavenProject =
        MavenProjectsManager.getInstance(module.getProject()).findProject(module);

    if (mavenProject == null) {
      return null;
    }

    return mavenProject.getFile().getPath();
  }
}
