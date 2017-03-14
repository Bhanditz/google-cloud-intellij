/*
 * Copyright 2017 Google Inc. All Rights Reserved.
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

package com.google.cloud.tools.intellij.vcs;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.AbstractVcs;
import com.intellij.openapi.vcs.actions.VcsQuickListContentProvider;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Adds Google Cloud Platform checkout to VCS quicklist. */
public class GcpQuickListContentProvider implements VcsQuickListContentProvider {

  @Override
  public List<AnAction> getVcsActions(
      @Nullable Project project,
      @Nullable AbstractVcs activeVcs,
      @Nullable DataContext dataContext) {
    return null;
  }

  @Override
  public List<AnAction> getNotInVcsActions(
      @Nullable Project project, @Nullable DataContext dataContext) {
    final AnAction action =
        ActionManager.getInstance().getAction("GoogleCloudTools.UploadSourceToGCP");
    return Collections.singletonList(action);
  }

  @Override
  public boolean replaceVcsActionsFor(
      @NotNull AbstractVcs activeVcs, @Nullable DataContext dataContext) {
    return false;
  }
}
