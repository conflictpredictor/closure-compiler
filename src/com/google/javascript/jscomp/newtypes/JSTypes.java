/*
 * Copyright 2014 The Closure Compiler Authors.
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
 */

package com.google.javascript.jscomp.newtypes;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

/**
 * This class contains commonly used types, accessible from the jscomp package.
 * Also, any JSType utility methods that do not need to be in JSType.
 *
 * There should only be one instance of this class per Compiler object.
 *
 * @author blickly@google.com (Ben Lickly)
 * @author dimvar@google.com (Dimitris Vardoulakis)
 */
public class JSTypes {
  // Instances of Boolean, Number and String; used for auto-boxing scalars.
  // Set at the end of GlobalTypeInfo.
  private JSType NUMBER_INSTANCE;
  private JSType BOOLEAN_INSTANCE;
  private JSType STRING_INSTANCE;

  private JSType NUMBER_OR_number;
  private JSType STRING_OR_string;
  private JSType BOOLEAN_OR_boolean;
  private JSType anyNumOrStr;

  private JSTypes() {}

  public static JSTypes make() {
    return new JSTypes();
  }

  public void setNumberInstance(JSType t) {
    Preconditions.checkState(NUMBER_INSTANCE == null);
    Preconditions.checkNotNull(t);
    NUMBER_INSTANCE = t;
    NUMBER_OR_number = t.isUnknown()
        ? JSType.NUMBER : JSType.join(JSType.NUMBER, NUMBER_INSTANCE);
    if (STRING_INSTANCE != null) {
      anyNumOrStr = JSType.join(NUMBER_OR_number, STRING_OR_string);
    }
  }

  public void setBooleanInstance(JSType t) {
    Preconditions.checkState(BOOLEAN_INSTANCE == null);
    Preconditions.checkNotNull(t);
    BOOLEAN_INSTANCE = t;
    BOOLEAN_OR_boolean = t.isUnknown()
        ? JSType.BOOLEAN : JSType.join(JSType.BOOLEAN, BOOLEAN_INSTANCE);
  }

  public void setStringInstance(JSType t) {
    Preconditions.checkState(STRING_INSTANCE == null);
    Preconditions.checkNotNull(t);
    STRING_INSTANCE = t;
    STRING_OR_string = t.isUnknown()
        ? JSType.STRING : JSType.join(JSType.STRING, STRING_INSTANCE);
    if (NUMBER_INSTANCE != null) {
      anyNumOrStr = JSType.join(NUMBER_OR_number, STRING_OR_string);
    }
  }

  public boolean isNumberScalarOrObj(JSType t) {
    return t.isSubtypeOf(NUMBER_OR_number);
  }

  public boolean isStringScalarOrObj(JSType t) {
    return t.isSubtypeOf(STRING_OR_string);
  }

  // This method is a bit ad-hoc, but it allows us to not make the boxed
  // instances (which are not final) public.
  public boolean isNumStrScalarOrObj(JSType t) {
    return t.isSubtypeOf(anyNumOrStr);
  }
}
