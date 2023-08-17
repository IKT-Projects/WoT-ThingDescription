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
package org.ict.model.wot.core;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author F. Kohlmorgen
 */
@Data
@EqualsAndHashCode
public class SystemProperty implements SystemPropertyIf {
  @SerializedName("@type")
  private List<URI> atType = Arrays.asList(URI.create("http://www.w3.org/ns/ssn/Property"),
      URI.create("http://schema.org/PropertyValue"));

  @SerializedName("instanceOf")
  private String instance;

  public SystemProperty(String instance) {
    this.instance = instance;
  }

  /**
   * RDF Bean sets RDF type from the RDF Bean annotation. We use at type (also RDF type) as
   * additional type annotations and don't want the RDFBean annotations in the atType List. We can
   * detect the RDFBean type by class checking. The class is always URI. So every URI gets filtered.
   * 
   * @param types
   */
  public void setAtTypeRDF(List<URI> types) {
    List<URI> filtered = types.stream()
        .filter(u -> !u.equals(URI.create("http://www.w3.org/ns/ssn/systems/SystemCapability")))
        .collect(Collectors.toList());
    if (filtered.isEmpty()) {
      this.atType = null;
    } else {
      this.atType = filtered;
    }
  }

  public List<URI> getAtTypeRDF() {
    return atType;
  }
}
