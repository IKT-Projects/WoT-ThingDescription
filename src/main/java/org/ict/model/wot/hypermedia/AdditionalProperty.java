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
package org.ict.model.wot.hypermedia;

import org.ict.model.wot.hypermedia.iface.AdditionalPropertyIf;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author F. Kohlmorgen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalProperty implements AdditionalPropertyIf {
  private String pname;
  private JsonElement element;

  public String getStrElement() {
    return element.toString();
  }

  public void setStrElement(String strElement) {
    this.element = new JsonParser().parse(strElement);
  }
}
