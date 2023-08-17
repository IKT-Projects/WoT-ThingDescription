/*
 * Copyright © 2023 Institut fuer Kommunikationstechnik - FH-Dortmund (codebase.ikt@fh-dortmund.de)
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
package org.ict.model.ssn.system.properties;

import org.ict.model.wot.core.SystemProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DetectionLimit extends SystemProperty implements DetectionLimitIf {
  private float value;
  private String unitCode;

  public DetectionLimit() {
    super(DetectionLimit.class.getName());
  }

  @Builder
  public DetectionLimit(float value, String unitCode) {
    super(DetectionLimit.class.getName());
    this.value = value;
    this.unitCode = unitCode;
  }
}