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

package com.google.cloud.tools.intellij.appengine.cloud.flexible;

import com.google.cloud.tools.intellij.util.GctBundle;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.text.StringUtil;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A widget for selecting a folder in which to generate Flexible source files. */
public class SelectConfigDestinationFolderDialog extends DialogWrapper {

  private JPanel rootPanel;
  private TextFieldWithBrowseButton destinationFolderChooser;

  /** Initialize the widget and set the default paths. */
  public SelectConfigDestinationFolderDialog(@Nullable Project project, String filePath) {
    super(project);
    setTitle(GctBundle.message("appengine.flex.config.destination.chooser.title"));

    init();
    destinationFolderChooser.addBrowseFolderListener(
        GctBundle.message("appengine.flex.config.choose.destination.folder.window.title"),
        null,
        project,
        FileChooserDescriptorFactory.createSingleFolderDescriptor());

    // Suggests the parent of the specified file as directory.
    if (filePath != null) {
      try {
        Path file = Paths.get(filePath);
        if (file.getParent() != null) {
          destinationFolderChooser.setText(file.getParent().toString());
        }
      } catch (InvalidPathException ipe) {
        // Do not assume any default directory and let the user specify one.
      }
    }
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return rootPanel;
  }

  public Path getDestinationFolder() {
    return Paths.get(destinationFolderChooser.getText());
  }

  @NotNull
  @Override
  protected Action[] createActions() {
    getOKAction().putValue(Action.NAME, "Generate");
    return super.createActions();
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (StringUtil.isEmpty(destinationFolderChooser.getText())) {
      return new ValidationInfo(
          GctBundle.message("appengine.flex.config.destination.chooser.directory.missing"),
          destinationFolderChooser);
    }
    return null;
  }
}
