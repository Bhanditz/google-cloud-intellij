/*
 * Copyright (C) 2013 The Android Open Source Project
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
package com.google.gct.intellij.endpoints.validation;

import com.google.gct.intellij.endpoints.GctConstants;
import com.google.gct.intellij.endpoints.util.EndpointBundle;
import com.google.gct.intellij.endpoints.util.EndpointUtilities;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 *  Inspection to check that a specified method name provided in @ApiMethod
 *  is in the correct pattern.
 */
public class MethodNameInspection extends EndpointInspectionBase {
  private static final String API_NAME_ATTRIBUTE = "name";
  private static final Pattern API_NAME_PATTERN = Pattern.compile("^\\w+(\\.\\w+)*$");

  @Override
  @Nullable
  public String getStaticDescription() {
    return EndpointBundle.message("method.name.description");
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return EndpointBundle.message("method.name.name");
  }

  @NotNull
  @Override
  public String getShortName() {
    return EndpointBundle.message("method.name.short.name");
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new EndpointPsiElementVisitor() {
      @Override
      public void visitAnnotation(PsiAnnotation annotation) {
        if (!isEndpointClass(annotation)) {
          return;
        }

        if(!annotation.getQualifiedName().equals(GctConstants.APP_ENGINE_ANNOTATION_API_METHOD)) {
          return;
        }

        PsiAnnotationMemberValue memberValue = annotation.findAttributeValue(API_NAME_ATTRIBUTE);
        if(memberValue == null) {
          return;
        }

        String nameValueWithQuotes = memberValue.getText();
        String nameValue = EndpointUtilities.removeBeginningAndEndingQuotes(nameValueWithQuotes);
        if(nameValue.isEmpty()) {
          return;
        }

        if (!API_NAME_PATTERN.matcher(nameValue).matches()) {
          holder.registerProblem(annotation, "Invalid method name: letters, digits, underscores and dots are acceptable characters. " +
            "Leading and trailing dots are prohibited.", LocalQuickFix.EMPTY_ARRAY);
        }
      }
    };
  }
}
